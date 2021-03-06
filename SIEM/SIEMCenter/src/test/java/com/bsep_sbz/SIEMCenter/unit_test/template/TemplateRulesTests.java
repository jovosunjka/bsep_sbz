package com.bsep_sbz.SIEMCenter.unit_test.template;

import com.bsep_sbz.SIEMCenter.helper.HelperMethods;
import com.bsep_sbz.SIEMCenter.model.sbz.enums.log.LogCategory;
import com.bsep_sbz.SIEMCenter.model.sbz.enums.log.LogLevel;
import com.bsep_sbz.SIEMCenter.model.sbz.log.Alarm;
import com.bsep_sbz.SIEMCenter.model.sbz.log.Log;
import com.bsep_sbz.SIEMCenter.model.sbz.rule.LoginData;
import com.bsep_sbz.SIEMCenter.sbz.KnowledgeSessionHelper;
import com.bsep_sbz.SIEMCenter.unit_test.template.model.AttackData;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.StringUtils;
import org.drools.template.*;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.InputStream;

import static org.junit.Assert.*;

public class TemplateRulesTests {

    @Test
    public void testAttackTemplateCreation() throws IOException {
        InputStream template = new FileInputStream(
                "..\\SiemCenterRules\\src\\main\\resources\\sbz\\rules\\templates\\attack.drt");

        int logCount = 50;
        String time = "60s";
        List<AttackData> data = new ArrayList<>();
        data.add(new AttackData(LogCategory.PAYMENT_SYSTEM.name(), logCount, time));
        data.add(new AttackData(LogCategory.LOGIN.name(), logCount, time)); // brute force
        data.add(new AttackData(LogCategory.APP.name(), logCount, time)); // DOS

        ObjectDataCompiler compiler = new ObjectDataCompiler();

        // Act
        String drl = compiler.compile(data, template);
        System.out.println(drl);

        // Assert
        int numOfPaymentSystemStrs = StringUtils.countMatches(drl, LogCategory.PAYMENT_SYSTEM.name());
        assertEquals(2, numOfPaymentSystemStrs);
        int numOfAppStrs = StringUtils.countMatches(drl, LogCategory.APP.name());
        assertEquals(2, numOfAppStrs);
        int numOfLoginStrs = StringUtils.countMatches(drl, LogCategory.LOGIN.name());
        assertEquals(2, numOfLoginStrs);
        int numOfLogCountStrs = StringUtils.countMatches(drl, Integer.toString(logCount));
        assertEquals(3, numOfLogCountStrs);
        int numOfTimeStrs = StringUtils.countMatches(drl, time);
        assertEquals(3, numOfTimeStrs);
    }

    @Test
    public void testLoginTemplateCreation() throws IOException{
        // Arrange
        InputStream template = new FileInputStream(
                "..\\SiemCenterRules\\src\\main\\resources\\sbz\\" +
                        "rules\\templates\\login_attempt.drt");

        List<LoginData> data = new ArrayList<>();
        data.add(new LoginData(2,10,"s", "==", "!=",
                true, 0));
        data.add(new LoginData(30,5,"h", "!=", "==",
                false, 1));

        ObjectDataCompiler compiler = new ObjectDataCompiler();

        // Act
        String drl = compiler.compile(data, template);
        System.out.println(drl);

        // Assert
        int numOfRules = StringUtils.countMatches(drl, "when");
        assertEquals(2, numOfRules);
        assertTrue(drl.contains("source == $s"));
        assertTrue(drl.contains("Login attempt rule_0"));
        assertTrue(drl.contains("Login attempt rule_1"));
        assertTrue(drl.contains("source != $s"));
        assertTrue(drl.contains("hostAddress == $h"));
        assertTrue(drl.contains("hostAddress != $h"));
        assertTrue(drl.contains("intValue >= 2"));
        assertTrue(drl.contains("intValue >= 30"));
        assertTrue(drl.contains("over window:time(5h)"));
        assertTrue(drl.contains("over window:time(10s)"));
    }

    @Test
    public void testLoginTemplateWholeFlow() throws IOException, MavenInvocationException {
        String templatePath = "..\\SiemCenterRules\\src\\main\\resources\\sbz\\" +
                "rules\\templates\\login_attempt.drt";
        String drlPath = "..\\SiemCenterRules\\src\\main\\resources\\sbz\\" +
                "rules\\LoginAttemptRule0.drl";
        String message = "Login attempt rule_0";

        // 1) Read template
        InputStream template = new FileInputStream(templatePath);

        // 2) Compile template to drl rule
        List<LoginData> data = new ArrayList<>();
        data.add(new LoginData(10,20,"s", "==", "!=",
                false, 0));
        String drl = (new ObjectDataCompiler()).compile(data, template);
        assertTrue(drl.contains(message));

        // 3) Save drl rule to file
        Files.write( Paths.get(drlPath), drl.getBytes(), StandardOpenOption.CREATE);

        // 4) maven clean, maven install
        HelperMethods.mavenCleanAndInstallRules();

        // 5) trigger rule
        KieSession kieSession = KnowledgeSessionHelper.getKieSession("logs-session");
        String host = "12.21.21.22";
        List<Log> logs = getLoginLogsWithSameHost(10, host);
        logs.forEach(kieSession::insert);
        kieSession.getAgenda().getAgendaGroup("app").setFocus();
        kieSession.fireAllRules();

        // 6) assert alarm
        QueryResults results = kieSession.getQueryResults("Get all alarms");
        Alarm alarm = null;
        for(QueryResultsRow r : results) {
            Alarm a = (Alarm)r.get("$a");
            if(a.getMessage().equals(message)) {
                alarm = a;
                break;
            }
        }
        try {
            assertNotNull(alarm);
        } catch (AssertionError e) {
            boolean isDeleted = (new File(drlPath)).delete();
            assertTrue(isDeleted);
            throw e;
        }
        // 7) delete rule
        boolean isDeleted = (new File(drlPath)).delete();
        assertTrue(isDeleted);
    }

    private List<Log> getLoginLogsWithSameHost(int count, String host) {
        List<Log> logs = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            Log log = new Log();
            log.setId(i + 1L);
            log.setType(LogLevel.WARN);
            log.setHostAddress(host);
            log.setSource(String.format("127.%d2.8%d.1", i, i));
            log.setCategory(LogCategory.LOGIN);
            Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            log.setTimestamp(date);
            logs.add(log);
        }
        return logs;
    }
}

package com.bsep.SiemCenterRules.model;



import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AntivirusLog extends Log {
    private AntivirusLog relatedLog;

    public static long diffrenceInHours(LocalDateTime ld1, LocalDateTime ld2) {
        long difference = ChronoUnit.HOURS.between(ld1, ld2);
        System.out.println(difference);
        return difference;
    }

    public AntivirusLog() {

    }



    public AntivirusLog getRelatedLog() {
        return relatedLog;
    }

    public void setRelatedLog(AntivirusLog relatedLog) {
        this.relatedLog = relatedLog;
    }
}

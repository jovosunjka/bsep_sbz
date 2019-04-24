package com.bsep.SIEMCenter.model.sbz;




import com.bsep.SIEMCenter.model.sbz.enums.AccountType;
import com.bsep.SIEMCenter.model.sbz.enums.RiskLevel;

import java.time.LocalDateTime;

public class UserAccount {
    private String username;
    private LocalDateTime lastLoginTimestamp;
    private RiskLevel riskLevel;
    private AccountType accountType;

    public UserAccount() {

    }

    public UserAccount(String username, LocalDateTime lastLoginTimestamp, RiskLevel riskLevel, AccountType accountType) {
        this.username = username;
        this.lastLoginTimestamp = lastLoginTimestamp;
        this.riskLevel = riskLevel;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(LocalDateTime lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}

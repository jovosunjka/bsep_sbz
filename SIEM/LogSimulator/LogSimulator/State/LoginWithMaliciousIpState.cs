﻿using LogSimulator.Service.Interface;

namespace LogSimulator.State
{
    public class LoginWithMaliciousIpState : IState
    {
        public string Description => Helper.Constants.StateDescription.LoginWithMaliciousIp;

        public void Simulate(IAppSettings appSettings, ILogService logService)
        {
            var log = logService.GetLog($"Login attempt with username '{appSettings.MaliciousUsername}' from ip address '{appSettings.MaliciousIpAddress}'");
            logService.WriteLogToFile(appSettings.LogsFilePath, log);
        }
    }
}

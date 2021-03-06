﻿using LogSimulator.Model.Enum;
using LogSimulator.Service.Interface;

namespace LogSimulator.State
{
    /// <summary>
    /// Pojava loga čiji tip je ERROR
    /// </summary>
    public class LogErrorState : IState
    {
        public string Description => Helper.Constants.StateDescription.LogError;

        public void Simulate(IAppSettings appSettings, ILogService logService)
        {
            var errorLog = logService.GetLog("Exception in thread main java.lang.NullPointerException", LogCategory.APP, LogLevelType.ERROR);
            var traceLog1 = logService.GetLog("at com.example.myproject.Flight.getTitle(Flight.java:16)", LogCategory.APP, LogLevelType.TRACE);
            var traceLog2 = logService.GetLog("at com.example.myproject.Ticket.getBookTitles(Ticket.java:25)", LogCategory.APP, LogLevelType.TRACE);
            // trace logs are related to same (error) event
            var eventId = errorLog.EventId;
            traceLog1.EventId = eventId;
            traceLog2.EventId = eventId;

            logService.WriteLogToFile(appSettings.OtherLogsFolderPath, errorLog);
            logService.WriteLogToFile(appSettings.OtherLogsFolderPath, traceLog1);
            logService.WriteLogToFile(appSettings.OtherLogsFolderPath, traceLog2);
        }
    }
}

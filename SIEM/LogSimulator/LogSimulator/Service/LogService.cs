﻿using LogSimulator.Model;
using LogSimulator.Model.Enum;
using LogSimulator.Service.Interface;
using System;
using System.IO;
using System.Threading;

namespace LogSimulator.Service
{
    public class LogService : ILogService
    {
        private readonly IAppSettings _appSettings;
        private readonly Mutex _mutex;

        public LogService(IAppSettings appSettings)
        {
            _appSettings = appSettings;
            _mutex = new Mutex();
        }

        public Log GetLog(string message, LogLevelType logLevelType = LogLevelType.INFO)
        {
            var id = GetNextLogId();
            return new Log
            {
                Id = id,
                EventId = id,
                TimeStamp = DateTime.UtcNow,
                LogLevelType = logLevelType,
                Message = message
            };
        }

        public long GetNextLogId()
        {
            lock (_mutex)
            {
                var nextId = long.Parse(_appSettings.LogSequencerCurrentValue);
                try
                {
                    return nextId;
                }
                finally
                {
                    _appSettings.LogSequencerCurrentValue = (++nextId).ToString();
                }
            }
        }

        public long GetNextAntivirusThreatId()
        {
            lock (_mutex)
            {
                var nextId = long.Parse(_appSettings.AntivirusThreatSequencerCurrentValue);
                try
                {
                    return nextId;
                }
                finally
                {
                    _appSettings.AntivirusThreatSequencerCurrentValue = (++nextId).ToString();
                }
            }
        }

        public void WriteLogToFile(string path, Log log)
        {
            lock (_mutex)
            {
                File.AppendAllText(path, log.ToString() + "\n");
            }
        }
    }
}

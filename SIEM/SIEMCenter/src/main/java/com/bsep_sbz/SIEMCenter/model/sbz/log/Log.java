package com.bsep_sbz.SIEMCenter.model.sbz.log;


import com.bsep_sbz.SIEMCenter.model.sbz.enums.log.LogCategory;
import com.bsep_sbz.SIEMCenter.model.sbz.enums.log.LogLevel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Log {
    private Long id;
    private LogLevel type;
    private LogCategory category;
    private String source;
    private Date timestamp;
    private String hostAddress;
    private String message;
    // attribute1:value1,attribute2:value2,attribute3:value3, ...  (message format)

    private final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Log() {

    }
    
    
    public Log(Long id, LogLevel type, LogCategory category, Date timestamp, String source, String hostAddress, String message) throws ParseException {
        this.id = id;
        this.type = type;
        this.category = category;
        this.timestamp = timestamp;
        this.source = source;
        this.hostAddress = hostAddress;
        this.message = message;
    }

    public Log(Long id, LogLevel type, LogCategory category, String timestampStr, String source, String hostAddress, String message) throws ParseException {
        this.id = id;
        this.type = type;
        this.category = category;
        this.timestamp = timestampStr.contains("/") ? sdf2.parse(timestampStr) : sdf1.parse(timestampStr);

        this.source = source;
        this.hostAddress = hostAddress;
        this.message = message;
    }

    public static long getDaysOfInactivity(long d1Long, long d2Long) {
        Date d1 = new Date(d1Long);
        long diffInMillies = Math.abs(d2Long - d1Long);
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        System.out.println("getDaysOfInactivity(): " + diff);
        return diff;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogLevel getType() {
        return type;
    }

    public void setType(LogLevel type) {
        this.type = type;
    }

    public LogCategory getCategory() { return category; }

    public void setCategory(LogCategory category) { this.category = category; }

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    /*public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    */

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return id.equals(log.id) &&
                type == log.type &&
                timestamp.equals(log.timestamp) &&
                hostAddress.equals(log.hostAddress) &&
                message.equals(log.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, timestamp, hostAddress, message);
    }

    @Override
    public String toString() {
        return "Log(id="+id.longValue()+", type="+type.name()+", category="+category.name()+", source="+source
                +", timestamp="+sdf1.format(timestamp)+", hostAddress="+hostAddress+", message=" + message+")";
    }
}

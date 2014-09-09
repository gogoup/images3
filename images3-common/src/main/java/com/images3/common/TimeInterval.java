package com.images3.common;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeInterval {

    private Date start;
    private long interval;
    private TimeUnit unit;
    
    public TimeInterval(Date start, long interval, TimeUnit unit) {
        this.start = start;
        this.interval = interval;
        this.unit = unit;
    }

    public Date getStart() {
        return start;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getUnit() {
        return unit;
    }
    
}

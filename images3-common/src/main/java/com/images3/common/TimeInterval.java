package com.images3.common;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeInterval {

    private Date start;
    private Date end;
    private long length;
    private TimeUnit unit;
    
    public TimeInterval(Date start, long length, TimeUnit unit) {
        this.start = start;
        this.end = null;
        this.length = length;
        this.unit = unit;
    }

    public Date getStart() {
        return start;
    }

    public long getLength() {
        return length;
    }

    public TimeUnit getUnit() {
        return unit;
    }
    
    public Date getEnd() {
        if (null != end) {
            return end;
        }
        end = new Date(getNextTime(getStart().getTime(), getLength()));
        return end;
    }
    
    public List<Date> getIntervals() {
        List<Date> intervals = new LinkedList<Date>();
        intervals.add(getStart());
        long nextTime = getStart().getTime();
        for (int i=0; i<getLength(); i++) {
            nextTime = getNextTime(nextTime, 1);
            intervals.add(new Date(nextTime));
        }
        return intervals;
    }
    
    private long getNextTime(long time, long length) {
        long startTime = time;
        long endTime = startTime;
        if (getUnit() == TimeUnit.MILLISECONDS) {
            endTime += length;
        } else if (getUnit() == TimeUnit.SECONDS) {
            endTime += length * 1000;
        } else if (getUnit() == TimeUnit.MINUTES) {
            endTime += (length * 1000 * 60);
        } else if (getUnit() == TimeUnit.HOURS) {
            endTime += (length * 1000 * 60 * 60);
        } else if (getUnit() == TimeUnit.DAYS) {
            endTime += (length * 1000 * 60 * 60 * 24);
        }
        return endTime;
    }
    
}

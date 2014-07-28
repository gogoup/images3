package com.images3.common;

public class PageCursor {
    
    private static final long MINIMAL_START = 1;
    private static final long ALL_PAGE_SIZE = -1;

    private long start;
    private long size;
    
    public PageCursor() {
        this(1, 0);
    }
    
    public PageCursor(long start, long size) {
        setStart(start);
        setSize(size);
    }
    
    private void checkIfValidStart() {
        if (this.start < MINIMAL_START) {
            throw new IllegalArgumentException("Page start need to be larger than " + MINIMAL_START);
        }
    }
    
    private void checkIfValidSize() {
        if (this.size < ALL_PAGE_SIZE || this.size == 0) {
            throw new IllegalArgumentException("Page size need to be " + ALL_PAGE_SIZE + " or larger than 0.");
        }
    }

    public long getStart() {
        return start;
    }
    
    public long getSize() {
        return size;
    }
    
    private void setStart(long start) {
        this.start = start;
        checkIfValidStart();
    }

    private void setSize(long size) {
        this.size = size;
        checkIfValidSize();
    }

    public PageCursor startAtPage(long start) {
        setStart(start);
        return this;
    }
    
    public PageCursor withSize(long size) {
        setSize(size);
        return this;
    }
    
    public PageCursor withAll() {
        setSize(ALL_PAGE_SIZE);;
        return this;
    }
    
}

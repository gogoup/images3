package com.images3.utility;

public class PageCursor {
    
    private static final int MINIMAL_START = 1;
    private static final int ALL_PAGE_SIZE = Integer.MAX_VALUE;

    private int start;
    private int size;
    
    public PageCursor() {
        this(1, 0);
    }
    
    public PageCursor(int start, int size) {
        setStart(start);
        setSize(size);
    }
    
    private void checkIfValidStart() {
        if (this.start < MINIMAL_START) {
            throw new IllegalArgumentException("Page start need to be larger than " + MINIMAL_START);
        }
    }
    
    private void checkIfValidSize() {
        if (this.size < 0) {
            throw new IllegalArgumentException("Page size need to be larger than or equal to 0.");
        }
    }

    public int getStart() {
        return start;
    }
    
    public int getSize() {
        return size;
    }
    
    private void setStart(int start) {
        this.start = start;
        checkIfValidStart();
    }

    private void setSize(int size) {
        this.size = size;
        checkIfValidSize();
    }

    public PageCursor startAtPage(int start) {
        setStart(start);
        return this;
    }
    
    public PageCursor withSize(int size) {
        setSize(size);
        return this;
    }
    
    public PageCursor withAll() {
        setSize(ALL_PAGE_SIZE);;
        return this;
    }
    
}

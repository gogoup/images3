package com.images3.common;

public interface PaginatedResultDelegate<T> {
    
    public T fetchResult(String methodName, Object[] arguments, Object pageCursor);
    
    /**
     * Returns next page cursor based on the giving current page cursor.
     * 
     * @param methodName String
     * @param arguments Object[]
     * @param pageCursor Object
     * @return Object - returns the first page cursor, if the giving current page cursor is null.
     */
    public Object getNextPageCursor(String methodName, Object[] arguments, Object pageCursor);
    
}

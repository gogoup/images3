/**
 * Copyright [2014] [SteveSun21]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.images3.utility;

public abstract class PaginatedResult<T> {

    private PaginatedResultDelegate<T> delegate;
    private String methodName;
    private Object[] arguments;
    private Object currentPageCursor;

    public PaginatedResult(PaginatedResultDelegate<T> delegate, String methodName, Object[] arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.delegate = delegate;
    }
    
    public T getAllResults() {
        return getResult(null);
    }
    
    public T getResult(Object pageCursor) {
        T result = delegate.fetchResult(methodName, arguments, pageCursor);
        setCurrentPageCursor(pageCursor);
        return result;
    }
    
    private void setCurrentPageCursor(Object pageCursor) {
        currentPageCursor = pageCursor;
    }
    
    public Object getCurrentPageCursor() {
        return currentPageCursor;
    }
    
    public Object getNextPageCursor() {
        checkForNullDelegate();
        return delegate.getNextPageCursor(methodName, arguments, getCurrentPageCursor());
    }
    
    private void checkForNullDelegate() {
        if (null == delegate) {
            throw new NullPointerException(
                    "Need set delegate (" + PaginatedResultDelegate.class.getName() + ") first.");
        }
    }
        
}
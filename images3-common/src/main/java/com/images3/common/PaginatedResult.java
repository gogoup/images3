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
package com.images3.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
 
public abstract class PaginatedResult<T> {
        
        private Object delegate;
        private String methodName;
        private Object[] arguments;
        private Method method;
        
        public PaginatedResult(Object delegate, String methodName, Object[] arguments) {
                this.delegate = delegate;
                this.methodName = methodName;
                this.arguments = arguments;
                loadMethod();
        }
        
        private void loadMethod() {
                try {
                        Class<?>[] parameterClasses = getParameterClasses();
                        method = delegate.getClass().getDeclaredMethod(methodName, parameterClasses);
                        method.setAccessible(true);
                } catch (NoSuchMethodException | SecurityException e) {
                        e.printStackTrace();
                }
                checkForReturnType();
        }
        
        private Class<?>[] getParameterClasses() {
                Class<?>[] parameterClasses = new Class<?>[this.arguments.length + 1];
                for (int i=0; i<this.arguments.length; i++) {
                        parameterClasses[i] = this.arguments[i].getClass();
                }
                parameterClasses[parameterClasses.length - 1] = Object.class;
                return parameterClasses;
        }
        
        private void checkForReturnType() {
                Type returnElementType = this.method.getGenericReturnType();
                Type requiredType = getTypeArgument();
                if (!requiredType.equals(returnElementType)) {
                        throw new IllegalArgumentException("Need to return List<T>");
                }
        }
        
        private Type getTypeArgument() {
                Type superClass = this.getClass().getGenericSuperclass();
                Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
                return type;
        }
 
        public T getAllResults() {
                return getResult(null);
        }
        
        @SuppressWarnings("unchecked")
        public T getResult(Object pageCursor) {
                T result = null;
                try {
                        result = (T) method.invoke(delegate, addPageCursorToArguments(null));
                } catch (IllegalAccessException | IllegalArgumentException
                                | InvocationTargetException e) {
                        e.printStackTrace();
                }
                return result;
        }
        
        private Object[] addPageCursorToArguments(Object pageCursor) {
                Object[] clone = new Object[arguments.length + 1];
                for (int i=0; i<arguments.length; i++) {
                        clone[i] = arguments[i];
                }
                clone[clone.length - 1] = pageCursor;
                return clone;
        }
        
}
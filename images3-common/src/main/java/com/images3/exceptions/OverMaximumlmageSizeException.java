/*******************************************************************************
 * Copyright 2014 Rui Sun
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
 *******************************************************************************/
package com.images3.exceptions;

public class OverMaximumlmageSizeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 761892710857913781L;
    
    private int maximumImageSize;
    private int imageSize;

    public OverMaximumlmageSizeException(int maximumImageSize, int imageSize, String message) {
        super(message);
        this.maximumImageSize = maximumImageSize;
        this.imageSize = imageSize;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getMaximumImageSize() {
        return maximumImageSize;
    }

    public int getImageSize() {
        return imageSize;
    }
    
}

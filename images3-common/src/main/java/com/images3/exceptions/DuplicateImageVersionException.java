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

import com.images3.common.ImageVersion;

public class DuplicateImageVersionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2406908279068349252L;
    
    private ImageVersion version;
    
    public DuplicateImageVersionException(ImageVersion version, String message) {
        super(message);
        this.version = version;
    }

    public ImageVersion getVersion() {
        return version;
    }
    
}

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

import com.images3.common.ResizingUnit;

public class IllegalResizingDimensionsException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9093407154876964467L;
    
    private int minimum;
    private int maximum;
    private ResizingUnit unit;
    
    public IllegalResizingDimensionsException(int minimum,
            ResizingUnit unit, String msg) {
        
        //'-1' means no limitation on the value.
        this(minimum, -1, unit, msg);
    }
    
    public IllegalResizingDimensionsException(int minimum, int maximum,
            ResizingUnit unit, String msg) {
        super(msg);
        this.minimum = minimum;
        this.maximum = maximum;
        this.unit = unit;
    }

    public int getMinimum() {
        return minimum;
    }


    public int getMaximum() {
        return maximum;
    }

    public ResizingUnit getUnit() {
        return unit;
    }

}


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
package com.images3.common;

public class ResizingConfig {
    
    private ResizingUnit unit;
    private int width;
    private int height;
    private boolean isKeepProportions;
    
    public ResizingConfig(ResizingUnit unit, int width, int height,
            boolean isKeepProportions) {
        this.unit = unit;
        this.width = width;
        this.height = height;
        this.isKeepProportions = isKeepProportions;
    }
    
    public ResizingUnit getUnit() {
        return unit;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isKeepProportions() {
        return isKeepProportions;
    }

    @Override
    public String toString() {
        return "ResizingConfig [unit=" + unit + ", width=" + width
                + ", height=" + height + ", isKeepProportions="
                + isKeepProportions + "]";
    }
}

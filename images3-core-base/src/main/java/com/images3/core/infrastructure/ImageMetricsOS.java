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
package com.images3.core.infrastructure;

import java.util.Collections;
import java.util.Map;

import com.images3.common.ImageMetricsType;

public class ImageMetricsOS {

    private String imagePlantId;
    private String templateName;
    private long second; //second in timestamp.
    private Map<ImageMetricsType, Long> numbers;
    
    public ImageMetricsOS(String imagePlantId, String templateName,
            long second, Map<ImageMetricsType, Long> numbers) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.second = second;
        this.numbers = Collections.unmodifiableMap(numbers);
    }
    public String getImagePlantId() {
        return imagePlantId;
    }
    public String getTemplateName() {
        return templateName;
    }
    public long getSecond() {
        return second;
    }
    public Map<ImageMetricsType, Long> getNumbers() {
        return numbers;
    }
    
}

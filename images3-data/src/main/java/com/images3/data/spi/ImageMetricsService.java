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
package com.images3.data.spi;

import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

import com.images3.common.ImageMetricsType;
import com.images3.common.TemplateIdentity;
import com.images3.common.TimeInterval;
import com.images3.data.ImageMetricsOS;
import com.images3.data.ImageOS;

public interface ImageMetricsService extends PaginatedResultDelegate<List<ImageMetricsOS>> {

    public void recordInbound(ImageOS image);
    
    public void recordOutbound(ImageOS image);
    
    public long calculateNumber(String imagePlantId, ImageMetricsType type);
    
    public long calculateNumber(TemplateIdentity templateId, ImageMetricsType type);
    
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            String imagePlantId, TimeInterval interval);
    
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            TemplateIdentity templateId, TimeInterval interval);
    
}

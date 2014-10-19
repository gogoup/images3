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
package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.AutoPaginatedResultDelegate;
import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.core.ImagePlant;

public class ImagePlantPaginatedResultDelegate extends AutoPaginatedResultDelegate<List<ImagePlantResponse>> {
    
    private AppObjectMapper objectMapper;
    
    public ImagePlantPaginatedResultDelegate(AppObjectMapper objectMapper) {
        super(0, "getAllImagePlants");
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ImagePlantResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getAllImagePlants".equals(tag)) {
            PaginatedResult<List<ImagePlant>> result = (PaginatedResult<List<ImagePlant>>) arguments[0];
            List<ImagePlant> imagePlants = result.getResult(pageCursor);
            return getAllImagePlants(imagePlants);
        }
        throw new UnsupportedOperationException(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ImagePlantResponse> fetchAllResults(String tag,
            Object[] arguments) {
        if ("getAllImagePlants".equals(tag)) {
            PaginatedResult<List<ImagePlant>> result = (PaginatedResult<List<ImagePlant>>) arguments[0];
            List<ImagePlant> imagePlants = result.getAllResults();
            return getAllImagePlants(imagePlants);
        }
        throw new UnsupportedOperationException(tag);
    }

    private List<ImagePlantResponse> getAllImagePlants(List<ImagePlant> imagePlants) {
        List<ImagePlantResponse> responses = new ArrayList<ImagePlantResponse>(imagePlants.size());
        for (ImagePlant imagePlant: imagePlants) {
            responses.add(objectMapper.mapToResponse(imagePlant));
        }
        return responses;
    }

}

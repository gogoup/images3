package com.images3;

import java.util.ArrayList;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

import com.images3.core.ImagePlant;

public class ImagePlantPaginatedResultDelegate implements PaginatedResultDelegate<List<ImagePlantResponse>> {
    
    private AppObjectMapper objectMapper;
    
    public ImagePlantPaginatedResultDelegate(AppObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ImagePlantResponse> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getAllImagePlants".equals(tag)) {
            PaginatedResult<List<ImagePlant>> result = (PaginatedResult<List<ImagePlant>>) arguments[0];
            return getAllImagePlants(result, pageCursor);
        }
        throw new UnsupportedOperationException(tag);
    }
    
    private List<ImagePlantResponse> getAllImagePlants(
            PaginatedResult<List<ImagePlant>> result, Object pageCursor) {
        List<ImagePlant> imagePlants = result.getResult(pageCursor);
        List<ImagePlantResponse> responses = new ArrayList<ImagePlantResponse>(imagePlants.size());
        for (ImagePlant imagePlant: imagePlants) {
            responses.add(objectMapper.mapToResponse(imagePlant));
        }
        return responses;
    }
    

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor) {
        if (!"getAllImagePlants".equals(tag)) {
            throw new UnsupportedOperationException(tag);
        }
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
        return osResult.getNextPageCursor();
    }

}

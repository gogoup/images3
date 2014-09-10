package com.images3.core.models.imageplant;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.TimeInterval;
import com.images3.core.ImagePlant;
import com.images3.core.ImageReport;
import com.images3.core.ImageReporter;
import com.images3.core.Template;
import com.images3.core.infrastructure.ImageMetricsOS;
import com.images3.core.infrastructure.spi.ImageMetricsService;

public class ImageReporterService implements ImageReporter {
    
    private ImagePlant imagePlant;
    private Template template;
    private ImageMetricsService imageStatService;

    public ImageReporterService(ImagePlant imagePlant, 
            Template template, ImageMetricsService imageStatService) {
        this.imagePlant = imagePlant;
        this.template = template;
        this.imageStatService = imageStatService;
    }

    @Override
    public long countImages() {
        return imageStatService.calculateNumberOfImages(imagePlant.getId());
    }

    @Override
    public long countImages(TimeInterval interval) {
        PaginatedResult<List<ImageMetricsOS>> osResult =
                imageStatService.retrieveStats(imagePlant.getId(), interval);
        Object pageCursor = osResult.getFirstPageCursor();
        long counts = 0;
        while(null != pageCursor) {
            List<ImageMetricsOS> osList = osResult.getResult(pageCursor);
            counts += osList.size();
            pageCursor = osResult.getNextPageCursor();
        }
        return counts;
    }

    @Override
    public long calculateSizeOfImages() {
        return imageStatService.calculateSizeOfImages(imagePlant.getId());
    }

    @Override
    public long calculateSizeOfImages(TimeInterval interval) {
        PaginatedResult<List<ImageMetricsOS>> osResult =
                imageStatService.retrieveStats(imagePlant.getId(), interval);
        Object pageCursor = osResult.getFirstPageCursor();
        long size = 0;
        while(null != pageCursor) {
            List<ImageMetricsOS> osList = osResult.getResult(pageCursor);
            for (Iterator<ImageMetricsOS> iter = osList.iterator(); iter.hasNext();) {
                ImageMetricsOS os = iter.next();
                size += os.getSizeOfImages();
            }
            pageCursor = osResult.getNextPageCursor();
        }
        return size;
    }

    @Override
    public ImageReport fetchReport(TimeInterval interval) {
        long startTime = interval.getStart().getTime();
        
        return null;
    }

}

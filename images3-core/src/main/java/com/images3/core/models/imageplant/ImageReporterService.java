package com.images3.core.models.imageplant;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.ImageReportType;
import com.images3.common.TemplateIdentity;
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
        PaginatedResult<List<ImageMetricsOS>> osResult = getImageMetricsPages(interval);
        Object pageCursor = osResult.getFirstPageCursor();
        long counts = 0;
        while(null != pageCursor) {
            List<ImageMetricsOS> osList = osResult.getResult(pageCursor);
            for (Iterator<ImageMetricsOS> iter = osList.iterator(); iter.hasNext();) {
                ImageMetricsOS os = iter.next();
                counts += os.getNumberOfImages();
            }
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
        PaginatedResult<List<ImageMetricsOS>> osResult = getImageMetricsPages(interval);
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
    public ImageReport fetchReport(ImageReportType[] types, TimeInterval interval) {
        PaginatedResult<List<ImageMetricsOS>> osResult = getImageMetricsPages(interval);
        List<Date> intervals = interval.getIntervals();
        Map<ImageReportType, Map<Date, Long>> stats = 
                generateImageReportValueMap(types, intervals, interval.getUnit());
        stats = processImageReportMetrics(stats, osResult, types, interval);
        return new ImageReport(
                imagePlant,
                template,
                new LinkedList<Date>(intervals),
                convertToValues(stats), 
                interval.getUnit());
    }
    
    private Map<ImageReportType, Map<Date, Long>> generateImageReportValueMap(
            ImageReportType[] types, List<Date> intervals, TimeUnit unit) {
        Map<ImageReportType, Map<Date, Long>> stats = new LinkedHashMap<ImageReportType, Map<Date, Long>>();
        for (ImageReportType type: types) {
            stats.put(type, generateImageReportValues(intervals, unit));
        }
        return stats;
    }
    
    private Map<Date, Long> generateImageReportValues(List<Date> intervals, TimeUnit unit) {
        Map<Date, Long> values = new LinkedHashMap<Date, Long>();
        for (Date time: intervals) {
            long timestamp = getTimeScale(time.getTime(), unit);
            time = new Date(timestamp * 1000);
            values.put(time, 0L);
        }
        return values;
    }
    
    private Map<ImageReportType, Map<Date, Long>> processImageReportMetrics(
            Map<ImageReportType, Map<Date, Long>> stats, 
            PaginatedResult<List<ImageMetricsOS>> osResult, 
            ImageReportType[] types, TimeInterval interval) {
        Object pageCursor = osResult.getFirstPageCursor();
        while(null != pageCursor) {
            List<ImageMetricsOS> osList = osResult.getResult(pageCursor);
            for (Iterator<ImageMetricsOS> iter = osList.iterator(); iter.hasNext();) {
                ImageMetricsOS os = iter.next();
                for (ImageReportType type: types) {
                    Map<Date, Long> values = stats.get(type);
                    updateReportValue(values, os, type, interval);
                }
            }
            pageCursor = osResult.getNextPageCursor();
        }
        return stats;
    }
    
    private void updateReportValue(Map<Date, Long> values, ImageMetricsOS os,
            ImageReportType type, TimeInterval interval) {
        long timestamp = getTimeScale(os.getSecond() * 1000, interval.getUnit());
        Date time = new Date(timestamp * 1000);
        Long value = values.get(time);
        value = calculateReportValue(value, os, type);
        values.put(time, value);
    }
    
    private Long calculateReportValue(Long value, ImageMetricsOS os, ImageReportType type) {
        if (type == ImageReportType.COUNTS) {
            value += os.getNumberOfImages();
        } else if (type == ImageReportType.SIZE) {
            value += os.getSizeOfImages();
        }
        return value;
    }
    
    private Map<ImageReportType, List<Long>> convertToValues(Map<ImageReportType, Map<Date, Long>> stats) {
        Map<ImageReportType, List<Long>> values = new LinkedHashMap<ImageReportType, List<Long>>();
        for (Iterator<ImageReportType> iter = stats.keySet().iterator(); iter.hasNext();) {
            ImageReportType type = iter.next();
            Map<Date, Long> numbers = stats.get(type);
            values.put(type, new LinkedList<Long>(numbers.values()));
        }
        return values;
    }
    
    private PaginatedResult<List<ImageMetricsOS>> getImageMetricsPages(TimeInterval interval) {
        PaginatedResult<List<ImageMetricsOS>> osResult = null;
        if (null == template) {
            osResult = imageStatService.retrieveStats(imagePlant.getId(), interval);
        } else {
            osResult = imageStatService.retrieveStats(
                    new TemplateIdentity(imagePlant.getId(), template.getName()), interval);
        }
        return osResult;
    }
    
    private long getTimeScale(long time, TimeUnit unit) {
        long frame = 0;
        if (unit == TimeUnit.SECONDS) {
            frame = 1000;
        } else if (unit == TimeUnit.MINUTES) {
            frame = 1000 * 60;
        } else if (unit == TimeUnit.HOURS) {
            frame = 1000 * 60 * 60;
        } else if (unit == TimeUnit.DAYS) {
            frame = 1000 * 60 * 60 * 24;
        }
        long rest = time % frame;
        long timeScale = time - (rest);
        timeScale /= frame;
        return timeScale;
    }

}

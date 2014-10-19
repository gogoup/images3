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
package com.images3.core.models.imageplant;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.ImageMetricsType;
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
    public long calculate(ImageMetricsType type) {
        return imageStatService.calculateNumber(imagePlant.getId(), type);
    }

    @Override
    public long calculate(ImageMetricsType type, TimeInterval interval) {
        PaginatedResult<List<ImageMetricsOS>> osResult = getImageMetricsPages(interval);
        Object pageCursor = osResult.getFirstPageCursor();
        long size = 0;
        while(null != pageCursor) {
            List<ImageMetricsOS> osList = osResult.getResult(pageCursor);
            for (Iterator<ImageMetricsOS> iter = osList.iterator(); iter.hasNext();) {
                ImageMetricsOS os = iter.next();
                size += os.getNumbers().get(type);
            }
            pageCursor = osResult.getNextPageCursor();
        }
        return size;
    }

    @Override
    public ImageReport fetchReport(ImageMetricsType[] types, TimeInterval interval) {
        PaginatedResult<List<ImageMetricsOS>> osResult = getImageMetricsPages(interval);
        List<Date> intervals = interval.getIntervals();
        Map<ImageMetricsType, Map<Date, Long>> stats = 
                generateImageReportValueMap(types, intervals, interval.getUnit());
        stats = processImageReportMetrics(stats, osResult, types, interval);
        return new ImageReport(
                imagePlant,
                template,
                new LinkedList<Date>(intervals),
                convertToValues(stats), 
                interval.getUnit());
    }
    
    private Map<ImageMetricsType, Map<Date, Long>> generateImageReportValueMap(
            ImageMetricsType[] types, List<Date> intervals, TimeUnit unit) {
        Map<ImageMetricsType, Map<Date, Long>> stats = new LinkedHashMap<ImageMetricsType, Map<Date, Long>>();
        for (ImageMetricsType type: types) {
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
    
    private Map<ImageMetricsType, Map<Date, Long>> processImageReportMetrics(
            Map<ImageMetricsType, Map<Date, Long>> stats, 
            PaginatedResult<List<ImageMetricsOS>> osResult, 
            ImageMetricsType[] types, TimeInterval interval) {
        Object pageCursor = osResult.getFirstPageCursor();
        while(null != pageCursor) {
            List<ImageMetricsOS> osList = osResult.getResult(pageCursor);
            for (Iterator<ImageMetricsOS> iter = osList.iterator(); iter.hasNext();) {
                ImageMetricsOS os = iter.next();
                for (ImageMetricsType type: types) {
                    Map<Date, Long> values = stats.get(type);
                    updateReportValue(values, os, type, interval);
                }
            }
            pageCursor = osResult.getNextPageCursor();
        }
        return stats;
    }
    
    private void updateReportValue(Map<Date, Long> values, ImageMetricsOS os,
            ImageMetricsType type, TimeInterval interval) {
        long timestamp = getTimeScale(os.getSecond() * 1000, interval.getUnit());
        Date time = new Date(timestamp * 1000);
        Long value = values.get(time);
        value += os.getNumbers().get(type);
        values.put(time, value);
    }
    
    private Map<ImageMetricsType, List<Long>> convertToValues(Map<ImageMetricsType, Map<Date, Long>> stats) {
        Map<ImageMetricsType, List<Long>> values = new LinkedHashMap<ImageMetricsType, List<Long>>();
        for (Iterator<ImageMetricsType> iter = stats.keySet().iterator(); iter.hasNext();) {
            ImageMetricsType type = iter.next();
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

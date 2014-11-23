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
package com.images3.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ResizingConfig;

import org.gogoup.dddutils.pagination.PaginatedResult;

public interface ImagePlant {
    
    public String getId();
    
    public String getName();
    
    public void updateName(String name);
    
    public Date getCreationTime();
    
    public AmazonS3Bucket getAmazonS3Bucket();
    
    public void setAmazonS3Bucket(AmazonS3Bucket amazonS3Bucket);
    
    public Template getMasterTemplate();
    
    public Template createTemplate(String name, ResizingConfig resizingConfig);
    
    public void updateTemplate(Template template);
    
    public Template fetchTemplate(String name);
    
    public long countTemplates();
    
    public PaginatedResult<List<Template>> listAllTemplates();
    
    public PaginatedResult<List<Template>> listActiveTemplates();
    
    public PaginatedResult<List<Template>> listArchivedTemplates();
    
    public void removeTemplate(Template template);
    
    public Image createImage(File imageFile);
    
    public Image createImage(Version version);
    
    public void removeImage(Image image);
    
    public Image fetchImageById(String id);
    
    public boolean hasImageVersion(Version version);
    
    public Image fetchImageByVersion(Version version);
    
    public PaginatedResult<List<Image>> fetchVersioningImages(Image originalImage);
    
    public PaginatedResult<List<Image>> fetchImagesByTemplate(Template template);
    
    public PaginatedResult<List<Image>> listAllImages();
    
    public ImageReporter generateImageReporter();
    
    public ImageReporter generateImageReporter(Template template);
    
}

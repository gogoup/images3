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

import com.images3.common.ImageIdentity;
import com.images3.common.ImageVersion;
import com.images3.data.ImageOS;
import com.images3.data.ImagePlantOS;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public interface ImageAccess extends PaginatedResultDelegate<List<ImageOS>> {
    
    public boolean isDuplicateVersion(String imagePlantId, ImageVersion version);
    
    public String generateImageId(ImagePlantOS imagePlant);

    public void insertImage(ImageOS image);
    
    public void deleteImage(ImageOS image);
    
    public void deleteImages(String imagePlantId);
    
    public ImageOS selectImageById(ImageIdentity id);
    
    public ImageOS selectImageByVersion(String imagePlantId, ImageVersion version);
    
    public PaginatedResult<List<ImageOS>> selectImagesByOriginalImageId(
            String imagePlantId, String originalImageId);
    
    public PaginatedResult<List<ImageOS>> selectImagesByTemplateName(
            String imagePlantId, String templateName);
    
    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(String imagePlantId);
    
}

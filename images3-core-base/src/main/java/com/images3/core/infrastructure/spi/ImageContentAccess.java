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
package com.images3.core.infrastructure.spi;

import java.io.File;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ImageIdentity;

public interface ImageContentAccess {
    
    public boolean testBucketAccessibility(AmazonS3Bucket bucket);

    public void insertImageContent(ImageIdentity id, AmazonS3Bucket bucket, File content);
    
    public void deleteImageContent(ImageIdentity id, AmazonS3Bucket bucket);
    
    public void deleteImageContentByImagePlantId(String imagePlantId, AmazonS3Bucket bucket);
    
    public File selectImageContent(ImageIdentity id, AmazonS3Bucket bucket);
    
}

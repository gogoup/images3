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

import com.images3.common.AmazonS3Bucket;
import com.images3.common.MaximumImageSize;
import com.images3.common.ResizingConfig;

public class ImagePlantAddRequest {

    private String name;
    private AmazonS3Bucket bucket;
    private ResizingConfig resizingConfig;
    private int maximumImageSize;

    public ImagePlantAddRequest(String name, AmazonS3Bucket bucket,
            ResizingConfig resizingConfig) {
        this(name, bucket, resizingConfig, MaximumImageSize.UNLIMITED);
    }
    public ImagePlantAddRequest(String name, AmazonS3Bucket bucket,
            ResizingConfig resizingConfig, int maximumImageSize) {
        this.name = name;
        this.bucket = bucket;
        this.resizingConfig = resizingConfig;
        this.maximumImageSize = maximumImageSize;
    }

    public String getName() {
        return name;
    }

    public AmazonS3Bucket getBucket() {
        return bucket;
    }

    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }

    public int getMaximumImageSize() {
        return maximumImageSize;
    }

}

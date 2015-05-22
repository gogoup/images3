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
package com.images3.data;

import java.util.Date;

import com.images3.common.AmazonS3Bucket;

public class ImagePlantOS {

    private String id;
    private String name;
    private Date creationTime;
    private AmazonS3Bucket amazonS3Bucket;
    private String masterTemplateName;
    private long numberOfTemplates;
    private int maximumImageSize; //in bytes.
    
    public ImagePlantOS(String id, String name, Date creationTime,
            AmazonS3Bucket amazonS3Bucket, String masterTemplateName,
            long numberOfTemplates, int maximumImageSize) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.amazonS3Bucket = amazonS3Bucket;
        this.masterTemplateName = masterTemplateName;
        setNumberOfTemplates(numberOfTemplates);
        setMaximumImageSize(maximumImageSize);
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public Date getCreationTime() {
        return creationTime;
    }
    public AmazonS3Bucket getAmazonS3Bucket() {
        return amazonS3Bucket;
    }
    public void setAmazonS3Bucket(AmazonS3Bucket amazonS3Bucket) {
        this.amazonS3Bucket = amazonS3Bucket;
    }
    public String getMasterTemplateName() {
        return masterTemplateName;
    }

    public long getNumberOfTemplates() {
        return numberOfTemplates;
    }

    public void setNumberOfTemplates(long numberOfTemplates) {
        this.numberOfTemplates = numberOfTemplates;
    }
    
    public int getMaximumImageSize() {
        return maximumImageSize;
    }

    public void setMaximumImageSize(int maximumImageSize) {
        this.maximumImageSize = maximumImageSize;
    }

    @Override
    public String toString() {
        return "ImagePlantOS [id=" + id + ", name=" + name + ", creationTime="
                + creationTime + ", amazonS3Bucket=" + amazonS3Bucket
                + ", masterTemplateName=" + masterTemplateName
                + ", numberOfTemplates=" + numberOfTemplates
                + ", maximumImageSize=" + maximumImageSize + "]";
    }

    
}

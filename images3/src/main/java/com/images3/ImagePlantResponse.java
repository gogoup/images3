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

import java.util.Date;

import com.images3.common.AmazonS3Bucket;

public class ImagePlantResponse {

    private String id;
    private String name;
    private AmazonS3Bucket bucket;
    private Date creationTime;
    private TemplateResponse masterTemplate;
    private long numberOfTemplates;
    private long numberOfImages;
    
    public ImagePlantResponse(String id, String name, AmazonS3Bucket bucket,
            Date creationTime, TemplateResponse masterTemplate,
            long numberOfTemplates, long numberOfImages) {
        this.id = id;
        this.name = name;
        this.bucket = bucket;
        this.creationTime = creationTime;
        this.masterTemplate = masterTemplate;
        this.numberOfTemplates = numberOfTemplates;
        this.numberOfImages = numberOfImages;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AmazonS3Bucket getBucket() {
        return bucket;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public TemplateResponse getMasterTemplate() {
        return masterTemplate;
    }

    public long getNumberOfTemplates() {
        return numberOfTemplates;
    }

    public long getNumberOfImages() {
        return numberOfImages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
        result = prime * result
                + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((masterTemplate == null) ? 0 : masterTemplate.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + (int) (numberOfImages ^ (numberOfImages >>> 32));
        result = prime * result
                + (int) (numberOfTemplates ^ (numberOfTemplates >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImagePlantResponse other = (ImagePlantResponse) obj;
        if (bucket == null) {
            if (other.bucket != null)
                return false;
        } else if (!bucket.equals(other.bucket))
            return false;
        if (creationTime == null) {
            if (other.creationTime != null)
                return false;
        } else if (!creationTime.equals(other.creationTime))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (masterTemplate == null) {
            if (other.masterTemplate != null)
                return false;
        } else if (!masterTemplate.equals(other.masterTemplate))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (numberOfImages != other.numberOfImages)
            return false;
        if (numberOfTemplates != other.numberOfTemplates)
            return false;
        return true;
    }

}

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
package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.common.AmazonS3Bucket;

public class ImagePlantOS {

    private String id;
    private String name;
    private Date creationTime;
    private AmazonS3Bucket amazonS3Bucket;
    private String masterTemplateName;
    private long numberOfTemplates;
    
    public ImagePlantOS(String id, String name, Date creationTime,
            AmazonS3Bucket amazonS3Bucket, String masterTemplateName,
            long numberOfTemplates) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.amazonS3Bucket = amazonS3Bucket;
        this.masterTemplateName = masterTemplateName;
        setNumberOfTemplates(numberOfTemplates);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((amazonS3Bucket == null) ? 0 : amazonS3Bucket.hashCode());
        result = prime * result
                + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime
                * result
                + ((masterTemplateName == null) ? 0 : masterTemplateName
                        .hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        ImagePlantOS other = (ImagePlantOS) obj;
        if (amazonS3Bucket == null) {
            if (other.amazonS3Bucket != null)
                return false;
        } else if (!amazonS3Bucket.equals(other.amazonS3Bucket))
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
        if (masterTemplateName == null) {
            if (other.masterTemplateName != null)
                return false;
        } else if (!masterTemplateName.equals(other.masterTemplateName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (numberOfTemplates != other.numberOfTemplates)
            return false;
        return true;
    }
    
    
}

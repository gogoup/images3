package com.images3;

public class ImageMetadata {

    private ImageDimension dimension;
    private ImageFormat format;
    private long size;
    
    public ImageMetadata() {}
    
    public ImageMetadata(ImageDimension dimension, ImageFormat format, long size) {
        this.dimension = dimension;
        this.format = format;
        this.size = size;
    }

    public ImageDimension getDimension() {
        return dimension;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public long getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((dimension == null) ? 0 : dimension.hashCode());
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));
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
        ImageMetadata other = (ImageMetadata) obj;
        if (dimension == null) {
            if (other.dimension != null)
                return false;
        } else if (!dimension.equals(other.dimension))
            return false;
        if (format != other.format)
            return false;
        if (size != other.size)
            return false;
        return true;
    }

    
}

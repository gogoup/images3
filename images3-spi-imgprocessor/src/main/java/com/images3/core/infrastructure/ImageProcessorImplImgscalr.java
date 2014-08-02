package com.images3.core.infrastructure;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.imgscalr.Scalr;

import com.images3.ImageDimension;
import com.images3.ImageFormat;
import com.images3.ImageMetadata;
import com.images3.ResizingConfig;
import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageProcessorImplImgscalr implements ImageProcessor {
    
    private static final byte[] JPG_MAGIC_NUMBER = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0};
    private static final byte[] PNG_MAGIC_NUMBER = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47};
    private static final byte[] GIF_MAGIC_NUMBER = new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38};
    private static final byte[] BMP_MAGIC_NUMBER = new byte[]{(byte) 0x42, (byte) 0x4D};
    
    private final String tempDir;
    
    public ImageProcessorImplImgscalr(String tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    public boolean isSupportedFormat(File imageFile) {
        boolean result = true;
        InputStream imageInputStream = null;
        try {
            imageInputStream = Files.newInputStream(imageFile.toPath());
            byte[] magicBytes = new byte[4];
            imageInputStream.read(magicBytes);
            if (!isJPEG(magicBytes)
                    && !isPNG(magicBytes)
                    && !isGIF(magicBytes)
                    && !isBMP(magicBytes)) {
                result = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    public ImageMetadata readImageMetadata(File imageFile) {
        ImageMetadata metadata = null;
        ImageInputStream imageInputStream = null;
        ImageReader imageReader = null;
        try {
            imageInputStream = ImageIO.createImageInputStream(imageFile);
            imageReader = getImageReader(imageFile, imageInputStream);
            metadata = createImageMetadata(imageFile, imageReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (null != imageReader) {
                imageReader.dispose();
            }
        }
        return metadata;
    }
    
    private ImageReader getImageReader(File imageFile, ImageInputStream imageInputStream) {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
        if (!imageReaders.hasNext()) {
            throw new IllegalArgumentException(imageFile.getAbsolutePath());
        }
        ImageReader imageReader = imageReaders.next();
        imageReader.setInput(imageInputStream);
        return imageReader;
    }
    
    private ImageMetadata createImageMetadata(File imageFile, ImageReader imageReader) throws IOException {
        ImageDimension dimension = new ImageDimension(imageReader.getWidth(0), imageReader.getHeight(0));
        ImageFormat format = ImageFormat.valueOf(imageReader.getFormatName().toUpperCase());
        return new ImageMetadata(dimension, format, imageFile.length());
    }

    public File resizeImage(String imageId, ImageOS image, File imageFile, ResizingConfig resizingConfig) {
        File resizedImageFile = null;
        try {
            BufferedImage originalImage = ImageIO.read(
                    new BufferedInputStream(Files.newInputStream(imageFile.toPath())));
            BufferedImage resizedImage = resizeImage(originalImage, resizingConfig);
            resizedImageFile = prepareImageFile(tempDir + File.separator + imageId);
            ImageIO.write(
                    resizedImage,
                    image.getMetadata().getFormat().toString(), 
                    new BufferedOutputStream(Files.newOutputStream(resizedImageFile.toPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resizedImageFile;
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, ResizingConfig resizingConfig) {
        BufferedImage resizedImage = null;
        if (resizingConfig.isKeepProportions()) {
            resizedImage = Scalr.resize(
                    originalImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, 
                    resizingConfig.getWidth(), resizingConfig.getHeight(), Scalr.OP_ANTIALIAS);
        } else {
            resizedImage = Scalr.resize(
                    originalImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, 
                    resizingConfig.getWidth(), resizingConfig.getHeight(), Scalr.OP_ANTIALIAS);
        }
        return resizedImage;
    }
    
    private File prepareImageFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            throw new IllegalArgumentException("Image file, " + file.getName() + " has already exist!");
        } else {
            file.createNewFile();
        }
        return file;
    }
    
    private static boolean isJPEG(byte[] content) {
        if(content.length < 4) return false;
        return (content[0] == JPG_MAGIC_NUMBER[0]
                        && content[1] == JPG_MAGIC_NUMBER[1]
                        && content[2] == JPG_MAGIC_NUMBER[2]
                        && content[3] == JPG_MAGIC_NUMBER[3]);
    }

    private static boolean isPNG(byte[] content) {
            if(content.length < 4) return false;
            return (content[0] == PNG_MAGIC_NUMBER[0]
                            && content[1] == PNG_MAGIC_NUMBER[1]
                            && content[2] == PNG_MAGIC_NUMBER[2]
                            && content[3] == PNG_MAGIC_NUMBER[3]);
    }
    
    private static boolean isGIF(byte[] content) {
            if(content.length < 4) return false;
            return (content[0] == GIF_MAGIC_NUMBER[0]
                            && content[1] == GIF_MAGIC_NUMBER[1]
                            && content[2] == GIF_MAGIC_NUMBER[2]
                            && content[3] == GIF_MAGIC_NUMBER[3]);
    }
    
    private static boolean isBMP(byte[] content) {
            if(content.length < 2) return false;
            return (content[0] == BMP_MAGIC_NUMBER[0]
                            && content[1] == BMP_MAGIC_NUMBER[1]);
    }

}

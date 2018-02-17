package com.dexmohq.imadex.image;

import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
public class ImageFormatValidator {

    public enum ValidImageFormat {
        JPEG("jpg"), PNG("png");
        private String extension;

        ValidImageFormat(String extension) {
            this.extension = extension;
        }

        static boolean isSupported(String format) {
            for (ValidImageFormat validImageFormat : values()) {
                if (validImageFormat.name().equalsIgnoreCase(format)) {
                    return true;
                }
            }
            return false;
        }

        static boolean isExtensionSupported(String extension) {
            for (ValidImageFormat validImageFormat : values()) {
                if (validImageFormat.extension.equals(extension)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void validateFormat(MultipartFile file) throws IOException, FileCorruptedException, UnsupportedFormatException {
        final ImageInputStream imageInputStream = ImageIO.createImageInputStream(file.getInputStream());
        final Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
        if (!readers.hasNext()) {
            imageInputStream.close();
            final String extension = Files.getFileExtension(file.getOriginalFilename());
            if (ValidImageFormat.isExtensionSupported(extension)) {
                throw new FileCorruptedException();
            }
            throw new UnsupportedFormatException();
        }
        do {
            final ImageReader next = readers.next();
            if (ValidImageFormat.isSupported(next.getFormatName())) {
                return;//format is valid
            }
        } while (readers.hasNext());
        imageInputStream.close();
        throw new UnsupportedFormatException();
    }

}

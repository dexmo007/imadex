package com.dexmohq.imadex.util;

import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@UtilityClass
public class Images {

    public static void validate(InputStream in) throws IOException {
        final ImageInputStream imageInputStream = ImageIO.createImageInputStream(in);
        final Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
        if (!readers.hasNext()) {
            imageInputStream.close();
            throw new IllegalStateException("File corrupted or format unsupported");
        }
        do {
            final ImageReader next = readers.next();
            System.out.println(next.getFormatName() + " (" + next.getClass() + ")");
        } while (readers.hasNext());
        imageInputStream.close();
    }

}

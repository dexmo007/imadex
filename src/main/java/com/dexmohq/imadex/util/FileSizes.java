package com.dexmohq.imadex.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class FileSizes {
    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB"};

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))
                + " " + UNITS[digitGroups];
    }

}

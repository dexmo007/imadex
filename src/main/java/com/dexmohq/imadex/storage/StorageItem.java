package com.dexmohq.imadex.storage;

import com.dexmohq.imadex.util.FileSizes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StorageItem {

    private final String filename;

    @JsonIgnore
    private final long bytes;

    @JsonProperty
    public String getFileSize() {
        return FileSizes.readableFileSize(bytes);
    }

}

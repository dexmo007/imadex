package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.data.TagDocument;
import com.dexmohq.imadex.storage.StorageItem;
import com.dexmohq.imadex.tag.Tag;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
public class TaggedStorageItem extends StorageItem {

    private final Set<TagDocument> tags;

    public TaggedStorageItem(StorageItem storageItem, Set<TagDocument> tags) {
        super(storageItem.getFilename(), storageItem.getBytes());
        this.tags = tags;
    }
}

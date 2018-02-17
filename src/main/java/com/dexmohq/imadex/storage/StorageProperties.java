package com.dexmohq.imadex.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "imadex.storage")
@Getter
@Setter
public class StorageProperties {

    private Path path;

}

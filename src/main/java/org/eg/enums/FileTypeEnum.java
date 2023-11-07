package org.eg.enums;

import lombok.Getter;

@Getter
public enum FileTypeEnum {
    Music("music"),
    Video("video");

    private final String type;
    FileTypeEnum(String type) {
        this.type = type;
    }

}

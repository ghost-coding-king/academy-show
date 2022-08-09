package project.academyshow.service;

import lombok.Getter;

@Getter
public enum FilePath {
    FROM_POST("/Users/woo/upload-test"), FROM_ACADEMY("/경로2"), FROM_TUTOR("/경로3");

    private final String path;
    FilePath(String path) {
        this.path = path;
    }
}

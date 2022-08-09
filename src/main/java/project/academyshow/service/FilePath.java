package project.academyshow.service;

import lombok.Getter;

@Getter
public enum FilePath {
    FROM_POST("/진짜경로1"), FROM_ACADEMY("/진짜경로2"), FROM_TUTOR("/진짜경로3");

    private final String path;
    FilePath(String path) {
        this.path = path;
    }
}

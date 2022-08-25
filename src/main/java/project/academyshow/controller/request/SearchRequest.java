package project.academyshow.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {

    private String searchType;
    private String q;
    private List<String> educations;
    private List<String> subjects;
    private String area;
    private String order;

    private int offset;
    private int limit;
}

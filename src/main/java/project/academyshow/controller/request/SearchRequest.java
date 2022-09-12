package project.academyshow.controller.request;

import lombok.Data;
import project.academyshow.entity.SearchType;

import java.util.List;

@Data
public class SearchRequest {

    private SearchType searchType;
    private String q;
    private List<String> educations;
    private List<String> subjects;
    private String area;
    private String order;

    public String getEducationRegexp() {
        return String.join("|", educations);
    }

    public String getSubjectRegexp() {
        return String.join("|", subjects);
    }
}

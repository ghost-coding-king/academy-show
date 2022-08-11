package project.academyshow.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private int code;
    private int count;
    private String msg;
    private T data;

    public ApiResponse(int code, String msg, T data) {
        if (data != null)
            this.count = data instanceof List ? ((List<?>) data).size() : 1;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(200, "success", data);
    }

}

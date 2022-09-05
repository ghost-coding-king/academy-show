package project.academyshow.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReferenceUpStatistics {

    private enum STATUS  {
        NOT_LOGIN, YES, NO
    }

    private Long count;
    private STATUS status;    /** 좋아요 상태 */

    public static ReferenceUpStatistics of(long count, boolean isLike) {
        return new ReferenceUpStatistics(count, isLike ? STATUS.YES:STATUS.NO);
    }

    public static ReferenceUpStatistics notAuthenticatedOf(long count) {
        return new ReferenceUpStatistics(count, STATUS.NOT_LOGIN);
    }
}

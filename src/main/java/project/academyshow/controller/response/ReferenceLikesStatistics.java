package project.academyshow.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReferenceLikesStatistics {

    private enum STATUS  {
        NOT_LOGIN, YES, NO
    }

    private Long count;
    private STATUS status;    /** 좋아요 상태 */

    public static ReferenceLikesStatistics of(long count, boolean isLike) {
        return new ReferenceLikesStatistics(count, isLike ? STATUS.YES:STATUS.NO);
    }

    public static ReferenceLikesStatistics notAuthenticatedOf(long count) {
        return new ReferenceLikesStatistics(count, STATUS.NOT_LOGIN);
    }
}

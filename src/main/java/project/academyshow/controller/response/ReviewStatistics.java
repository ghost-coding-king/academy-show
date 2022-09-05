package project.academyshow.controller.response;

import lombok.Data;

import javax.persistence.Tuple;
import java.util.List;

@Data
public class ReviewStatistics {

    /** 0 번 인덱스 부터, 별점 각 1 ~ 5 점의 개수 */
    private long[] count = new long[5];
    private long totalReviews;
    private double averageStar;

    public ReviewStatistics(List<Tuple> countQueryResult) {
        /* idx 0: 개수
        *  idx 1: rating */
        for (Tuple tuple : countQueryResult) {
            Long count = (Long)tuple.get(0);
            Integer rating = (Integer)tuple.get(1);
            this.count[rating - 1] = count;
        }

        for (int i=0; i<count.length; i++) {
            totalReviews += count[i];
            averageStar += (i + 1) * count[i];
        }

        if (totalReviews > 0)
            averageStar /= totalReviews;
    }
}

package project.academyshow.controller.response;

import lombok.Data;

import javax.persistence.Tuple;
import java.util.List;

@Data
public class ReviewStatistics {

    private long[] count = new long[5];

    public ReviewStatistics(List<Tuple> countQueryResult) {
        /* idx 0: 개수
        *  idx 1: rating */
        for (Tuple tuple : countQueryResult) {
            Long count = (Long)tuple.get(0);
            Integer rating = (Integer)tuple.get(1);
            this.count[rating - 1] = count;
        }
    }
}

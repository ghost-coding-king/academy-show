package project.academyshow.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.academyshow.entity.BatchLikes;

import javax.persistence.EntityManagerFactory;

/**
 * 참고자료
 * https://khj93.tistory.com/entry/Spring-Batch%EB%9E%80-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 *
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchLikesConfig {
    
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    /**
     * batchLike 들의 Count를 초기화한후
     * batchLike 들의 Count 다시 센다.
     */
    @Bean
    public Job BatchLikesJob() throws Exception {

        Job exampleJob = jobBuilderFactory.get("batchLikesJob")
                .start(batchLikeCountInitStep())
                .next(batchLikeCountStep())
                .build();

        return exampleJob;
    }

    /**
     * batchLikeCountInitStep
     * batchLike 를 천개씩 가지고 와서 0으로 초기화시킨다.
     */
    @Bean
    @JobScope
    public Step batchLikeCountInitStep() throws Exception {
        return stepBuilderFactory.get("Step")
                .startLimit(2)
                .<BatchLikes, BatchLikes>chunk(1000)
                .reader(readerForInit())
                .processor(processorForInit())
                .writer(writer())
                .build();
    }


    @Bean
    @JobScope
    public Step batchLikeCountStep() throws Exception {
        return stepBuilderFactory.get("Step")
                .startLimit(2)
                .<BatchLikes, BatchLikes>chunk(1000)
                .reader(readerForCount())
                .processor(processorForCount())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<BatchLikes> readerForInit() throws Exception {
        return new JpaPagingItemReaderBuilder<BatchLikes>()
                .pageSize(1000)
                .queryString("select b FROM BatchLikes b ORDER BY b.id ASC")
                .entityManagerFactory(entityManagerFactory)
                .name("batchLikeInitializer")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<BatchLikes, BatchLikes> processorForInit(){
        return new ItemProcessor<BatchLikes, BatchLikes>() {
            @Override
            public BatchLikes process(BatchLikes b) throws Exception {
                b.resetCountForBatch();
                return b;
            }
        };
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<BatchLikes> readerForCount() throws Exception {
        return new JpaPagingItemReaderBuilder<BatchLikes>()
                .pageSize(1000)
                .queryString("select l.batchLikes FROM Likes l ORDER BY l.id ASC")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<BatchLikes, BatchLikes> processorForCount(){
        return new ItemProcessor<BatchLikes, BatchLikes>() {
            @Override
            public BatchLikes process(BatchLikes b) throws Exception {
                b.incrementCountForBatch();
                return b;
            }
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<BatchLikes> writer(){
        return new JpaItemWriterBuilder<BatchLikes>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}

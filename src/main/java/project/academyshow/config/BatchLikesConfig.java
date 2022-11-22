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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchLikesConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job BatchLikesJob() throws Exception {

        Job exampleJob = jobBuilderFactory.get("batchLikesJob")
                .start(batchLikeCountInitStep())
                .next(batchLikeCountStep())
                .build();

        return exampleJob;
    }

    @Bean
    @JobScope
    public Step batchLikeCountInitStep() throws Exception {
        return stepBuilderFactory.get("Step")
                .<BatchLikes, BatchLikes>chunk(100)
                .reader(readerForInit())
                .processor(processorForInit())
                .writer(writer())
                .build();
    }


    @Bean
    @JobScope
    public Step batchLikeCountStep() throws Exception {
        return stepBuilderFactory.get("Step")
                .<BatchLikes, BatchLikes>chunk(100)
                .reader(readerForCount())
                .processor(processorForCount())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<BatchLikes> readerForInit() throws Exception {
        return new JpaPagingItemReaderBuilder<BatchLikes>()
                .pageSize(100)
                .queryString("select b FROM BatchLikes b ORDER BY b.id ASC")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
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
                .pageSize(100)
                .queryString("select b FROM Likes l, BatchLikes b where l.type=b.referenceType and l.referenceId=b.referenceId ORDER BY l.id ASC")
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

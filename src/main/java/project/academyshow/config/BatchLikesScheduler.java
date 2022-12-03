package project.academyshow.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchLikesScheduler {

    private final JobLauncher jobLauncher;

    private final BatchLikesConfig batchLikesConfig;

    @Scheduled(fixedDelay = 1000*3600*24)
    public void runJob() {
        log.info("좋아요 배치작업 시작");
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(confMap);

        try {
            jobLauncher.run(batchLikesConfig.BatchLikesJob(), parameters);
            log.info("좋아요 배치작업 성공");
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("좋아요 배치작업 실패!");
        }
    }
}

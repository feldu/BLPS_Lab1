package blps.labs.sсheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReviewMessagingJob implements Job {
    //    @Autowired
//    private ReviewMessagingJobService jobService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        jobService.executeSampleJob();
        log.info("It's time to blow job!");
    }
}

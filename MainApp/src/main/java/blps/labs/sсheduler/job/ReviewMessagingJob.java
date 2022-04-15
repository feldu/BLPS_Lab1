package blps.labs.sсheduler.job;

import blps.labs.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReviewMessagingJob implements Job {
    /*
        Don't use constructor injection, default constructor required.
     */
    @Autowired
    private MessageService messageService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("It's time to send spam!");
        messageService.sendMessageWithTheNewestReviews();
    }
}

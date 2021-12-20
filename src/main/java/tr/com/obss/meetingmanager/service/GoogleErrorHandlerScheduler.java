package tr.com.obss.meetingmanager.service;


import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tr.com.obss.meetingmanager.feigns.GoogleCalendarServiceClient;
import tr.com.obss.meetingmanager.repository.GoogleErrorRepository;


@Service
@RequiredArgsConstructor
public class GoogleErrorHandlerScheduler {

    private final GoogleCalendarServiceClient googleCalendarServiceClient;
    private final GoogleErrorRepository repository;
    @Scheduled(fixedRate = 2000)
    @SchedulerLock(
            name = "scheduleDfConnection",
            lockAtMostFor = "${shedlock.df-connection.resend.lock-at-most}",
            lockAtLeastFor = "${shedlock.df-connection.resend.lock-at-least}")
    public void startConnectionCheckerScheduler() {
    }
}

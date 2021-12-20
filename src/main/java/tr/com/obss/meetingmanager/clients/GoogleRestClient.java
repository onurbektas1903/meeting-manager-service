package tr.com.obss.meetingmanager.clients;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;

@Component
@RequiredArgsConstructor
public class GoogleRestClient {
    private final RestTemplate restTemplate;
    private String googleUrl ="http://google-calendar-service";
    @Retry(name = "googleEvent")
    public ResponseEntity<CalendarEventDTO> scheduleEvent( CalendarEventDTO calendarEvent) {
        String apiUrl = googleUrl+"/google-event-manager/event";
        HttpEntity<CalendarEventDTO> httpEntity = new HttpEntity<>(calendarEvent);
        try{
            ResponseEntity<CalendarEventDTO> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, CalendarEventDTO.class);
            return responseEntity;

        }catch (Exception e){
      System.out.println("afdsfsa");
      return null;
        }
    }
}

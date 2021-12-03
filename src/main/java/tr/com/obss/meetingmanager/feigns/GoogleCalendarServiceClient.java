package tr.com.obss.meetingmanager.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleMailDTO;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(value = "google-calendar-service")
public interface GoogleCalendarServiceClient {
    @PostMapping("/google/calendar/schedule-meeting")
    CalendarEventDTO scheduleMeeting(@RequestBody CalendarEventDTO calendarEvent);

    @PostMapping(
            path = "/google/calendar/create-account",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    CalendarEventDTO createAccount(@PathVariable(name = "file") MultipartFile file);

    @PostMapping("/google/calendar/create-change-slot-email")
     void changeSlot(@RequestBody GoogleMailDTO mailDTO);

    @PostMapping("/google/calendar/update-meeting")
    @ResponseBody
    @ResponseStatus(OK)
    CalendarEventDTO updateMeeting(@RequestBody CalendarEventDTO calendarEvent);
}

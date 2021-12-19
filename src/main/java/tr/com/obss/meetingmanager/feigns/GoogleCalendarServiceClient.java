package tr.com.obss.meetingmanager.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.google.CalendarEventDTO;
import tr.com.obss.meetingmanager.dto.google.DeleteEventDTO;
import tr.com.obss.meetingmanager.dto.google.GoogleMailDTO;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(value = "google-calendar-service")
public interface GoogleCalendarServiceClient {
    @PostMapping("/google-event-manager/event")
    CalendarEventDTO scheduleEvent(@RequestBody CalendarEventDTO calendarEvent);

    @PostMapping(
            path = "/google-event-manager/account",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    CalendarEventDTO createAccount(@RequestPart MultipartFile accountFile, @RequestPart String accountMail);

    @PutMapping(
            path = "/google-event-manager/account/{accountName}",
            consumes = MULTIPART_FORM_DATA_VALUE
     )
    void updateAccount(@RequestPart MultipartFile file, @PathVariable String accountName);

    @DeleteMapping("/google-event-manager/account/{accountName}")
    @ResponseBody
    @ResponseStatus(OK)
    void deleteAccount(@PathVariable String accountName);

    @PostMapping("/google-event-manager/google-mail")
     void changeSlot(@RequestBody GoogleMailDTO mailDTO);

    @PostMapping("/google-event-manager/update-event")
    @ResponseBody
    @ResponseStatus(OK)
    CalendarEventDTO updateEvent(@RequestBody CalendarEventDTO calendarEvent);

    @PostMapping("/google-event-manager/delete-event}")
    @ResponseBody
    @ResponseStatus(OK)
     String deleteEvent(@RequestBody DeleteEventDTO deleteEventDTO);
}

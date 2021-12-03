package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.factory.MeetProviderHandlerFactory;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import tr.com.obss.meetingmanager.service.google.GoogleMeetProviderService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meeting-provider")
@Validated
public class MeetingProviderController {
  private final MeetProviderHandlerFactory handlerFactory;
  private final ProviderManagerService providerManagerService;
  private final GoogleMeetProviderService googleProviderService;
  @PostMapping("/create")
  @ResponseBody
  public MeetingProviderDTO createProvider(@Valid @RequestBody MeetingProviderDTO meetingProvider) {

    return handlerFactory
        .findStrategy(meetingProvider.getMeetingProviderType())
        .createMeetingProvider(meetingProvider);
  }

  @ResponseBody
  @RequestMapping(value = "/create-with-files", method = RequestMethod.POST, consumes = { "multipart/form-data" })
  public MeetingProviderDTO upload(@RequestPart("meetingProviderDTO") MeetingProviderDTO meetingProviderDTO,
                     @RequestPart("file") MultipartFile file){
    return googleProviderService.createMeetingProvider(meetingProviderDTO,file);
  }

  @PostMapping("/update")
  @ResponseBody
  public MeetingProviderDTO updateMeeting(@Valid @RequestBody MeetingProviderDTO meetingDTO) {

    return handlerFactory
        .findStrategy(meetingDTO.getMeetingProviderType())
        .updateMeetingProvider(meetingDTO);
  }
//TODO endpointi düzelt
  @GetMapping("/")
  @ResponseBody
  public List<MeetingProviderDTO> listProviders() {
    System.out.println("received");
    return providerManagerService.listMeetingProviders();
  }
}

package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.factory.MeetProviderHandlerFactory;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import tr.com.obss.meetingmanager.service.google.GoogleProviderService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/provider-manager")
@Validated
public class MeetingProviderController {
  private final MeetProviderHandlerFactory handlerFactory;
  private final ProviderManagerService providerManagerService;
  private final GoogleProviderService googleProviderService;
  @PostMapping("/provider")
  @ResponseBody
  public MeetingProviderDTO createProvider(@Valid @RequestBody MeetingProviderDTO meetingProvider) {

    return handlerFactory
        .findStrategy(meetingProvider.getMeetingProviderType())
        .createMeetingProvider(meetingProvider);
  }

  @PutMapping("/provider/{id}")
  @ResponseBody
  public MeetingProviderDTO updateProvider(@Valid @RequestBody MeetingProviderDTO meetingDTO, @PathVariable String id) {

    return handlerFactory
        .findStrategy(meetingDTO.getMeetingProviderType())
        .updateMeetingProvider(meetingDTO,id);
  }
  @GetMapping("/provider/{id}")
  @ResponseBody
  public MeetingProviderDTO getProviderById(@PathVariable String id){
    return providerManagerService.findById(id);
  }

  @GetMapping("providers")
  @ResponseBody
  public List<MeetingProviderDTO> listProviders() {
    System.out.println("received");
    return providerManagerService.listMeetingProviders();
  }


}

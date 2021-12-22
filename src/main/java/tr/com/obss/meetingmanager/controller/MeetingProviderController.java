package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.service.ProviderManagerService;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/provider-manager")
@Validated
public class MeetingProviderController {
  private final ProviderManagerService providerManagerService;

  @PostMapping("/provider")
  @ResponseBody
  public MeetingProviderDTO createProvider(@Valid @RequestBody MeetingProviderDTO meetingProvider) {

    return providerManagerService.saveMeetingProvider(meetingProvider);
  }

  @PutMapping("/provider/{id}")
  @ResponseBody
  public MeetingProviderDTO updateProvider(@Valid @RequestBody MeetingProviderDTO meetingProviderDTO, @PathVariable String id) {

    return providerManagerService.updateMeetingProvider(meetingProviderDTO,id);
  }

  @DeleteMapping("/provider/{id}")
  @ResponseBody
  @ResponseStatus(OK)
  public void  deleteProvider(@PathVariable String id) {
    providerManagerService.deleteMeetingProvider(id);
  }

  @GetMapping("/provider/{id}")
  @ResponseBody
  public MeetingProviderDTO getProviderById(@PathVariable String id){
    return providerManagerService.findById(id);
  }

  @GetMapping("providers")
  @ResponseBody
  public List<MeetingProviderDTO> listProviders() {
    return providerManagerService.getMeetingProviders();
  }

  @PostMapping("active-providers")
  @ResponseBody
  public List<MeetingProviderDTO> listActiveProviders(@RequestBody Set<String> roleGroups) {

    return providerManagerService.getActiveProviders(roleGroups);
  }

  @PutMapping("/provider/active-passive/{id}/{isActive}")
  @ResponseBody
  public MeetingProviderDTO handleProviderActiveStatus(
          @PathVariable String id, @PathVariable boolean isActive) {
    return providerManagerService.activateDeactivateProvider(id,isActive);
  }

}

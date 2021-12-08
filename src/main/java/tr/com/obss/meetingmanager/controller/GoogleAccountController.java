package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;
import tr.com.obss.meetingmanager.service.google.GoogleAccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google-account-manager")
@Validated
public class GoogleAccountController {
  private final GoogleAccountService googleAccountService;

  @ResponseBody
  @RequestMapping(value = "/account", method = RequestMethod.POST, consumes = { "multipart/form-data" })
  public GoogleAccountDTO createGoogleAccount(@Valid @RequestPart("googleAccountDTO") GoogleAccountDTO googleAccount,
                     @RequestPart("file") MultipartFile file){
    return googleAccountService.createGoogleAccount(googleAccount,file);
  }
  @GetMapping("/accounts")
  @ResponseBody
  public List<GoogleAccountDTO> getGoogleAccounts() {
    return googleAccountService.getAll();
  }
  @GetMapping("/active-account")
  @ResponseBody
  public GoogleAccountDTO getActiveGoogleAccount() {
    return googleAccountService.findActiveAccount();
  }
  @GetMapping("/account/{id}")
  @ResponseBody
  public GoogleAccountDTO findById(@PathVariable String id) {
    return googleAccountService.findById(id);
  }
  @PutMapping("/account/{id}")
  @ResponseBody
  public GoogleAccountDTO updateGoogleAccount(@RequestBody GoogleAccountDTO googleAccount,
                                                  @PathVariable String id) {
    //TODO implement here
    return null;
  }

  @PutMapping("/account/active-passive/{id}/{isActive}")
  @ResponseBody
  public GoogleAccountDTO makeAccountPassive(@PathVariable String id,@PathVariable boolean isActive) {
    //TODO implement here
    return null;
  }
}

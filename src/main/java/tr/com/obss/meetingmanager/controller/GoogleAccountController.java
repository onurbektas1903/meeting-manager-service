package tr.com.obss.meetingmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tr.com.obss.meetingmanager.service.google.GoogleAccountService;
import tr.com.obss.meetingmanager.dto.google.GoogleAccountDTO;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google-account-manager")
@Validated
public class GoogleAccountController {
  private final GoogleAccountService googleAccountService;

  @ResponseBody
  @RequestMapping(
      value = "/account",
      method = RequestMethod.POST,
      consumes = {"multipart/form-data"})
  public GoogleAccountDTO createGoogleAccount(
      @Valid @RequestPart("googleAccountDTO") GoogleAccountDTO googleAccount,
      @RequestPart("file") MultipartFile file) {
    return googleAccountService.createGoogleAccount(googleAccount, file);
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

  @DeleteMapping("/account/{id}")
  @ResponseBody
  @ResponseStatus(OK)
  public void  deleteAccount(@PathVariable String id) {
     googleAccountService.deleteAccount(id);
  }

  @PutMapping("/account/{id}")
  @ResponseBody
  public GoogleAccountDTO updateGoogleAccount(
          @Valid @RequestPart("googleAccountDTO") GoogleAccountDTO googleAccount,
          @RequestPart("file") MultipartFile file,@PathVariable String id) {
     return googleAccountService.updateGoogleAccount(googleAccount,file,id);
  }


}

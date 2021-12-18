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
import tr.com.obss.meetingmanager.dto.zoom.ZoomAccountDTO;
import tr.com.obss.meetingmanager.service.zoom.ZoomAccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zoom-account-manager")
@Validated
public class ZoomAccountController {
  private final ZoomAccountService zoomAccountService;
  @PostMapping("/account")
  @ResponseBody
  public ZoomAccountDTO createZoomAccount(@Valid @RequestBody ZoomAccountDTO zoomAccount) {

    return zoomAccountService.createZoomAccount(zoomAccount);
  }

  @GetMapping("/account/{id}")
  @ResponseBody
  public ZoomAccountDTO findById(@PathVariable String id) {
    return zoomAccountService.findById(id);
  }

  @GetMapping("/accounts")
  @ResponseBody
  public List<ZoomAccountDTO> getZoomAccounts() {
    return zoomAccountService.getAll();
  }

  @GetMapping("/free-accounts/{providerId}")
  @ResponseBody
  public List<ZoomAccountDTO> getFreeZoomAccounts(@PathVariable String providerId) {
    return zoomAccountService.getFreeZoomAccounts(providerId);
  }

}

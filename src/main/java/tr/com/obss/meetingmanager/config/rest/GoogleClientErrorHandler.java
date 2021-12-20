package tr.com.obss.meetingmanager.config.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import tr.com.common.exceptions.ErrorMessage;
import tr.com.common.exceptions.NotFoundException;

import java.io.IOException;

@Slf4j
public class GoogleClientErrorHandler implements ResponseErrorHandler {
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    System.out.println("boham");
    return clientHttpResponse.getStatusCode().is4xxClientError() || clientHttpResponse.getStatusCode().is5xxServerError();
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    log.error(
        "Zoom Client Error | HTTP Status Code: " + clientHttpResponse.getStatusCode().value());
      ErrorMessage errorMessage = mapper.readValue(clientHttpResponse.getBody(), ErrorMessage.class);
      if(clientHttpResponse.getStatusCode().value() == 500){
          throw new NotFoundException("Account");
      }else{
          throw new NotFoundException("Acount");
      }
  }
}

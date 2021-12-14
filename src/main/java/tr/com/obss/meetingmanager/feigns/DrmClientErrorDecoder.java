package tr.com.obss.meetingmanager.feigns;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import tr.com.common.exceptions.ErrorMessage;
import tr.com.common.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
@Slf4j
public class DrmClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        ErrorMessage message = null;
        System.out.println("Error Response!!!");
        try( Reader reader =response.body().asReader(StandardCharsets.UTF_8)) {
            String result = CharStreams.toString(reader);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
             message = mapper.readValue(result,
                    ErrorMessage.class);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (500 == response.status()) {
            throw new ServiceNotAvailableException("Google Server Unreachable");
        }

        return defaultErrorDecoder.decode(s, response);
    }
}
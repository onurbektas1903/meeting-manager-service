package tr.com.obss.meetingmanager.resilliance4j.predicates;

import org.springframework.http.ResponseEntity;
import tr.com.obss.meetingmanager.exception.ErrorMessage;

import java.util.function.Predicate;

public class ConditionalRetryPredicate implements Predicate<ResponseEntity<ErrorMessage>> {
    @Override
    public boolean test(ResponseEntity<ErrorMessage> responseEntity) {
        ErrorMessage errorMessage = responseEntity.getBody();
        if (errorMessage != null) {
            System.out.println("Search returned error code = " + errorMessage.getErrorCode());
            return errorMessage.getErrorCode() == 404;
        }
        return true;
    }
}
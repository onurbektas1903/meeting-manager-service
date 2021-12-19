package tr.com.obss.meetingmanager.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import tr.com.common.exceptions.BaseException;
import tr.com.common.exceptions.ErrorMessage;

@Data
public class ProviderException extends BaseException {
    private static final long serialVersionUID = 23405878576840153L;
    private HttpStatus httpStatus;

    public ProviderException(ErrorMessage errorMessage,HttpStatus httpStatus){
        super(errorMessage.getMessage(),errorMessage.getErrorList(),errorMessage.getDetailedErrors(),
                errorMessage.getErrorCode());
        this.httpStatus = httpStatus;
    }
}

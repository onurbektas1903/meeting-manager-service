package tr.com.obss.meetingmanager.exception;

import java.util.Map;
import java.util.Set;

public class BusinessValidationException extends BaseException{
    private static final int code = 1005;
    private static final long serialVersionUID = 23405878576840153L;

    public BusinessValidationException(String message) {
        super(message, code);
    }
    public BusinessValidationException(String message, Set<String> errList) {
        super(message,errList, code);
    }

    public BusinessValidationException(String message, Map<String ,String> errMap) {
        super(message, errMap, code);
    }
}

package tr.com.obss.meetingmanager.exception;

import java.util.Map;

public class BusinessValidationException extends BaseException{
    private static final int code = 1005;
    private static final long serialVersionUID = 23405878576840153L;

    public BusinessValidationException(String message) {
        super(message, code);
    }

    public BusinessValidationException(String message, Map<String ,String> errList) {
        super(message, errList, code);
    }
}

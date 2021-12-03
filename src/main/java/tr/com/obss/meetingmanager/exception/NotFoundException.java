package tr.com.obss.meetingmanager.exception;

import java.util.Set;

public class NotFoundException extends BaseException{
    private static final int code = 1002;
    private static final long serialVersionUID = 23405878576840153L;

    public NotFoundException(String message) {
        super(message, code);
    }

    public NotFoundException(String message, Set<String> errList) {
        super(message, errList, code);
    }
}

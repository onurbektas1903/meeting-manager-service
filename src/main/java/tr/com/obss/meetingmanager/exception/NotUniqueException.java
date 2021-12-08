package tr.com.obss.meetingmanager.exception;

import java.util.Set;

public class NotUniqueException extends BaseException{
    private static final int code = 1006;
    private static final long serialVersionUID = 23405878576840153L;

    public NotUniqueException(String message) {
        super(message, code);
    }

    public NotUniqueException(String message, Set<String> errList) {
        super(message, errList, code);
    }
}

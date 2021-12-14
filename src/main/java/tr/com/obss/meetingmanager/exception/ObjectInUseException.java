package tr.com.obss.meetingmanager.exception;

import java.util.Set;

public class ObjectInUseException extends BaseException{
    private static final int code = 1007;
    private static final long serialVersionUID = 23405878576840153L;

    public ObjectInUseException(String message) {
        super(message, code);
    }

    public ObjectInUseException(String message, Set<String> errList) {
        super(message, errList, code);
    }
}

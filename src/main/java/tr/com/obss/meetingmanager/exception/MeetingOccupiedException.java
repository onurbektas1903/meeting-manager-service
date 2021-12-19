package tr.com.obss.meetingmanager.exception;

import tr.com.common.exceptions.BaseException;

import java.util.Set;

public class MeetingOccupiedException  extends BaseException {
    private static final int code = 1001;
    private static final long serialVersionUID = 23405878576840153L;

    public MeetingOccupiedException(String message) {
        super(message, code);
    }

    public MeetingOccupiedException(String message, Set<String> errList) {
        super(message, errList, code);
    }
}

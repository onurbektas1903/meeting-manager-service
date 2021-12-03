package tr.com.obss.meetingmanager.exception;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

@Data
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = -3710020688038755005L;

    private int errorCode;
    private Set<String> errorList;
    private String message;
    private HashMap<String,String> detailedErrors;
    public ErrorMessage(int errorCode, Set<String> errorList, String message) {
        this.errorCode = errorCode;
        this.errorList = errorList;
        this.message = message;
    }
    public ErrorMessage(int errorCode, HashMap<String,String> detailedErrors, String message) {
        this.errorCode = errorCode;
        this.detailedErrors = detailedErrors;
        this.message = message;
    }

    public ErrorMessage(BaseException e){
        this.errorCode = e.getCode();
        this.errorList = e.getErrList();
        this.message = e.getMessage();
    }
}

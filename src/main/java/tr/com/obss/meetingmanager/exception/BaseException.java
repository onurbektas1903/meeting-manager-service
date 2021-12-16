package tr.com.obss.meetingmanager.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BaseException extends RuntimeException{
    private static final long serialVersionUID = -52048225256247522L;
    protected Set<String> errList;
    protected Map<String,String> detailedErrorList;
    private int code;

    public BaseException(String message,int code){
        super(message);
        this.code = code;
    }
    public BaseException(String message,Set<String> errList,int code){
        super(message);
        this.errList = errList;
        this.code = code;
    }
    public BaseException(String message, Map<String,String> detailedErrorList, int code){
        super(message);
        this.detailedErrorList = detailedErrorList;
        this.code = code;
    }
    public BaseException(String message, Set<String> errList, Map<String,String> detailedErrorList, int code){
        super(message);
        this.errList = errList;
        this.detailedErrorList = detailedErrorList;
        this.code = code;
    }

}

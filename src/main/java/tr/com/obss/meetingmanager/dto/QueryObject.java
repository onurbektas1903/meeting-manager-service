package tr.com.obss.meetingmanager.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryObject implements Serializable {
  private static final long serialVersionUID = -573355107162777082L;
  private int pageSize;
  private int pageNumber;
}

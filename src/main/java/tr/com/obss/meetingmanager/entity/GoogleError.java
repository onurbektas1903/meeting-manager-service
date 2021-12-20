package tr.com.obss.meetingmanager.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class GoogleError {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id ;
    private String meetingId;
    private String errorMessage;
    private int errorCode;

}

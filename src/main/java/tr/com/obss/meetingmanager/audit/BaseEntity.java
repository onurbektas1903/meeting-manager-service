package tr.com.obss.meetingmanager.audit;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 7254354677989266791L;
    @Id
    private String id;

    @Column(nullable = false)
    private boolean deleted;

}

package tr.com.obss.meetingmanager.audit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -8299390888890590912L;
  @CreatedDate private long createdDate;

  @LastModifiedDate private long lastModifiedDate;

  @CreatedBy private String createdBy;

  @LastModifiedBy private String updatedBy;
}

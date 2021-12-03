package tr.com.obss.meetingmanager.dto.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class GoogleProviderAccountDetail implements Serializable {

    private static final long serialVersionUID = -7613145535055916247L;
    private String accountMail;
    private String fileName;
}

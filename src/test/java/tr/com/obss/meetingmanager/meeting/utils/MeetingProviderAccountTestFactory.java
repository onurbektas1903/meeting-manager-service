package tr.com.obss.meetingmanager.meeting.utils;

import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProvider;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderDTO;

public class MeetingProviderAccountTestFactory {

  public static final String ID = String.valueOf(UUID.randomUUID());
  public static final String ACCOUNT_MAIL = "dummy@gmail.com";
  public static final String APPLICATION_NAME = "schedule-app";
  public static final String ROLE_GROUP = "dummyRoleGroup";

  public static ProviderAccountDTO createProviderAccountDTO(MeetingProviderTypeEnum type) {
    return ProviderAccountDTO.builder()
            .id(ID)
            .accountMail(ACCOUNT_MAIL)
            .applicationName(APPLICATION_NAME)
            .meetingProvider(createMeetingProviderDTO(type))
            .meetingProviderType(type)
            .build();
  }

  public static ProviderAccount createProviderAccount(MeetingProviderTypeEnum type) {
     ProviderAccount providerAccount = new ProviderAccount();
     providerAccount.setId(ID);
     providerAccount.setAccountMail(ACCOUNT_MAIL);
     providerAccount.setApplicationName(APPLICATION_NAME);
     providerAccount.setMeetingProvider(createMeetingProvider(type));
     return providerAccount;
  }
  public static List<ProviderAccount> createProviderAccountList(){
     return Collections.singletonList(createProviderAccount(GOOGLE));
  }
  public static List<ProviderAccountDTO> createProviderAccountDTOList(){
     return Collections.singletonList(createProviderAccountDTO(GOOGLE));
  }

}

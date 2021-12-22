package tr.com.obss.meetingmanager.meeting.utils;

import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.POOL;
import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.SINGLE;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

public class MeetingProviderTestFactory {

  public static final String PROVIDER_ID = String.valueOf(UUID.randomUUID());
  public static final String NAME = "dummyProvider";
  public static final String DESCRIPTION = "description";
  public static final String ROLE_GROUP = "dummyRoleGroup";

  public static MeetingProviderDTO createMeetingProviderDTO(MeetingProviderTypeEnum type,
                                                            ConferenceProviderTypeEnum conferenceType) {
    HashMap<String, Object> accounts = new HashMap<>();
    accounts.put("id1", "accountMail");
    accounts.put("id2", "accountMail2");
    return MeetingProviderDTO.builder()
            .meetingProviderType(type)
            .conferenceType(conferenceType)
            .id(PROVIDER_ID)
            .name(NAME)
            .userRoleGroup(ROLE_GROUP)
            .accounts(accounts)
            .build();
  }

  public static MeetingProviderDTO createMeetingProviderDTOSingle(MeetingProviderTypeEnum type) {
    return createMeetingProviderDTO(type, SINGLE);
  }

  public static MeetingProviderDTO createMeetingProviderDTOPool(MeetingProviderTypeEnum type) {
    return createMeetingProviderDTO(type, POOL);
  }

  public static MeetingProvider createMeetingProviderSingle(MeetingProviderTypeEnum type) {
    return createMeetingProvider(type, SINGLE);
  }

  public static MeetingProvider createMeetingProviderPool(MeetingProviderTypeEnum type) {
    return createMeetingProvider(type, POOL);
  }

  public static MeetingProvider createMeetingProvider(MeetingProviderTypeEnum type, ConferenceProviderTypeEnum conferenceType) {
    HashMap<String, Object> accounts = new HashMap<>();
    accounts.put("id1", "accountMail");
    accounts.put("id2", "accountMail2");
    MeetingProvider meetingProvider = new MeetingProvider();
    meetingProvider.setId(PROVIDER_ID);
    meetingProvider.setName(NAME);
    meetingProvider.setUserRoleGroup(ROLE_GROUP);
    meetingProvider.setMeetingProviderType(type);
    meetingProvider.setConferenceType(conferenceType);
    meetingProvider.setAccounts(accounts);
    return meetingProvider;
  }

  public static List<MeetingProvider> createMeetingProviderListSingle(){
     return Collections.singletonList(createMeetingProviderSingle(GOOGLE));
  }

  public static List<MeetingProvider> createMeetingProviderListPool(){
     return Collections.singletonList(createMeetingProviderPool(GOOGLE));
  }
  public static List<MeetingProviderDTO> createMeetingProviderDTOList(){
     return Collections.singletonList(createMeetingProviderDTOSingle(GOOGLE));
  }

}

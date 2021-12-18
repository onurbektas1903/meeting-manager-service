package tr.com.obss.meetingmanager.meeting.utils;

import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum.SINGLE;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;

public class MeetingProviderTestFactory {

  public static final String PROVIDER_ID = String.valueOf(UUID.randomUUID());
  public static final String NAME = "dummyProvider";
  public static final String DESCRIPTION = "description";
  public static final String ROLE_GROUP = "dummyRoleGroup";

  public static MeetingProviderDTO createMeetingProviderDTO(MeetingProviderTypeEnum type) {
     return MeetingProviderDTO.builder()
            .meetingProviderType(type)
            .conferenceType(SINGLE)
            .id(PROVIDER_ID)
            .name(NAME)
            .userRoleGroup(ROLE_GROUP)
            .build();
  }

  public static MeetingProvider createMeetingProvider(MeetingProviderTypeEnum type) {
    MeetingProvider meetingProvider = new MeetingProvider();
    meetingProvider.setId(PROVIDER_ID);
    meetingProvider.setName(NAME);
    meetingProvider.setUserRoleGroup(ROLE_GROUP);
    meetingProvider.setMeetingProviderType(type);
    meetingProvider.setConferenceType(SINGLE);
    return meetingProvider;
  }
  public static List<MeetingProvider> createMeetingProviderList(){
     return Collections.singletonList(createMeetingProvider(GOOGLE));
  }
  public static List<MeetingProviderDTO> createMeetingProviderDTOList(){
     return Collections.singletonList(createMeetingProviderDTO(GOOGLE));
  }

}

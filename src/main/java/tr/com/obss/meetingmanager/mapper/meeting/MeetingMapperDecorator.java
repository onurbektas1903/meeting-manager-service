package tr.com.obss.meetingmanager.mapper.meeting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.ProviderAccount;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Slf4j
public class MeetingMapperDecorator implements MeetingMapper {
  private MeetingMapper delegate;
  private MeetingProviderDecorator providerMapper;

  @Autowired
  @Qualifier("delegate")
  public void setDelegate(MeetingMapper delegate) {
    this.delegate = delegate;
  }

  @Autowired
  public void setProviderMapper(MeetingProviderDecorator providerMapper) {
    this.providerMapper = providerMapper;
  }

  @Override
  public MeetingDTO toDTO(Meeting meeting) {
    MeetingDTO meetingDTO = delegate.toDTO(meeting);
    ProviderAccount account = meeting.getProviderAccount();
    if (account != null && account.getMeetingProvider() != null) {
      meetingDTO.setMeetingProvider(
          providerMapper.toDTOWithoutProviderAccounts(account.getMeetingProvider()));
    }
    return meetingDTO;
  }

  @Override
  public MeetingDTO toDTOWithAccount(Meeting meeting) {
    MeetingDTO meetingDTO = toDTO(meeting);

    meetingDTO.setProviderAccount(
        ProviderAccountDTO.builder().id(meeting.getProviderAccount().getId()).build());
    return meetingDTO;
  }

  @Override
  public Meeting toEntity(MeetingDTO meetingDTO) {
    Meeting meeting = delegate.toEntity(meetingDTO);
    if (meetingDTO.getId() == null || meetingDTO.getId().isEmpty()) {
      meeting.setId(UUID.randomUUID().toString());
    }
    meeting
        .getRecipients()
        .forEach(
            recipient -> {
              recipient.setMeeting(meeting);
              if (recipient.getId() == null) {
                recipient.setId(UUID.randomUUID().toString());
              }
            });
    return meeting;
  }

  @Override
  public void updateMeeting(MeetingDTO dto, Meeting entity) {
    delegate.updateMeeting(dto, entity);
    if (entity.getRecipients() != null && !entity.getRecipients().isEmpty()) {
      entity
          .getRecipients()
          .forEach(
              recipient -> {
                recipient.setMeeting(entity);
                if (recipient.getId() == null) {
                  recipient.setId(UUID.randomUUID().toString());
                }
              });
    }
  }

  @Override
  public List<MeetingDTO> toDTOList(List<Meeting> meetings) {
    return meetings.parallelStream().map(this::toDTO).collect(Collectors.toList());
  }
}

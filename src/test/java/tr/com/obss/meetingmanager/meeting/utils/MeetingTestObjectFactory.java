package tr.com.obss.meetingmanager.meeting.utils;

import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.RecipientDTO;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.Recipient;
import tr.com.obss.meetingmanager.entity.SlotRequest;
import tr.com.obss.meetingmanager.enums.ConferenceProviderTypeEnum;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;
import static tr.com.obss.meetingmanager.enums.MeetingProviderTypeEnum.GOOGLE;
import static tr.com.obss.meetingmanager.enums.SlotRequestStatusEnum.WAITING;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderDTO;
import static tr.com.obss.meetingmanager.meeting.utils.MeetingProviderTestFactory.createMeetingProviderSingle;

public class MeetingTestObjectFactory {

  public static final String MEETING_ID = String.valueOf(UUID.randomUUID());
  public static final String RECIPIENT1_ID = String.valueOf(UUID.randomUUID());
  public static final String SLOT1_ID = String.valueOf(UUID.randomUUID());
  public static final String SLOT2_ID = String.valueOf(UUID.randomUUID());
  public static final String RECIPIENT2_ID = String.valueOf(UUID.randomUUID());
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
  public static final String RECIPIENT1_NAME = "recipient1";
  public static final String RECIPIENT2_NAME = "recipient2";
  public static final String ORGANIZER = "organizer";
  public static final long START_DATE = Instant.now().plus(30, MINUTES).toEpochMilli();
  public static final long END_DATE = Instant.now().plus(45, MINUTES).toEpochMilli();
  public static final long SLOT_START_DATE = Instant.now().plus(70, MINUTES).toEpochMilli();
  public static final long SLOT_END_DATE = Instant.now().plus(80, MINUTES).toEpochMilli();

  public static MeetingDTO createMeetingDTO() {
    return createMeetingDTOWithProviderType(ConferenceProviderTypeEnum.SINGLE);
  }

  public static MeetingDTO createMeetingDTOFromPoolProvider() {
    return createMeetingDTOWithProviderType(ConferenceProviderTypeEnum.POOL);
  }

  private static MeetingDTO createMeetingDTOWithProviderType(
      ConferenceProviderTypeEnum providerType) {
    return MeetingDTO.builder()
        .id(MEETING_ID)
        .title(TITLE)
        .description(DESCRIPTION)
        .meetingProvider(createMeetingProviderDTO(GOOGLE, providerType))
        .start(START_DATE)
        .end(END_DATE)
        .recipients(createRecipientsDTOList())
        .organizer(ORGANIZER)
        .slotRequests(new ArrayList<>())
        .providerAccount("Account")
        .build();
  }

  public static Meeting createMeetingEntity() {
    Meeting meeting = new Meeting();
    meeting.setId(MEETING_ID);
    meeting.setTitle(TITLE);
    meeting.setDescription(DESCRIPTION);
    meeting.setRecipients(createRecipientsList());
    meeting.setStartDate(START_DATE);
    meeting.setSlotRequests(new ArrayList<>());
    meeting.setEndDate(END_DATE);
    meeting.setOrganizer(ORGANIZER);
    meeting.setProviderAccount("account");
    meeting.setMeetingProvider(createMeetingProviderSingle(GOOGLE));
    return meeting;
  }

  public static SlotRequestDTO createSlotRequestDTO() {
    return SlotRequestDTO.builder()
        .id(SLOT1_ID)
        .creator(RECIPIENT1_NAME)
        .organizer(ORGANIZER)
        .title(TITLE)
        .description(DESCRIPTION)
        .startDate(SLOT_START_DATE)
        .endDate(SLOT_END_DATE)
        .meetingId(MEETING_ID)
        .requestStatus(WAITING)
        .type(GOOGLE)
        .meetingLink("dummyLing")
        .build();
  }

  public static SlotRequest createSlotRequest() {
    SlotRequest slotRequest = new SlotRequest();
    slotRequest.setId(SLOT1_ID);
    slotRequest.setCreator(RECIPIENT1_NAME);
    slotRequest.setOrganizer(ORGANIZER);
    slotRequest.setTitle(TITLE);
    slotRequest.setDescription(DESCRIPTION);
    slotRequest.setStartDate(SLOT_START_DATE);
    slotRequest.setEndDate(SLOT_END_DATE);
    slotRequest.setMeeting(createMeetingEntity());
    slotRequest.setRequestStatus(WAITING);
    return slotRequest;
  }

  public static List<SlotRequestDTO> createSlotRequestListDTO() {
    SlotRequestDTO slotRequestDTO = createSlotRequestDTO();
    SlotRequestDTO slotRequestDTO2 = createSlotRequestDTO();
    slotRequestDTO2.setId("secondId");
    slotRequestDTO2.setDescription("description2");
    return Arrays.asList(slotRequestDTO, slotRequestDTO2);
  }

  public static List<RecipientDTO> createRecipientsDTOList() {
    return Arrays.asList(
        new RecipientDTO(RECIPIENT1_ID, RECIPIENT1_NAME),
        new RecipientDTO(RECIPIENT2_ID, RECIPIENT2_NAME));
  }

  public static List<Recipient> createRecipientsList() {
    Recipient recipient1 = new Recipient();
    recipient1.setId(RECIPIENT1_ID);
    recipient1.setName(RECIPIENT1_NAME);
    Recipient recipient2 = new Recipient();
    recipient2.setId(RECIPIENT2_ID);
    recipient2.setName(RECIPIENT2_NAME);
    return Arrays.asList(recipient1, recipient2);
  }
}

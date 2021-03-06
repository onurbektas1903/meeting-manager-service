package tr.com.obss.meetingmanager.mapper.meeting;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.entity.Meeting;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(MeetingMapperDecorator.class)
public interface MeetingMapper {

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    @Mapping(target = "meetingProvider",ignore = true)
    MeetingDTO toDTO(Meeting meeting);

    List<MeetingDTO> toDTOList(List<Meeting> meetings);

    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    Meeting toEntity(MeetingDTO meetingDTO);

    @Mapping(target = "providerAccount",ignore = true)
    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    @Mapping(target = "slotRequests", ignore = true)
    void updateMeeting(MeetingDTO dto, @MappingTarget Meeting entity);

}

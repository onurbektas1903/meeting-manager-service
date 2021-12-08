package tr.com.obss.meetingmanager.mapper.meeting;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tr.com.obss.meetingmanager.dto.MeetingDTO;
import tr.com.obss.meetingmanager.dto.MeetingProviderDTO;
import tr.com.obss.meetingmanager.dto.ProviderAccountDTO;
import tr.com.obss.meetingmanager.dto.RecipientDTO;
import tr.com.obss.meetingmanager.entity.Meeting;
import tr.com.obss.meetingmanager.entity.MeetingProvider;
import tr.com.obss.meetingmanager.entity.ProviderAccount;
import tr.com.obss.meetingmanager.entity.Recipient;
import tr.com.obss.meetingmanager.mapper.google.GoogleMapperDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(MeetingMapperDecorator.class)
public interface MeetingMapper {

    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    @Mapping(target = "meetingProvider",ignore = true)
    @Mapping(target = "providerAccount",ignore = true)
    MeetingDTO toDTO(Meeting meeting);

    List<MeetingDTO> toDTOList(List<Meeting> meetings);

    @Mapping(source = "start", target = "startDate")
    @Mapping(source = "end", target = "endDate")
    Meeting toEntity(MeetingDTO meetingDTO);

    void updateMeeting(MeetingDTO dto, @MappingTarget Meeting entity);

}

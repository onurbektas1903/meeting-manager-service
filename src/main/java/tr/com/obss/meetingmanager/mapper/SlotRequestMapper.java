package tr.com.obss.meetingmanager.mapper;

import org.mapstruct.Mapper;
import tr.com.obss.meetingmanager.dto.SlotRequestDTO;
import tr.com.obss.meetingmanager.entity.SlotRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SlotRequestMapper {
    SlotRequestDTO toDTO(SlotRequest entity);
    SlotRequest toEntity(SlotRequestDTO slotRequestDTO);
    List<SlotRequestDTO> toDTOList(List<SlotRequest> entities);
}

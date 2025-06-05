package ua.edu.ukma.fin.ui.mapper;

import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ListResponseDto;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ResponseDto;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ViewDto;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]ListResponse;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]Response;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]View;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface [:main-h-ns:]Mapper {

    [:main-h-ns:]View mapDtoToView(Integer id, [:main-h-ns:]ViewDto dto);

    [:main-h-ns:]ResponseDto mapResponseToDto([:main-h-ns:]Response response);

    [:main-h-ns:]ListResponseDto mapListResponseToDto([:main-h-ns:]ListResponse response);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<[:main-h-ns:]ResponseDto> mapResponsesToDtos(List<[:main-h-ns:]Response> responses);
}
package ua.edu.ukma.fin.ui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ListResponseDto;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ResponseDto;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ViewDto;
import ua.edu.ukma.fin.ui.mapper.[:main-h-ns:]Mapper;

import javax.validation.Valid;

@Component
@RequiredArgsConstructor
public class [:main-h-ns:]Service {
    private final ua.edu.ukma.fin.userinfo.client.api.[:main-h-ns:]ControllerApi api;
    private final ResponseHandler responseHandler;
    private final [:main-h-ns:]Mapper mapper;

    public int create[:main-h-ns:](@Valid final [:main-h-ns:]ViewDto dto) {
        return responseHandler.execute(
                () -> api.create[:main-h-ns:](mapper.mapDtoToView(null, dto))
        );
    }

    public boolean update[:main-h-ns:](final int id, @Valid final [:main-h-ns:]ViewDto dto) {
        return responseHandler.execute(
                () -> api.update[:main-h-ns:](mapper.mapDtoToView(id, dto))
        );
    }

    public boolean delete[:main-h-ns:]ById(final int id) {
        return responseHandler.execute(
                () -> api.delete[:main-h-ns:]ById(id)
        );
    }

    public [:main-h-ns:]ResponseDto get[:main-h-ns:]ById(final int id) {
        return mapper.mapResponseToDto(
                responseHandler.executeForDto(() -> api.get[:main-h-ns:]ById(id))
        );
    }

    public [:main-h-ns:]ListResponseDto getListOf[:plural-h-ns:](final String restrict) {
        return mapper.mapListResponseToDto(
                responseHandler.executeForDto(() -> api.getListOf[:plural-h-ns:](restrict))
        );
    }

    public [:main-h-ns:]ListResponseDto getListOf[:plural-h-ns:]ByUserEmail(final String email) {
        return mapper.mapListResponseToDto(
                responseHandler.executeForDto(() -> api.getListOf[:plural-h-ns:]ByUserEmail(email))
        );
    }
}
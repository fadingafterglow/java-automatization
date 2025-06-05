package ua.edu.ukma.fin.ui.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.fin.ui.controller.rest.api.[:main-h-ns:]ControllerApi;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ListResponseDto;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ResponseDto;
import ua.edu.ukma.fin.ui.controller.rest.model.[:main-h-ns:]ViewDto;
import ua.edu.ukma.fin.ui.security.SecurityContextAccessor;
import ua.edu.ukma.fin.ui.service.[:main-h-ns:]Service;

@RestController
@RequiredArgsConstructor
@Slf4j
public class [:main-h-ns:]ControllerUi implements [:main-h-ns:]ControllerApi {

    private final [:main-h-ns:]Service service;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> create[:main-h-ns:]([:main-h-ns:]ViewDto [:main-rs-ns:]ViewDto) {
        log.info("User {} wants to create new [:main-l:]: {}", SecurityContextAccessor.getAuthenticateUser().getEmail(), [:main-rs-ns:]ViewDto);
        Integer id = service.create[:main-h-ns:]([:main-rs-ns:]ViewDto);
        return ResponseEntity.ok(id);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> update[:main-h-ns:](Integer id, [:main-h-ns:]ViewDto [:main-rs-ns:]ViewDto) {
        log.info("User {} wants to update [:main-l:] with id {}: {}", SecurityContextAccessor.getAuthenticateUser().getEmail(), id, [:main-rs-ns:]ViewDto);
        Boolean isUpdated = service.update[:main-h-ns:](id, [:main-rs-ns:]ViewDto);
        return ResponseEntity.ok(isUpdated);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> delete[:main-h-ns:]ById(Integer id) {
        log.info("User {} wants to delete [:main-l:] with id {}", SecurityContextAccessor.getAuthenticateUser().getEmail(), id);
        Boolean isDeleted = service.delete[:main-h-ns:]ById(id);
        return ResponseEntity.ok(isDeleted);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<[:main-h-ns:]ResponseDto> get[:main-h-ns:]ById(Integer id) {
        [:main-h-ns:]ResponseDto responseDto = service.get[:main-h-ns:]ById(id);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<[:main-h-ns:]ListResponseDto> getListOf[:plural-h-ns:](String restrict) {
        [:main-h-ns:]ListResponseDto responseDto = service.getListOf[:plural-h-ns:](restrict);
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<[:main-h-ns:]ListResponseDto> getListOf[:plural-h-ns:]ByUserEmail(String email) {
        [:main-h-ns:]ListResponseDto responseDto = service.getListOf[:plural-h-ns:]ByUserEmail(email);
        return ResponseEntity.ok(responseDto);
    }
}
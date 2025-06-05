package ua.edu.ukma.fin.userinfo.controllers.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.fin.common.response.CommonResponse;
import ua.edu.ukma.fin.userinfo.controllers.rest.api.[:main-h-ns:]ControllerApi;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]ListResponse;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]Response;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]View;
import ua.edu.ukma.fin.userinfo.services.[:main-l-sd:].[:main-h-ns:]Service;
import ua.edu.ukma.fin.userinfo.security.SecurityContextAccessor;


@RestController
@RequiredArgsConstructor
@Slf4j
public class [:main-h-ns:]Controller implements [:main-h-ns:]ControllerApi {

    private final [:main-h-ns:]Service service;

    @Override
    public ResponseEntity<CommonResponse.IntegerResponse> create[:main-h-ns:]([:main-h-ns:]View body) {
        log.info("User {} wants to create new [:main-l:] {}", SecurityContextAccessor.getBehalfOnEmail(), body);
        return ResponseEntity.ok(CommonResponse.IntegerResponse.of(service.create(body)));
    }

    @Override
    public ResponseEntity<CommonResponse.BooleanResponse> update[:main-h-ns:]([:main-h-ns:]View body) {
        log.info("User {} wants to update [:main-l:] {}", SecurityContextAccessor.getBehalfOnEmail(), body);
        return ResponseEntity.ok(CommonResponse.BooleanResponse.of(service.update(body)));
    }

    @Override
    public ResponseEntity<CommonResponse.BooleanResponse> delete[:main-h-ns:]ById(Integer id) {
        log.info("User {} wants to delete [:main-l:] with id {}", SecurityContextAccessor.getBehalfOnEmail(), id);
        return ResponseEntity.ok(CommonResponse.BooleanResponse.of(service.delete(id)));
    }

    @Override
    public ResponseEntity<[:main-h-ns:]Response> get[:main-h-ns:]ById(Integer id) {
        return ResponseEntity.ok(service.getResponseById(id));
    }

    @Override
    public ResponseEntity<[:main-h-ns:]ListResponse> getListOf[:plural-h-ns:](String restrict) {
        return ResponseEntity.ok(service.getResponseByRestriction(restrict));
    }

    @Override
    public ResponseEntity<[:main-h-ns:]ListResponse> getListOf[:plural-h-ns:]ByUserEmail(String email) {
        return ResponseEntity.ok(service.getResponseByUserEmail(email));
    }
}

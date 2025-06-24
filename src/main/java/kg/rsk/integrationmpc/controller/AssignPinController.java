package kg.rsk.integrationmpc.controller;

import kg.rsk.integrationmpc.dto.AssignPinRequest;
import kg.rsk.integrationmpc.service.IssuingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssignPinController {

    private final IssuingService IssuingService;

    @PostMapping("/assign-pin")
    public ResponseEntity<String> assignPin(@RequestBody AssignPinRequest request) throws Exception {
        String response = IssuingService.assignPin(
                request.getCard(),
                request.getExpiry(),
                request.getPin(),
                request.getPan(),
                request.getStan());

        return ResponseEntity.ok(response);
    }
}

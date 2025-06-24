package kg.rsk.integrationmpc.controller;

import kg.rsk.integrationmpc.dto.AssignPinRequest;
import kg.rsk.integrationmpc.service.IssuingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssignPinController {

    private final IssuingService issuingService;

    @PostMapping("/assign-pin")
    public ResponseEntity<String> assignPin(@RequestBody AssignPinRequest request) throws Exception {
        String response = issuingService.assignPin(
                request.getCard(),
                request.getExpiry(),
                request.getPin(),
                request.getPan(),
                request.getStan());
        return ResponseEntity.ok(response);
    }
}

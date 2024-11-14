package ureca.nolmung.presentation.controller.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ureca.nolmung.business.dummy.DummyUseCase;
import ureca.nolmung.business.dummy.dto.request.AddDummyReq;
import ureca.nolmung.business.dummy.dto.response.AddDummyResp;

@RestController
@RequestMapping("/api/v1/dummys")
@RequiredArgsConstructor
public class DummyController {

    private final DummyUseCase dummyUseCase;

    @PostMapping("/add")
    public ResponseEntity<AddDummyResp> addDummy(@RequestBody AddDummyReq req) {
        AddDummyResp resp = dummyUseCase.addDummy(req);
        return ResponseEntity.ok(resp);
    }

}

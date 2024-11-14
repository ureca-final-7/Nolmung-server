package ureca.nolmung.business.dummy;

import ureca.nolmung.business.dummy.dto.request.AddDummyReq;
import ureca.nolmung.business.dummy.dto.response.AddDummyResp;

public interface DummyUseCase {
    AddDummyResp addDummy(AddDummyReq req);
}

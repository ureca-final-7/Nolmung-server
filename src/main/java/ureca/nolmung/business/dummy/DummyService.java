package ureca.nolmung.business.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.nolmung.business.dummy.dto.request.AddDummyReq;
import ureca.nolmung.business.dummy.dto.response.AddDummyResp;
import ureca.nolmung.implementation.dummy.DummyManager;
import ureca.nolmung.implementation.dummy.dtomapper.DummyDtoMapper;
import ureca.nolmung.jpa.dummy.Dummy;

@Service
@RequiredArgsConstructor
public class DummyService implements DummyUseCase{

    private final DummyManager dummyManager;
    private final DummyDtoMapper dummyDtoMapper;

    @Override
    public AddDummyResp addDummy(AddDummyReq req) {
        Dummy dummy = dummyManager.addDummy(req);
        return dummyDtoMapper.toAddDummyResp(dummy);
    }
}

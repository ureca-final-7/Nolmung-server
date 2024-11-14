package ureca.nolmung.implementation.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ureca.nolmung.business.dummy.dto.request.AddDummyReq;
import ureca.nolmung.jpa.dummy.Dummy;
import ureca.nolmung.persistence.dummy.DummyRepository;

@Component
@RequiredArgsConstructor
public class DummyManager {

    private final DummyRepository dummyRepository;

    public Dummy addDummy(AddDummyReq req) {
        Dummy dummy = Dummy.builder()
                .name(req.name())
                .age(req.age())
                .build();
        return dummyRepository.save(dummy);
    }

}

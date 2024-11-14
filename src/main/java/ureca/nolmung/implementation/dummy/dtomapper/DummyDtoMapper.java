package ureca.nolmung.implementation.dummy.dtomapper;

import org.springframework.stereotype.Component;
import ureca.nolmung.business.dummy.dto.response.AddDummyResp;
import ureca.nolmung.jpa.dummy.Dummy;

@Component
public class DummyDtoMapper {

    public AddDummyResp toAddDummyResp(Dummy dummy) {
        return new AddDummyResp(dummy.getName());
    }
}

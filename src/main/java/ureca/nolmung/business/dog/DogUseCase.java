package ureca.nolmung.business.dog;

import ureca.nolmung.business.dog.dto.request.DogReq;
import ureca.nolmung.business.dog.dto.response.DogResp;

public interface DogUseCase {
    DogResp addDog(Long userId, DogReq req);
}
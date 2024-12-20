package ureca.nolmung.business.diary.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DiaryDetailResp(
        Long userId,
        Long diaryId,
        String title,
        String content,
        Boolean publicYn,
        LocalDateTime createdAt,
        List<DogDiary> dogs,
        List<Place> places,
        List<Media> medias
) {
    public record Place(
            Long placeId,
            String placeName,
            String roadAddress,
            String address,
            Double ratingAvg
    ){}
    public record Media(
            Long mediaId,
            Enum mediaType,
            String mediaUrl
    ) {}
    public record DogDiary(
            Long dogId,
            String dogName,
            String dogProfileImageUrl
    ) {}
}

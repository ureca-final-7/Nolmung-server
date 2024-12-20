package ureca.nolmung.implementation.review;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ureca.nolmung.business.review.dto.request.AddReviewReq;
import ureca.nolmung.business.review.dto.response.AddReviewResp;
import ureca.nolmung.business.review.dto.response.DeleteReviewResp;
import ureca.nolmung.business.review.dto.response.ReviewResp;
import ureca.nolmung.implementation.label.LabelException;
import ureca.nolmung.implementation.label.LabelExceptionType;
import ureca.nolmung.implementation.place.PlaceException;
import ureca.nolmung.implementation.place.PlaceExceptionType;
import ureca.nolmung.jpa.label.Label;
import ureca.nolmung.jpa.label.LabelId;
import ureca.nolmung.jpa.place.Place;
import ureca.nolmung.jpa.review.Review;
import ureca.nolmung.jpa.reviewlabel.ReviewLabel;
import ureca.nolmung.jpa.user.User;
import ureca.nolmung.persistence.label.LabelRepository;
import ureca.nolmung.persistence.place.PlaceRepository;
import ureca.nolmung.persistence.review.ReviewRepository;
import ureca.nolmung.persistence.reviewlabel.ReviewLabelRepository;

import static java.time.LocalDate.now;

@Component
@RequiredArgsConstructor
public class ReviewManager {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final ReviewLabelRepository reviewLabelRepository;
    private final LabelRepository labelRepository;

    public List<AddReviewResp> addReview(User user, AddReviewReq req) {

        List<AddReviewResp> responses = new ArrayList<>();

        for (AddReviewReq.ReviewDto reviewDto : req.reviews()){

            // 장소 유효성 검증
            Place place = placeRepository.findById(reviewDto.placeId())
                    .orElseThrow(() -> new PlaceException(PlaceExceptionType.PLACE_NOT_FOUND_EXCEPTION));

            // 리뷰 저장
            Review newReview = createReview(user, reviewDto, place);
            reviewRepository.save(newReview);

            // 장소에 대한 별점 총합 추가
            place.addRating(reviewDto.rating());
            placeRepository.save(place);

            // 리뷰-라벨 저장 및 장소별 라벨 카운트 증가
            for (AddReviewReq.ReviewDto.LabelDto labelDto : reviewDto.labels()) {
                ReviewLabel newReviewLabel = createReviewLabel(labelDto, newReview);
                reviewLabelRepository.save(newReviewLabel);

                // 해당 장소에 등록된 라벨 존재하는지 확인
                Optional<Label> existLabelOpt = labelRepository.findById_LabelIdAndId_PlaceId(labelDto.labelId(), place.getId());

                if (existLabelOpt.isPresent()) {
                    Label existingLabel = existLabelOpt.get();
                    existingLabel.addLabelCount();
                    labelRepository.save(existingLabel);
                } else {
                    // 라벨이 존재하지 않으면 새로 생성하여 저장 (초기 labelCount는 1로 설정)
                    LabelId newLabelId = new LabelId(labelDto.labelId(), place.getId());
                    Label newLabel = createLabelCount(newLabelId, place);
                    labelRepository.save(newLabel);
                }
            }
            responses.add(new AddReviewResp(newReview.getId()));
        }
        return responses;
    }


    public DeleteReviewResp deleteReview(Long reviewId) {

        // 리뷰 유효성 검증
        Review review = reviewRepository.findByIdWithPlace(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewExceptionType.REVIEW_NOT_FOUND_EXCEPTION));

        Place place = review.getPlace();

        // 장소에서 해당 리뷰에 대한 별점 제거 및 갱신 작업
        place.removeRating(review.getRating());
        placeRepository.save(place);

        List<ReviewLabel> reviewLabels = reviewLabelRepository.findByReviewId(reviewId);

        // 리뷰와 연관된 장소에 대한 라벨 카운트 차감
        for(ReviewLabel reviewLabel : reviewLabels){
            Label existingLabel = labelRepository.findById_LabelIdAndId_PlaceId(reviewLabel.getLabelId(), place.getId())
                    .orElseThrow(() -> new LabelException(LabelExceptionType.LABEL_NOT_FOUND_EXCEPTION));

            existingLabel.removeLabelCount();

            // 카운트 0이면 라벨 삭제
            if (existingLabel.getLabelCount() == 0) {
                labelRepository.delete(existingLabel);
            } else {
                labelRepository.save(existingLabel);
            }
        }
        reviewRepository.deleteById(reviewId);

        return new DeleteReviewResp(reviewId);
    }

    private Review createReview(User user, AddReviewReq.ReviewDto req, Place place) {
        return Review.builder()
                .user(user)
                .place(place)
                .rating(req.rating())
                .build();
    }

    private ReviewLabel createReviewLabel(AddReviewReq.ReviewDto.LabelDto labelDto, Review newReview) {
        return ReviewLabel.builder()
                .review(newReview)
                .labelId(labelDto.labelId())
                .labelName(labelDto.labelName())
                .build();
    }

    private Label createLabelCount(LabelId newLabelId, Place place) {
        return Label.builder()
                .id(newLabelId)
                .place(place)
                .labelCount(1)
                .build();
    }

    public void checkReviewWriter(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewExceptionType.REVIEW_NOT_FOUND_EXCEPTION));
        if(!review.getUser().getId().equals(userId)) {
            throw new ReviewException(ReviewExceptionType.REVIEW_UNAUTHORIZED_EXCEPTION);
        }
    }

    public Slice<Review> getReviews(Long userId, int page, int size) {
        return reviewRepository.findByUserIdWithPlace(userId, PageRequest.of(page, size));
    }

    public List<Review> getTodayMyReviews(Long userId) {
        return reviewRepository.findByUserIdAndCreatedAt(userId, now());
    }

    public List<Review> getDateReviews(Long userId, LocalDate date) {
        return reviewRepository.findByUserIdAndCreatedAt(userId, date);
    }
}

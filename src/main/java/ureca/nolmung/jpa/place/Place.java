package ureca.nolmung.jpa.place;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.nolmung.jpa.config.BaseEntity;
import ureca.nolmung.jpa.place.Enum.Category;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String placeImageUrl;

    @Column(nullable = false)
    private Double mapx;        // 위도

    @Column(nullable = false)
    private Double mapy;        // 경도

    @Column
    private String roadAddress;     // 도로명주소

    @Column(nullable = false)
    private String address;    // 지번주소

    @Column(length = 15)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String homepageUrl;

    @Column(length = 25)
    private String holiday;

    @Column(length = 20)
    private String openHour;

    @Column(nullable = false)
    private boolean parkingYn;

    @Column(length = 30, nullable = false)
    private String price;

    @Column(nullable = false)
    private Integer acceptSize;     //수용가능크기

    @Column(columnDefinition = "TEXT", nullable = false)
    private String rule;        // 제한사항

    @Column(nullable = false)
    private boolean impossibleYn;       // 내부동반가능여부

    @Column(nullable = false)
    private boolean outpossibleYn;      // 외부동반가능여부

    @Column(nullable = false)
    private Integer extraPrice;     // 반려견 추가요금

    @Column
    private Double ratingTotal;     // 별점 총합
}

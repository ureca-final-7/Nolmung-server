package ureca.nolmung.persistence.place;

import java.util.List;

import org.locationtech.jts.geom.Polygon;

import ureca.nolmung.jpa.place.Enum.Category;
import ureca.nolmung.jpa.place.Place;
import ureca.nolmung.jpa.user.User;

public interface PlaceRepositoryCustom {
	List<Place> findBySearchOption(User user, Category category, String acceptSize, Double ratingAvg, Boolean isBookmarked, Polygon polygon);
}

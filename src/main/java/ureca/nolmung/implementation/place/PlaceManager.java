package ureca.nolmung.implementation.place;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ureca.nolmung.business.place.dto.response.SearchedPlaceResponse;
import ureca.nolmung.business.user.dto.response.CustomUserDetails;
import ureca.nolmung.jpa.place.Enum.Category;
import ureca.nolmung.jpa.place.Place;
import ureca.nolmung.jpa.placeposition.PlacePosition;
import ureca.nolmung.persistence.bookmark.BookmarkRepository;
import ureca.nolmung.persistence.place.PlaceRepository;
import ureca.nolmung.persistence.placeposition.PlacePositionRepository;

@Component
@RequiredArgsConstructor
public class PlaceManager {

	private final int SRID = 4326;

	private final PlaceRepository placeRepository;
	private final PlacePositionRepository placePositionRepository;
	private final BookmarkRepository bookmarkRepository;

	private final GeometryFactory geometryFactory = new GeometryFactory();

	public List<Place> searchByKeyword(String keyword) {
		return placeRepository.searchByKeyword(keyword);
	}

	public Place findPlaceById(Long placeId) {
		return placeRepository.findById(placeId)
			.orElseThrow(() -> new PlaceException(PlaceExceptionType.PLACE_NOT_FOUND_EXCEPTION));
	}

	public List<Place> findAllPlace() {
		return placeRepository.findAll();
	}

	public void makePointData(List<Place> places) {
		for (Place place : places) {
			Point point = geometryFactory.createPoint(new Coordinate(place.getLongitude(), place.getLatitude()));
			point.setSRID(SRID);
			PlacePosition placePosition = PlacePosition.builder()
				.place(place)
				.location(point)
				.build();
			placePositionRepository.save(placePosition);
		}
	}

	public List<Place> findBySearchOption(CustomUserDetails userDetails, Category category, Boolean isVisited, Boolean isBookmarked, double latitude, double longitude, double maxLatitude, double maxLongitude) {
		Polygon polygon = generatePolygon(latitude, longitude, maxLatitude, maxLongitude);
		return placeRepository.findBySearchOption(userDetails, category, isVisited, isBookmarked, polygon);
	}

	public List<Place> findPlaceMapOn(double latitude, double longitude, double maxLatitude, double maxLongitude) {
		Polygon polygon = generatePolygon(latitude, longitude, maxLatitude, maxLongitude);
		return placePositionRepository.findPlaceByCoordinate(polygon);
	}

	public List<SearchedPlaceResponse> createSearchedPlaceResponse(CustomUserDetails userDetails, List<Place> places) {
		boolean isUserDetailsNull = userDetails == null;
		return places.stream()
			.map(place -> {
				boolean isBookmarked = isUserDetailsNull ? false : bookmarkRepository.existsByUserAndPlace(userDetails.getUser(), place);
				return SearchedPlaceResponse.of(place, isBookmarked);
			})
			.collect(Collectors.toList());
	}

	private Polygon generatePolygon(double latitude, double longitude, double maxLatitude, double maxLongitude) {
		Coordinate[] vertexes = createVertexes(latitude, longitude, maxLatitude, maxLongitude);
		Polygon polygon = geometryFactory.createPolygon(vertexes);
		polygon.setSRID(SRID);
		return polygon;
	}

	private Coordinate[] createVertexes(double latitude, double longitude, double maxLatitude, double maxLongitude) {
		double latitudeDelta = calcLatitudeDelta(maxLatitude, latitude);
		double longitudeDelta = calcLongitudeDelta(maxLongitude, longitude);

		double minLatitude = latitude - latitudeDelta;
		double minLongitude = longitude - longitudeDelta;

		return new Coordinate[] {
			new Coordinate(maxLongitude, minLatitude),
			new Coordinate(maxLongitude, maxLatitude),
			new Coordinate(minLongitude, maxLatitude),
			new Coordinate(minLongitude, minLatitude),
			new Coordinate(maxLongitude, minLatitude)
		};
	}

	private double calcLatitudeDelta(double maxLatitude, double latitude) {
		return maxLatitude - latitude;
	}

	private double calcLongitudeDelta(double maxLongitude, double longitude) {
		return maxLongitude - longitude;
	}

	public Boolean isBookmarked(CustomUserDetails userDetails, Place place) {
		if (userDetails == null) {
			return false;
		}

		return bookmarkRepository.existsByUserAndPlace(userDetails.getUser(), place);
	}
}

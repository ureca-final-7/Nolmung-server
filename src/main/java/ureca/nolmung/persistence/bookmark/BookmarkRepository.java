package ureca.nolmung.persistence.bookmark;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ureca.nolmung.jpa.bookmark.Bookmark;
import ureca.nolmung.jpa.place.Place;
import ureca.nolmung.jpa.user.User;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

	Boolean existsByUserAndPlace(User user, Place place);

	Optional<Bookmark> findByUserAndPlace(User user, Place place);

	@Override
	@Query("SELECT b FROM Bookmark b JOIN b.user u JOIN b.place p")
	List<Bookmark> findAll();
}

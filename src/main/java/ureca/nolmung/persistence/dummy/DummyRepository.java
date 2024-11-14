package ureca.nolmung.persistence.dummy;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.nolmung.jpa.dummy.Dummy;

public interface DummyRepository extends JpaRepository<Dummy, Long> {
}

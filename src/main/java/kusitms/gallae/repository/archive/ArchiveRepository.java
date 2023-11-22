package kusitms.gallae.repository.archive;

import kusitms.gallae.domain.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {

}

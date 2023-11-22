package kusitms.gallae.repository.archive;


import kusitms.gallae.domain.Archive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArchiveRespositoryCustom {

    Page<Archive> findArchiveDynamicCategory(String category, String title, Pageable pageable);

}

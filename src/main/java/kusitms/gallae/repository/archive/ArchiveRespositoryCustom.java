package kusitms.gallae.repository.archive;


import kusitms.gallae.domain.Archive;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.archive.ArchiveDetailRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArchiveRespositoryCustom {

    Page<Archive> findArchiveDynamicCategory(String category, Pageable pageable);

}

package kusitms.gallae.service.archive;


import kusitms.gallae.domain.Archive;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.archive.ArchiveDtoRes;
import kusitms.gallae.dto.archive.ArchivePageRes;
import kusitms.gallae.dto.archive.ArchivePostReq;
import kusitms.gallae.repository.archive.ArchiveRepository;
import kusitms.gallae.repository.archive.ArchiveRespositoryCustom;
import kusitms.gallae.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ArchiveService {


    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private ArchiveRespositoryCustom archiveRespositoryCustom;

    @Autowired
    private UserRepository userRepository;


    public ArchivePageRes getArchivesByCategory(String category, Pageable pageable) {
        Page<Archive> archives = archiveRespositoryCustom.findArchiveDynamicCategory(category,pageable);
        List<ArchiveDtoRes> archiveDtos = archives.getContent().stream()
                .map(archive -> {
                    ArchiveDtoRes archiveDtoRes =new ArchiveDtoRes();
                    archiveDtoRes.setCategory(archive.getCategory());
                    archiveDtoRes.setId(archive.getId());
                    archiveDtoRes.setWriter(archive.getWriter());
                    archiveDtoRes.setTitle(archive.getTitle());
                    archiveDtoRes.setCreatedDate(archive.getCreatedAt());
                    return archiveDtoRes;
                }).collect(Collectors.toList());

        ArchivePageRes archivePageRes = new ArchivePageRes();
        archivePageRes.setArchives(archiveDtos);
        archivePageRes.setTotalSize(archives.getTotalPages());

        return archivePageRes;
    }

    public void postArchive(ArchivePostReq archivePostReq, String username) {
        Archive archive = new Archive();
        User user = userRepository.findById(Long.valueOf(username)).get();
        archive.setTitle(archivePostReq.getTitle());
        archive.setUser(user);
        archive.setBody(archivePostReq.getBody());
        archive.setFileName(archivePostReq.getFileName());
        archive.setWriter(archivePostReq.getWriter());
        archive.setCategory(archivePostReq.getCategory());
        archive.setFileUrl(archivePostReq.getFileUrl());
        archive.setHashtag(archivePostReq.getHashTags());
        archiveRepository.save(archive);
    }


}

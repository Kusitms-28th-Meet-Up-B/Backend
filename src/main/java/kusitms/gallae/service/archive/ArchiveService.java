package kusitms.gallae.service.archive;


import kusitms.gallae.domain.Archive;
import kusitms.gallae.domain.Review;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.archive.ArchiveDetailRes;
import kusitms.gallae.dto.archive.ArchiveDtoRes;
import kusitms.gallae.dto.archive.ArchivePageRes;
import kusitms.gallae.dto.archive.ArchivePostReq;
import kusitms.gallae.repository.archive.ArchiveRepository;
import kusitms.gallae.repository.archive.ArchiveRespositoryCustom;
import kusitms.gallae.repository.favoriteArchiveRepository.FavoriteArchiveRepository;
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
    private FavoriteArchiveRepository favoriteArchiveRepository;

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

    public Long postArchive(ArchivePostReq archivePostReq, String username) {
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
        archive.setLikes(0L);
        Archive saveArchive = archiveRepository.save(archive);
        return saveArchive.getId();
    }

    public ArchiveDetailRes getArchiveById(Long archiveId , String username) {
        User user = null;
        if(username != null) {
            user = userRepository.findById(Long.valueOf(username)).orElse(null);
        }
        User finalUser = user;
        return archiveRepository.findById(archiveId)
                .map(archive -> {
                    ArchiveDetailRes detailRes = new ArchiveDetailRes();
                    detailRes.setId(archive.getId());
                    detailRes.setCategory(archive.getCategory());
                    detailRes.setTitle(archive.getTitle());
                    detailRes.setWriter(archive.getWriter());
                    detailRes.setFileName(archive.getFileName());
                    detailRes.setFileUrl(archive.getFileUrl());
                    detailRes.setHashtag(archive.getHashtag());
                    detailRes.setBody(archive.getBody());
                    detailRes.setCreatedDate(archive.getCreatedAt());
                    if(finalUser != null) {
                        detailRes.setLikeCheck(favoriteArchiveRepository.existsByUserAndArchive(finalUser,archive));
                    }
                    Long prevId = getPreviousArchiveId(archiveId);
                    Long nextId = getNextArchiveId(archiveId);

                    detailRes.setPreviousId(prevId);
                    detailRes.setNextId(nextId);

                    return detailRes;
                })
                .orElse(null);
    }


    public Long getPreviousArchiveId(Long currentId) {
        return archiveRepository.findTop1ByIdLessThanOrderByIdDesc(currentId)
                .map(Archive::getId)
                .orElse(null);
    }

    public Long getNextArchiveId(Long currentId) {
        return archiveRepository.findTop1ByIdGreaterThanOrderByIdAsc(currentId)
                .map(Archive::getId)
                .orElse(null);
    }

    public Page<Archive> getAllArchivesSortedByLikes(Pageable pageable) {
        return archiveRepository.findAllByOrderByLikesDesc(pageable);
    }


}

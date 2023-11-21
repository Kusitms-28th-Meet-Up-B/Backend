package kusitms.gallae.service.archive;


import kusitms.gallae.config.BaseException;
import kusitms.gallae.config.BaseResponseStatus;
import kusitms.gallae.domain.*;
import kusitms.gallae.dto.archive.*;
import kusitms.gallae.dto.review.ReviewEditReq;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.repository.archive.ArchiveRepository;
import kusitms.gallae.repository.archive.ArchiveRespositoryCustom;
import kusitms.gallae.repository.favoriteArchiveRepository.FavoriteArchiveRepository;
import kusitms.gallae.repository.point.PointRepository;
import kusitms.gallae.repository.user.UserRepository;
import kusitms.gallae.repository.userArchive.UserArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class ArchiveService {


    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private ArchiveRespositoryCustom archiveRespositoryCustom;

    @Autowired
    private FavoriteArchiveRepository favoriteArchiveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserArchiveRepository userArchiveRepository;

    @Autowired
    private S3Service s3Service;

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

    public ArchiveDetailRes checkArchiveEditable(Long archiveId, String username) {
        Archive archive = archiveRepository.findById(archiveId).orElse(null);
        User user = userRepository.findById(Long.valueOf(username)).orElse(null);
        if(archive.getUser().getId() != user.getId()){
            throw new BaseException(BaseResponseStatus.NOT_WRITER);
        }
        return convertArchive(archive,user);
    }

    public void deleteArchive(Long archiveId, String username) {
        Archive archive = archiveRepository.findById(archiveId).orElse(null);
        User user = userRepository.findById(Long.valueOf(username)).orElse(null);
        if(archive.getUser().getId() != user.getId()){
            throw new BaseException(BaseResponseStatus.NOT_WRITER);
        }
        if(archive.getFileUrl() != null ){
            s3Service.deleteFile(archive.getFileUrl());
        }
        favoriteArchiveRepository.deleteAllByArchive(archive);
        userArchiveRepository.deleteAllByArchive(archive);
        archiveRepository.deleteById(archiveId);
    }

    public Long editArchive(ArchiveEditReq archiveEditReq) {
        Archive archive = archiveRepository.findById(archiveEditReq.getArchiveId()).orElse(null);
        if(archive.getFileUrl() != null ){
            s3Service.deleteFile(archiveEditReq.getFileUrl());
        }
        archive.setTitle(archiveEditReq.getTitle());
        archive.setBody(archiveEditReq.getBody());
        archive.setCategory(archiveEditReq.getCategory());
        archive.setFileName(archiveEditReq.getFileName());
        archive.setWriter(archiveEditReq.getWriter());
        archive.setFileUrl(archiveEditReq.getFileUrl());
        archive.setHashtag(archiveEditReq.getHashTags());
        Archive saveArchive = archiveRepository.save(archive);
        return saveArchive.getId();
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

        //포인트 적립
        Point point = new Point();
        point.setDate(LocalDate.now());
        point.setPointCategory("적립");
        point.setPointActivity("보고서 작성");
        point.setTime(LocalTime.now());
        point.setPointScore(30);
        point.setUser(user);
        pointRepository.save(point);
        user.setPoint(user.getPoint() + 30);
        userRepository.save(user);
        return saveArchive.getId();
    }

    public ArchiveDetailRes getArchiveById(Long archiveId , String username) {
        User user = userRepository.findById(Long.valueOf(username)).orElse(null);
        if(user.getPoint() < 15 ) throw new BaseException(BaseResponseStatus.POINT_TRIBE);
        Archive archive = archiveRepository.findById(archiveId).orElse(null);
        if(archive.getUser().getId() != user.getId() && !userArchiveRepository.existsByUserAndArchive(user,archive)) {
            //포인트 적립
            Point point = new Point();
            point.setDate(LocalDate.now());
            point.setPointCategory("사용");
            point.setPointActivity("보고서 열람");
            point.setTime(LocalTime.now());
            point.setPointScore(-15);
            point.setUser(user);
            pointRepository.save(point);
            user.setPoint(user.getPoint() - 15);
            userRepository.save(user);

            UserArchive userArchive = new UserArchive();
            userArchive.setUser(user);
            userArchive.setArchive(archive);
            userArchiveRepository.save(userArchive);
        }

        return convertArchive(archive,user);
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

    private ArchiveDetailRes convertArchive(Archive archive,User user){
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
        if(user != null) {
            detailRes.setLikeCheck(favoriteArchiveRepository.existsByUserAndArchive(user,archive));
        }
        Long prevId = getPreviousArchiveId(archive.getId());
        Long nextId = getNextArchiveId(archive.getId());

        detailRes.setPreviousId(prevId);
        detailRes.setNextId(nextId);

        return detailRes;
    }
}

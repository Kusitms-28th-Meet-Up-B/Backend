package kusitms.gallae.service.user;

import kusitms.gallae.domain.Archive;
import kusitms.gallae.domain.Review;
import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.ManagerRegistratiorDto;
import kusitms.gallae.dto.user.UserPostDto;
import kusitms.gallae.dto.user.UserPostsPageRes;
import kusitms.gallae.dto.user.UserRegistrationDto;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.repository.archive.ArchiveRepository;
import kusitms.gallae.repository.review.ReviewRepository;
import kusitms.gallae.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Service s3Service;

    private final ArchiveRepository archiveRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public UserService(ArchiveRepository archiveRepository, ReviewRepository reviewRepository) {
        this.archiveRepository = archiveRepository;
        this.reviewRepository = reviewRepository;
    }

    public void registerNewManager(ManagerRegistratiorDto registrationDto) throws IOException {

        String profileImageUrl = null;
        if (registrationDto.getProfileImage() != null && !registrationDto.getProfileImage().isEmpty()) {
            profileImageUrl = s3Service.upload(registrationDto.getProfileImage());
        }

        User newUser = User.builder()
                .name(registrationDto.getName())
                .nickName(registrationDto.getCompanyName())
                .loginId(registrationDto.getLoginId())
                .phoneNumber(registrationDto.getPhoneNum()) // 선택적 입력
                .email(registrationDto.getEmail()) // 선택적 입력
                .department(registrationDto.getDepartment())
                .birth(registrationDto.getBirth())
                .refreshToken("")  // 회원가입은 토큰 없음
                .profileImageUrl(profileImageUrl) // 프로필 이미지 URL 추가
                .signUpStatus(User.UserSignUpStatus.MANAGER)
                .loginPw(registrationDto.getLoginPw())
                .point(100L)
                .build();

        userRepository.save(newUser);

    }

    public void registerNewUser(UserRegistrationDto registrationDto) throws IllegalStateException, IOException {

        String profileImageUrl = null;
        if (registrationDto.getProfileImage() != null && !registrationDto.getProfileImage().isEmpty()) {
            profileImageUrl = s3Service.upload(registrationDto.getProfileImage());
        }

        User newUser = User.builder()
                .name(registrationDto.getName())
                .nickName(registrationDto.getNickName())
                .loginId(registrationDto.getLoginId())
                .phoneNumber(registrationDto.getPhoneNumber()) // 선택적 입력
                .email(registrationDto.getEmail()) // 선택적 입력
                .birth(registrationDto.getBirth())
                .refreshToken("")  // 회원가입은 토큰 없음
                .profileImageUrl(profileImageUrl) // 프로필 이미지 URL 추가
                .signUpStatus(User.UserSignUpStatus.USER)
                .loginPw(registrationDto.getLoginPw())
                .point(100L)
                .build();

        userRepository.save(newUser);
    }

    public Boolean checkDuplicateLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public Boolean checkDuplicateNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    public UserPostsPageRes getUserPostByArchive(String userId,Pageable pageable) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        Page<Archive> archives = archiveRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        UserPostsPageRes userPostsPageRes = new UserPostsPageRes();
        userPostsPageRes.setUserPosts(archives.getContent().stream().map(archive-> {
            UserPostDto userPostDto = new UserPostDto();
            userPostDto.setId(archive.getId());
            userPostDto.setWriter(archive.getWriter());
            userPostDto.setCategory(archive.getCategory());
            userPostDto.setTitle(archive.getTitle());
            userPostDto.setCreatedAt(archive.getCreatedAt());
            return userPostDto;
        }).collect(Collectors.toList()));
        userPostsPageRes.setTotalPages(archives.getTotalPages());
        return userPostsPageRes;
    }
    public UserPostsPageRes getUserPostByReview(String userId,Pageable pageable) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        Page<Review> reviews = reviewRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        UserPostsPageRes userPostsPageRes = new UserPostsPageRes();
        userPostsPageRes.setUserPosts(reviews.getContent().stream().map(review-> {
            UserPostDto userPostDto = new UserPostDto();
            userPostDto.setId(review.getId());
            userPostDto.setWriter(review.getWriter());
            userPostDto.setCategory(review.getCategory());
            userPostDto.setTitle(review.getTitle());
            userPostDto.setCreatedAt(review.getCreatedAt());
            return userPostDto;
        }).collect(Collectors.toList()));
        userPostsPageRes.setTotalPages(reviews.getTotalPages());
        return userPostsPageRes;
    }
}

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

    public Page<UserPostDto> getUserPosts(String userId, Pageable pageable) {
        Page<UserPostDto> reviewPosts = reviewRepository.findByWriter(userId, pageable);
        Page<UserPostDto> archivePosts = archiveRepository.findByWriter(userId, pageable);
 //reveiew 랑 archive합쳐서 ... 한번에 출력하게 해주는 거래
        List<UserPostDto> combinedPosts = Stream.concat(reviewPosts.getContent().stream(), archivePosts.getContent().stream())
                .sorted(Comparator.comparing(UserPostDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
        Page<UserPostDto> combinedPage = new PageImpl<>(combinedPosts, pageable, combinedPosts.size());

        return combinedPage;
    }
}

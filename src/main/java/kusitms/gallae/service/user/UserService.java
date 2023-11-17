package kusitms.gallae.service.user;

import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.UserRegistrationDto;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@Service
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, S3Service s3Service) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
    }

    public User registerNewUser(UserRegistrationDto registrationDto) throws IllegalStateException, IOException {
        if (userRepository.existsByLoginId(registrationDto.getLoginId())) {
            throw new IllegalStateException("이미 존재하는 ID 입니다.");
        }
        if (userRepository.existsByNickName(registrationDto.getNickName())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

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
                .refreshToken("")  // 회원가입은 토큰 없음
                .profileImageUrl(profileImageUrl) // 프로필 이미지 URL 추가
                .signUpStatus(User.UserSignUpStatus.USER)
                .loginPw(passwordEncoder.encode(registrationDto.getLoginPw()))
                .build();

        return userRepository.save(newUser);
    }
}

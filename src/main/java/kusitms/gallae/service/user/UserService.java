package kusitms.gallae.service.user;

import kusitms.gallae.domain.User;
import kusitms.gallae.dto.user.ManagerRegistratiorDto;
import kusitms.gallae.dto.user.UserRegistrationDto;
import kusitms.gallae.global.S3Service;
import kusitms.gallae.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@Service
public class UserService  {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3Service s3Service;


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
}

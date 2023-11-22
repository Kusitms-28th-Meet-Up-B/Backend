package kusitms.gallae.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationDto  {
    private String name;
    private String nickName;
    private String loginId;
    private String phoneNumber;
    private String email;
    private String loginPw;
    private LocalDate birth;
    private MultipartFile profileImage;

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

}

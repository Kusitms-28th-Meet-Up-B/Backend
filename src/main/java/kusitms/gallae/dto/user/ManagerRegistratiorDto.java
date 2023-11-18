package kusitms.gallae.dto.user;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ManagerRegistratiorDto {

    private String companyName;

    private String registNum;

    private String department;

    private String name;

    private String loginId;

    private String loginPw;

    private String email;

    private String phoneNum;

    private MultipartFile profileImage;

    public MultipartFile getProfileImage() {
        return profileImage;
    }

}

package kusitms.gallae.dto.user;


import kusitms.gallae.global.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class LoginResponse {
    private Long id;

    private String loginId;
    private String nickName;
    private String email;
    private String name;
    private String imageUrl;
    private String phoneNumber;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(Long id, String name,String loginId, String phoneNumber, String nickName, String email, String imageUrl, String accessToken, String refreshToken) {
        this.id = id;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}

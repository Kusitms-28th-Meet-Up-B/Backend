package kusitms.gallae.dto.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import kusitms.gallae.global.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class LoginResponse {
    private Long id;

    private String nickName;   //기업이면 이게 기관명

    private String registrationNum;

    private String department;  //담당 부서

    private String loginId;
    private String name;   //담당자 명
    private String phoneNumber;
    private String email;
    private String imageUrl;

    private Long point;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String role;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(Long id, String name,String department,String registrationNum,
                         String loginId, String phoneNumber, String nickName, String email, Long point, LocalDate birth,
                         String imageUrl, String accessToken, String refreshToken,  String role) {
        this.id = id;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.department = department;
        this.registrationNum = registrationNum;
        this.point = point;
        this.birth = birth;
    }

}

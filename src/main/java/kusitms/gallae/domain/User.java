package kusitms.gallae.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kusitms.gallae.global.Role;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName;   //기업이면 이게 기관명

    private String registrationNum;

    private String department;  //담당 부서

    private String loginId;

    private String loginPw;

    private String refreshToken;
    private String name;   //담당자 명
    private String phoneNumber;
    private String email;
    private String profileImageUrl;
    private Long point;
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserSignUpStatus signUpStatus;

    @CreationTimestamp
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime updatedAt;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    public enum UserSignUpStatus{
        MANAGER,
        USER,
        ADMIN
    }

    public void renewRefreshToken() {
        this.refreshToken = RandomStringUtils.randomAlphanumeric(32);
    }

    public Role getRole(){
        if(this.signUpStatus == UserSignUpStatus.USER ) {
            return Role.USER;
        }else if(this.signUpStatus == UserSignUpStatus.MANAGER) {
            return Role.MANAGER;
        }else if(this.signUpStatus == UserSignUpStatus.ADMIN) {
            return Role.ADMIN;
        }else{
            throw new IllegalArgumentException();
        }
    }
}

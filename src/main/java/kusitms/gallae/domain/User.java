package kusitms.gallae.domain;


import jakarta.persistence.*;
import kusitms.gallae.global.Role;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    private String nickName;

    private String loginId;

    private String loginPw;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserSignUpStatus signUpStatus;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
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

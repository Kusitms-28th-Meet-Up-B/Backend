package kusitms.gallae.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kusitms.gallae.global.Role;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String name;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> points = new ArrayList<>();
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

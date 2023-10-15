//package kusitms.gallae.domain;
//
//
//import jakarta.persistence.*;
//import kusitms.gallae.global.Role;
//import lombok.Builder;
//import lombok.ToString;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "user")
//@Builder
//@ToString
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(length = 45, nullable = false)
//    private String username;
//
//    @Column(length = 45, nullable = false)
//    private String loginId;
//
//    @Column(length = 500, nullable = false)
//    private String passward;
//
//    @Column(length = 500, nullable = false)
//    private String refreshToken;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, columnDefinition = "ENUM")
//    private Role role;
//
//    @CreationTimestamp
//    @Column
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column
//    private LocalDateTime updatedAt;
//
//    @Column
//    private LocalDateTime deletedAt;
//
//    public void renewRefreshToken() {
//        this.refreshToken = RandomStringUtils.randomAlphanumeric(32);
//    }
//}

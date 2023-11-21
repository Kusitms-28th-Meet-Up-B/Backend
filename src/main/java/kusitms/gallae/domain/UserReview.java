package kusitms.gallae.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userReview")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_Id", nullable = false)
    @ToString.Exclude
    private User user;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_Id", nullable = false)
    @ToString.Exclude
    private Review review;
}

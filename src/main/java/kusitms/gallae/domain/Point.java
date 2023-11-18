package kusitms.gallae.domain;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @Column(nullable = false)
    private Integer pointScore;

    @Column(nullable = false)
    private String pointActivity;

    @Column(nullable = false)
    private String pointCategory;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}


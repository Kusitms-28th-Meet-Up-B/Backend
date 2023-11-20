package kusitms.gallae.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private Integer pointScore;


    private String pointActivity;


    private String pointCategory;


    private LocalDate date;


    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
package kusitms.gallae.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "program")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_Id", nullable = false)
    @ToString.Exclude
    private User user;

    private String programName;

    private String location;

    private String description;

    private Float latitude;

    private Float longitude;

    private String hashTags;

    private LocalDate recruitStartDate;

    private LocalDate recruitEndDate;

    private LocalDate tripStartDate;

    private LocalDate tripEndDate;

    private LocalDate activeStartDate;

    private LocalDate activeEndDate;

    private String contact;

    private String contactNumber;

    private String programLink;

    private String programType;

    private String detailType;

    private String photoUrl;

    private Long programLike;

    private Long viewCount;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

    public enum ProgramStatus {
        TEMPSAVE,
        SAVE, // 저장
        FINISH, //마감
        DELETED
    }

    public void delete() {
        this.status = ProgramStatus.DELETED;
    }

    public void finish(){
        this.status = ProgramStatus.FINISH;
    }

    public void save(){
        this.status = ProgramStatus.SAVE;
    }

}

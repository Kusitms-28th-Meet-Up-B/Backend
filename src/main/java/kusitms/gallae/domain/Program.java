package kusitms.gallae.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    private LocalDateTime recruitStartDate;

    private LocalDateTime recruitEndDate;

    private LocalDateTime tripStartDate;

    private LocalDateTime tripEndDate;

    private LocalDateTime activeStartDate;

    private LocalDateTime activeEndDate;

    private String contact;

    private String contactNumber;

    private String programLink;

    private String programType;

    private String detailType;

    private String photoUrl;

    private Long programLike;

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
        ACTIVE,
        SAVE, // 임시 저장
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

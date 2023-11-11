package kusitms.gallae.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProgram is a Querydsl query type for Program
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProgram extends EntityPathBase<Program> {

    private static final long serialVersionUID = -42364266L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProgram program = new QProgram("program");

    public final DatePath<java.time.LocalDate> activeEndDate = createDate("activeEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> activeStartDate = createDate("activeStartDate", java.time.LocalDate.class);

    public final StringPath contact = createString("contact");

    public final StringPath contactNumber = createString("contactNumber");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final StringPath detailType = createString("detailType");

    public final StringPath hashTags = createString("hashTags");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Float> latitude = createNumber("latitude", Float.class);

    public final StringPath location = createString("location");

    public final NumberPath<Float> longitude = createNumber("longitude", Float.class);

    public final StringPath photoUrl = createString("photoUrl");

    public final NumberPath<Long> programLike = createNumber("programLike", Long.class);

    public final StringPath programLink = createString("programLink");

    public final StringPath programName = createString("programName");

    public final StringPath programType = createString("programType");

    public final DatePath<java.time.LocalDate> recruitEndDate = createDate("recruitEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> recruitStartDate = createDate("recruitStartDate", java.time.LocalDate.class);

    public final EnumPath<Program.ProgramStatus> status = createEnum("status", Program.ProgramStatus.class);

    public final DatePath<java.time.LocalDate> tripEndDate = createDate("tripEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> tripStartDate = createDate("tripStartDate", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUser user;

    public QProgram(String variable) {
        this(Program.class, forVariable(variable), INITS);
    }

    public QProgram(Path<? extends Program> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProgram(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProgram(PathMetadata metadata, PathInits inits) {
        this(Program.class, metadata, inits);
    }

    public QProgram(Class<? extends Program> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


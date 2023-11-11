package kusitms.gallae.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * Qarchive is a Querydsl query type for archive
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Qarchive extends EntityPathBase<archive> {

    private static final long serialVersionUID = -2145731340L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final Qarchive archive = new Qarchive("archive");

    public final StringPath body = createString("body");

    public final StringPath category = createString("category");

    public final StringPath fileUrl = createString("fileUrl");

    public final StringPath hashtag = createString("hashtag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final QUser user;

    public final StringPath writer = createString("writer");

    public Qarchive(String variable) {
        this(archive.class, forVariable(variable), INITS);
    }

    public Qarchive(Path<? extends archive> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public Qarchive(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public Qarchive(PathMetadata metadata, PathInits inits) {
        this(archive.class, metadata, inits);
    }

    public Qarchive(Class<? extends archive> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


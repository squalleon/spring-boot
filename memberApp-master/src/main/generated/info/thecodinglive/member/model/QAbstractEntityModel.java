package info.thecodinglive.member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractEntityModel is a Querydsl query type for AbstractEntityModel
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractEntityModel extends EntityPathBase<AbstractEntityModel> {

    private static final long serialVersionUID = 474567314L;

    public static final QAbstractEntityModel abstractEntityModel = new QAbstractEntityModel("abstractEntityModel");

    public final StringPath createdByUser = createString("createdByUser");

    public final DateTimePath<java.time.LocalDateTime> createdDateTime = createDateTime("createdDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDateTime = createDateTime("lastModifiedDateTime", java.time.LocalDateTime.class);

    public final StringPath modifiedByUser = createString("modifiedByUser");

    public QAbstractEntityModel(String variable) {
        super(AbstractEntityModel.class, forVariable(variable));
    }

    public QAbstractEntityModel(Path<? extends AbstractEntityModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractEntityModel(PathMetadata metadata) {
        super(AbstractEntityModel.class, metadata);
    }

}


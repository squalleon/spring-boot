package info.thecodinglive.member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRememberMeToken is a Querydsl query type for RememberMeToken
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRememberMeToken extends EntityPathBase<RememberMeToken> {

    private static final long serialVersionUID = -1819123198L;

    public static final QRememberMeToken rememberMeToken = new QRememberMeToken("rememberMeToken");

    public final DateTimePath<java.util.Date> lastUsed = createDateTime("lastUsed", java.util.Date.class);

    public final StringPath series = createString("series");

    public final StringPath token = createString("token");

    public final StringPath username = createString("username");

    public QRememberMeToken(String variable) {
        super(RememberMeToken.class, forVariable(variable));
    }

    public QRememberMeToken(Path<? extends RememberMeToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRememberMeToken(PathMetadata metadata) {
        super(RememberMeToken.class, metadata);
    }

}


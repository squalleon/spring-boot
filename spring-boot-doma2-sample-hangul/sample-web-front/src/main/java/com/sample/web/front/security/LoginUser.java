package com.sample.web.front.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.sample.domain.dto.user.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -5891919297179603893L;
//    private static final long serialVersionUID = 7416966553094941923L;
    /**
     * serialVersionUID
     * front 띄우나서 다시 admin 띄워서 url 접속하면 역직열화 오류 때문에 500 error 가 뜬다.
     * org.eclipse.jetty.server.HttpChannel     : /admin/ org.springframework.core.NestedIOException: Failed to deserialize object type; nested exception is java.lang.ClassNotFoundException: com.sample.web.front.security.LoginUser
     * 역직렬화 버전업을 하게 되면 문제가 야기된다고 해서
     * serialVersionUID 값을 없애보고 7416966553094941923L 로 바꿔도 봤지만 해결되지 않았다.
     * 재부팅하고 나면 admin 접속이 정상적으로 된다.
     */

    /**
     * 생성자
     * 
     * @param user
     * @param authorities
     */
    public LoginUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(String.valueOf(user.getEmail()), user.getPassword(), authorities);
    }
}

package com.example.yblog.config.auth;

import com.example.yblog.model.YUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayDeque;
import java.util.Collection;
// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션 저장소에 저장을 해준다

@Data
public class PrincipalDetail implements UserDetails {
    private YUser yUser; // 컴포지션(상속대신 품고있는것 )

    // 이거 컴포지션으로 안담아주면 기본 계정을 던져준다
    public PrincipalDetail(YUser yUser){
        this.yUser = yUser;
    }

    @Override
    public String getPassword() {
        return yUser.getPassword();
    }

    @Override
    public String getUsername() {
        return yUser.getUsername();
    }

    // 계정이 만료되지 않았는지 리턴한다(true 잠기지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true; // true 안잠겨있다 라는 뜻
    }
    // 계정이 만료되지 않았는지 리턴한다(true 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정활성화가 완료 되어있는지 아닌지 (true를 해야 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collecters = new ArrayDeque<>(); // 어레이리스트는 콜렉션 타입
        collecters.add(()-> {return "ROLE_USER";});
        return collecters;
    }

}

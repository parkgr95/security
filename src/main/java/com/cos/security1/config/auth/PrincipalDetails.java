package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;
import lombok.Data;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어줌 (Security ContextHolder)
//오브젝트 타입 => Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 됨
//User 오브젝트 타입 => UserDetails 타입 객체

//Security Session => Authentication => UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails {

    private User user; //콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    //해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole(); //ROLE_USER
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //true: 계정 만료 안됨
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //true: 계정 잠김 안됨
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //true: 계정 패스워드 만료 안됨
    }

    @Override
    public boolean isEnabled() {
        //우리 사이트!! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하기로 함
        //현재시간 - 로그인 시간 => 1년을 초과하면 return false;
        return true;
    }
}
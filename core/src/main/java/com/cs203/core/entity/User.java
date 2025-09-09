package com.cs203.core.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    public transient UserEntity userEntity;

    public User(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(userEntity.getIsAdmin() ? "Admin" : "Trader")
        );
    }

    @Override
    public String getPassword() {
        return userEntity.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userEntity.getId().toString();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }
}

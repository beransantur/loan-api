package com.beransantur.loanapi.common.security;

import com.beransantur.loanapi.repository.entity.AuthorityEntity;
import com.beransantur.loanapi.repository.entity.RoleEntity;
import com.beransantur.loanapi.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(user -> {
                    List<SimpleGrantedAuthority> authorities = new LinkedList<>();
                    for (RoleEntity roleEntity : user.getRoles()) {
                        for(AuthorityEntity authorityEntity : roleEntity.getAuthorities()) {
                            authorities.add(new SimpleGrantedAuthority(authorityEntity.getName()));
                        }
                    }

                    return User.builder().username(user.getUsername())
                            .authorities(authorities)
                            .password(user.getPassword())
                            .build();

                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

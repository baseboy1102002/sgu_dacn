package com.sgu.schedulerApp.config.security;

import com.sgu.schedulerApp.entity.Role;
import com.sgu.schedulerApp.entity.User;
import com.sgu.schedulerApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("user not found: "+username);
        Role role = user.getRole();
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setUser(user);
        myUserDetails.setAuthorities(authorities);
        return myUserDetails;
    }
}

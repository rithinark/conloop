package com.mock.conloop.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mock.conloop.model.Role;
import com.mock.conloop.model.User;
import com.mock.conloop.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(
                "User with email '%s' Not Found", email)));
        return new User(user.getUserId(), user.getUsername(),
                user.getEmail(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    public Set<GrantedAuthority> getAuthorities(List<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

}

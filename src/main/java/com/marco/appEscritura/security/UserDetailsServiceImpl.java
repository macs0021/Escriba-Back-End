package com.marco.appEscritura.security;

import com.marco.appEscritura.entity.User;
import com.marco.appEscritura.repository.UserRepository;
import com.marco.appEscritura.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.print("BUSCANDO NOMBRE");
        User user = userRepository.findOneByUsername(username).orElseThrow(()->new UsernameNotFoundException("The user does not exist"));
        return new UserDetailsImpl(user);
    }
}

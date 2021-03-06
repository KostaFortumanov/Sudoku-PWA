package com.awd.sudoku.service;

import com.awd.sudoku.models.AppUser;
import com.awd.sudoku.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    public void save(AppUser user) {
        userRepository.save(user);
    }

    public void saveAll(List<AppUser> users) {
        userRepository.saveAll(users);
    }
}

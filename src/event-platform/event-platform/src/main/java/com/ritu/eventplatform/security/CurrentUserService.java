package com.ritu.eventplatform.security;

import com.ritu.eventplatform.entity.User;
import com.ritu.eventplatform.exception.UserNotFoundException;
import com.ritu.eventplatform.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }
}
package com.danven.web_library.service;

import com.danven.web_library.domain.user.User;
import com.danven.web_library.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Service class that implements UserDetailsService to provide custom user details retrieval.
 */
@Service
public class CustomerServiceImpl implements CustomerService{

    private final UserRepository userRepository;

    /**
     * Constructs a new CustomUserDetailsService instance.
     *
     * @param userRepository The repository for accessing User entities.
     */
    public CustomerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a User entity by their email.
     *
     * @param email The email of the user to retrieve.
     * @return The User entity associated with the given email.
     * @throws UsernameNotFoundException If no user is found with the provided email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}

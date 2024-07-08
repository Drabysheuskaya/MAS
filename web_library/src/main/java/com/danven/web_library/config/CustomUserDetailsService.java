package com.danven.web_library.config;

import com.danven.web_library.domain.user.User;
import com.danven.web_library.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service class that implements UserDetailsService to provide custom user details retrieval.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerService customerService;

    /**
     * Constructs a new CustomUserDetailsService instance.
     *
     * @param customerService The service for accessing User entities.
     */
    public CustomUserDetailsService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves UserDetails based on the user's email.
     *
     * @param email The email of the user to load.
     * @return UserDetails for the user.
     * @throws UsernameNotFoundException If no user is found with the given email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = customerService.getUserByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Retrieves the currently logged-in user based on the authenticated context.
     *
     * @return The User entity representing the currently logged-in user.
     * @throws UsernameNotFoundException If no user is found for the authenticated context.
     */
    public User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerService.getUserByEmail(email);
    }

}


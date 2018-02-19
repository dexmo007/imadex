package com.dexmohq.imadex.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("No user with that nickname exists");
        }
        return user;
    }

    private final Pattern pattern = Pattern.compile(".*index: ([a-z]*) dup key.*; nested exception.*");

    public User registerUser(String username, String email, String password) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        final User newUser = new User();
        newUser.setEnabled(true);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setAuthorities(Collections.singleton(Authority.USER));
        try {
            return userRepository.save(newUser);
        } catch (DuplicateKeyException e) {
            final Matcher m = pattern.matcher(e.getMessage());
            if (m.matches()) {
                switch (m.group(1)) {
                    case "username":
                        throw new UsernameAlreadyExistsException();
                    case "email":
                        throw new EmailAlreadyExistsException();
                    default:
                        throw new InternalError(e);
                }
            }
            throw new InternalError(e);
        }
    }

}

package ru.education.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.education.springboot.services.PersonDetailService;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final PersonDetailService personDetailService;
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    @Autowired
    public AuthProviderImpl(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    // Authentication authentication - тут лежат креды
    // Возвращаем Authentication
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // приходит из формы
        String username = authentication.getName();

        UserDetails personDetails = personDetailService.loadUserByUsername(username);

        // приходит из формы
        String password = authentication.getCredentials().toString();

        if (!password.equals(personDetails.getPassword()))
            throw new BadCredentialsException("Incorrect credentials");

        return new UsernamePasswordAuthenticationToken(personDetails, password,
                Collections.emptyList());
    }

    // для какого сценария используется этот auth Provider
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}

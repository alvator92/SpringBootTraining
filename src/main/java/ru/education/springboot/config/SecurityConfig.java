package ru.education.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.education.springboot.security.AuthProviderImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProviderImpl provider;

    @Autowired
    public SecurityConfig(AuthProviderImpl provider) {
        this.provider = provider;
    }

    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);

    }
}

package se.inera.intyg.intygsbestallning.web.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import se.inera.intyg.intygsbestallning.web.auth.BaseSecurityConfig;

@Configuration
@Profile("dev-security")
@Order(2)
public class DevSecurityConfig extends BaseSecurityConfig {

    @Autowired
    public AuthenticationManager authenticationManagerBean;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests().antMatchers("/h2-console/**").permitAll().and()
                .authorizeRequests().anyRequest().permitAll();

        http
                .headers().frameOptions().disable();

        http
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
                .authenticationProvider(ibAuthenticationProvider());
    }
}
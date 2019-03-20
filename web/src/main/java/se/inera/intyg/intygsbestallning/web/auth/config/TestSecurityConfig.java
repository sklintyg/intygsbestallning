package se.inera.intyg.intygsbestallning.web.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import se.inera.intyg.intygsbestallning.web.auth.BaseSecurityConfig;

@Configuration
@Profile("ib-security-test")
@Order(1)
public class TestSecurityConfig extends BaseSecurityConfig {

    @Autowired
    public AuthenticationManager authenticationManagerBean;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
            .httpBasic()
                .authenticationEntryPoint(samlEntryPoint())
            .and()
                .requestMatcher(samlRequestMatcher())
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/#/app")
            .and()
                .csrf().disable()
                .headers().frameOptions().disable()
            .and()
                .requestCache()
                .requestCache(requestCache())
            .and()
                .sessionManagement()
                .sessionAuthenticationStrategy(registerSessionAuthenticationStrategy())
            .and()
                .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/**").fullyAuthenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
                .authenticationProvider(samlAuthenticationProvider())
                .authenticationProvider(ibAuthenticationProvider());
    }
}
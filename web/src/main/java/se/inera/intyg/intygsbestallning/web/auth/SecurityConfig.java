/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.web.auth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.common.collect.Lists;

import se.inera.intyg.infra.security.exception.HsaServiceException;
import se.inera.intyg.infra.security.exception.MissingMedarbetaruppdragException;
import se.inera.intyg.intygsbestallning.web.auth.exceptions.MissingIBSystemRoleException;
import se.inera.intyg.intygsbestallning.web.auth.fake.FakeAuthenticationFilter;
import se.inera.intyg.intygsbestallning.web.auth.fake.FakeAuthenticationProvider;
import se.inera.intyg.intygsbestallning.web.auth.service.IntygsbestallningUserDetailsService;

@EnableWebSecurity
@PropertySource("file:${credentials.file}")
@ComponentScan({"se.inera.intyg.infra.security.authorities", "org.springframework.security.saml"})
public class SecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean, DisposableBean {

    private static final String DEV_PROFILE = "dev-security";
    private static final String TEST_PROFILE = "ib-security-test";

    private MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;

    @Autowired
    public SecurityConfig(IntygsbestallningUserDetailsService userDetailsService, ApplicationContext context) {
        this.userDetailsService = userDetailsService;
        this.context = context;
    }
    private final IntygsbestallningUserDetailsService userDetailsService;

    private final ApplicationContext context;

    private List<String> profiles;

    private void init() {
        this.multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
        this.profiles =  Arrays.asList(context.getEnvironment().getActiveProfiles());
    }

    private void shutdown() {
        this.multiThreadedHttpConnectionManager.shutdown();
    }

    @Value("${config.folder}")
    private String configFolder;

    @Value("${sakerhetstjanst.saml.keystore.file}")
    private Resource keyStoreFile;

    @Value("${sakerhetstjanst.saml.keystore.alias}")
    private String keystoreAlias;

    @Value("${sakerhetstjanst.saml.keystore.password}")
    private String keystorePassword;

    // Initialization of the velocity engine
    @Bean
    public VelocityEngine velocityEngine() {
        return VelocityFactory.getEngine();
    }

    // XML parser pool needed for OpenSAML parsing
    @Bean(initMethod = "initialize")
    public StaticBasicParserPool parserPool() {
        return new StaticBasicParserPool();
    }

    @Bean(name = "parserPoolHolder")
    public ParserPoolHolder parserPoolHolder() {
        return new ParserPoolHolder();
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient(this.multiThreadedHttpConnectionManager);
    }

    // Initialization of OpenSAML library
    @Bean
    public static SAMLBootstrap samlBootstrap() {
        return new SAMLBootstrap();
    }

    @Bean
    public static RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new LoggingSessionRegistryImpl());
    }

    @Bean
    public static ExceptionMappingAuthenticationFailureHandler failureHandler() {
        ExceptionMappingAuthenticationFailureHandler failureHandler = new ExceptionMappingAuthenticationFailureHandler();
        HashMap<String, String> exceptionMappings = new HashMap<>();

        exceptionMappings.put(BadCredentialsException.class.getName(), "/index.html?reason=login.failed");
        exceptionMappings.put(MissingMedarbetaruppdragException.class.getName(),
                "/index.html?reason=login.medarbetaruppdrag");
        exceptionMappings.put(MissingIBSystemRoleException.class.getName(),
                "/index.html?reason=login.no-hsa-ib-systemrole");
        exceptionMappings.put(HsaServiceException.class.getName(), "/index.html?reason=login.hsaerror");

        failureHandler.setExceptionMappings(exceptionMappings);
        failureHandler.setDefaultFailureUrl("/index.html?reason=login.failed");
        return failureHandler;
    }

    @Bean
    public OrRequestMatcher samlRequestMatcher() {
        ArrayList<RequestMatcher> matchers = Lists.newArrayList(
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/#/app"),
                new AntPathRequestMatcher("/saml/**"),
                new AntPathRequestMatcher("/maillink/**"));
        return new OrRequestMatcher(matchers);
    }

    /* =====================================================
     *
     * SAML-stuff (prod, ib-security-test, ib-security-prod)
     *
     ===================================================== */
    // SAML 2.0 WebSSO Assertion Consumer

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public JKSKeyManager keyManager() {
        Map<String, String> map = new HashMap<>();
        map.put(keystoreAlias, keystorePassword);
        return new JKSKeyManager(keyStoreFile, keystorePassword, map, keystoreAlias);
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }

    // SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    // SAML 2.0 Web SSO profile
    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public WebSSOProfile webSSOprofile() {
        return new WebSSOProfileImpl();
    }

    // SAML 2.0 Holder-of-Key Web SSO profile
    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SingleLogoutProfile logoutprofile() {
        return new SingleLogoutProfileImpl();
    }

    // Processor bindings
    private ArtifactResolutionProfile artifactResolutionProfile() {
        final ArtifactResolutionProfileImpl artifactResolutionProfile =
                new ArtifactResolutionProfileImpl(httpClient());
        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
        return artifactResolutionProfile;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public HTTPArtifactBinding artifactBinding(ParserPool parserPool, VelocityEngine velocityEngine) {
        return new HTTPArtifactBinding(parserPool, velocityEngine, artifactResolutionProfile());
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public HTTPSOAP11Binding soapBinding() {
        return new HTTPSOAP11Binding(parserPool());
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public HTTPRedirectDeflateBinding redirectDeflateBinding() {
        return new HTTPRedirectDeflateBinding(parserPool());
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLBinding httpPostBinding() {
        return new HTTPPostBinding(parserPool(), velocityEngine());
    }

    // Processor
    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLProcessorImpl processor() {
        Collection<SAMLBinding> bindings = new ArrayList<>();
        bindings.add(redirectDeflateBinding());
        bindings.add(httpPostBinding());
        bindings.add(artifactBinding(parserPool(), velocityEngine()));
        return new SAMLProcessorImpl(bindings);
    }

    // Logger for SAML messages and events
    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLDefaultLogger samlLogger() {
        SAMLDefaultLogger logger = new SAMLDefaultLogger();
        logger.setLogErrors(true);
        logger.setLogMessages(true);
        return logger;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public static HttpSessionRequestCache requestCache() {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setRequestMatcher(new RegexRequestMatcher("\\/maillink\\/.*", null));
        return requestCache;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public IntygsbestallningSAMLEntryPoint samlEntryPoint() {
        IntygsbestallningSAMLEntryPoint entryPoint = new IntygsbestallningSAMLEntryPoint();
        WebSSOProfileOptions options = new WebSSOProfileOptions();
        options.setIncludeScoping(false);
        options.setAuthnContexts(Lists.newArrayList("urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient"));
        entryPoint.setDefaultProfileOptions(options);
        return entryPoint;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLContextProviderLB contextProvider() {
        SAMLContextProviderLB contextProvider = new SAMLContextProviderLB();
        contextProvider.setScheme("https");
        contextProvider.setServerName("${ib.server}");
        contextProvider.setServerPort(443);
        contextProvider.setIncludeServerPortInRequestURL(false);
        contextProvider.setContextPath("/");
        contextProvider.setKeyManager(keyManager());
        return contextProvider;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public FilterChainProxy samlFilter() throws Exception {
        List<SecurityFilterChain> chains = new ArrayList<>();
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/login/**"), samlEntryPoint()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/logout/**"), samlLogoutFilter()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/SSO/**"), samlWebSSOProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/SingleLogout/**"), samlLogoutProcessingFilter()));
        return new FilterChainProxy(chains);
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SecurityContextLogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
        successLogoutHandler.setDefaultTargetUrl("/");
        successLogoutHandler.setAlwaysUseDefaultTargetUrl(true);
        return successLogoutHandler;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[] { logoutHandler() },
                new LogoutHandler[] { logoutHandler() });
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public static SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/");
        handler.setAlwaysUseDefaultTargetUrl(true);
        handler.setRequestCache(requestCache());
        return handler;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
        SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
        samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
        samlWebSSOProcessingFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
        samlWebSSOProcessingFilter.setAuthenticationFailureHandler(failureHandler());
        return samlWebSSOProcessingFilter;
    }

    // SAML Authentication Provider responsible for validating of received SAML
    // messages
    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public SAMLAuthenticationProvider samlAuthenticationProvider() {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setUserDetails(userDetailsService);
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        return samlAuthenticationProvider;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public CachingMetadataManager metadata() throws MetadataProviderException, IOException {
        return new CachingMetadataManager(Lists.newArrayList(
                extendedMetadataDelegateSP(), extendedMetadataDelegateIdp()));
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public ExtendedMetadataDelegate extendedMetadataDelegateSP() throws MetadataProviderException {
        File spMetadataFile = new File(configFolder + "/sp-sakerhetstjanst.xml");
        FilesystemMetadataProvider spMetadataProvider = new FilesystemMetadataProvider(spMetadataFile);
        spMetadataProvider.setParserPool(parserPool());
        ExtendedMetadata extendedMetadata = generateExtendedMetadataSP();

        ExtendedMetadataDelegate delegate = new ExtendedMetadataDelegate(spMetadataProvider, extendedMetadata);
        delegate.setMetadataTrustCheck(true);

        return delegate;
    }

    @NotNull
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    private ExtendedMetadata generateExtendedMetadataSP() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setAlias("defaultAlias");
        extendedMetadata.setLocal(true);
        extendedMetadata.setSslSecurityProfile("metaiop");
        extendedMetadata.setSecurityProfile("metaiop");
        extendedMetadata.setSignMetadata(true);
        extendedMetadata.setSigningKey(keystoreAlias);
        extendedMetadata.setEncryptionKey(keystoreAlias);
        extendedMetadata.setRequireArtifactResolveSigned(true);
        extendedMetadata.setRequireLogoutRequestSigned(false);
        extendedMetadata.setRequireLogoutResponseSigned(false);
        return extendedMetadata;
    }

    @Bean
    @Profile({"prod", "ib-security-test", "ib-security-prod"})
    public ExtendedMetadataDelegate extendedMetadataDelegateIdp() throws MetadataProviderException {
        File spMetadataFile = new File(configFolder + "/idp-sakerhetstjanst.xml");
        FilesystemMetadataProvider spMetadataProvider = new FilesystemMetadataProvider(spMetadataFile);
        spMetadataProvider.setParserPool(parserPool());
        ExtendedMetadata extendedMetadata = generateExtendedMetadataIdp();

        ExtendedMetadataDelegate delegate = new ExtendedMetadataDelegate(spMetadataProvider, extendedMetadata);
        delegate.setMetadataTrustCheck(true);

        return delegate;
    }

    @NotNull
    private ExtendedMetadata generateExtendedMetadataIdp() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setAlias("defaultAlias");
        return extendedMetadata;
    }

    /* ===================================================
    *
    * Dev-security & ib-security-test
    *
    =====================================================*/
    @Bean
    @Profile({ "dev-security", "ib-security-test"})
    public FakeAuthenticationProvider fakeAuthenticationProvider() {
        FakeAuthenticationProvider fakeAuthenticationProvider = new FakeAuthenticationProvider();
        fakeAuthenticationProvider.setUserDetails(userDetailsService);
        return fakeAuthenticationProvider;
    }

    @Bean
    @Profile({ "dev-security", "ib-security-test"})
    public FakeAuthenticationFilter fakeAuthenticationFilter() throws Exception {
        FakeAuthenticationFilter fakeAuthenticationFilter = new FakeAuthenticationFilter();
        fakeAuthenticationFilter.setAuthenticationManager(authenticationManager());
        fakeAuthenticationFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        fakeAuthenticationFilter.setAuthenticationSuccessHandler(fakeSuccessHandler());
        fakeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler());
        return fakeAuthenticationFilter;
    }

    @Bean
    @Profile({ "dev-security", "ib-security-test"})
    public SimpleUrlAuthenticationSuccessHandler fakeSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/index.html");
        handler.setAlwaysUseDefaultTargetUrl(true);
        return handler;
    }

    @Override
    public void afterPropertiesSet() {
        init();
    }

    @Override
    public void destroy() {
        shutdown();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // These should always be permitted
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/welcome-assets/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/components/**").permitAll()
                .antMatchers("/saml/web/**").permitAll()
                .antMatchers("/saml2/web/**").permitAll()
                .antMatchers("/services/**").permitAll()
                .antMatchers("/api/config/**").permitAll();

        if (profiles.contains(DEV_PROFILE)) {
            http
                    .authorizeRequests()
                    .antMatchers("/welcome.html").permitAll()
                    .antMatchers("/api/stub/**").permitAll()
                    .antMatchers("/api/test/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll();

            // @formatter:off
            http
                .authorizeRequests().antMatchers("/**").fullyAuthenticated()
                .and().httpBasic()
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                    .csrf().disable()
                    .logout()
                    .invalidateHttpSession(true)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/welcome.html")
                .and()
                    .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                    .sessionManagement()
                    .sessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
            // @formatter:on
        }

        if (profiles.contains(TEST_PROFILE)) {
            // Unauthenticated requests matching samlRequestMatcher will be sent to saml login flow
            // @formatter:off
            http
                .requestMatcher(samlRequestMatcher())
                    .authorizeRequests()
                    .antMatchers("/fake").permitAll()
                    .antMatchers("/**").fullyAuthenticated()
                .and()
                    .httpBasic()
                    .authenticationEntryPoint(samlEntryPoint())
                .and()
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .authorizeRequests().antMatchers("/**").fullyAuthenticated()
                .and()
                    .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)
                    .sessionManagement()
                    .sessionAuthenticationStrategy(registerSessionAuthenticationStrategy())
                .and()
                    .requestCache()
                    .requestCache(requestCache());

            //Other unauthenticated requests will be returned as http status 403.
            //This will allow frontend to act correctly on ajax requests
            http
                .httpBasic()
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                    .csrf().disable()
                    .logout()
                    .invalidateHttpSession(true)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/#/app")
                .and()
                    .authorizeRequests()
                    .antMatchers("/fake").permitAll()
                    .antMatchers("/**").fullyAuthenticated()
                .and()
                    .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(fakeAuthenticationFilter(), BasicAuthenticationFilter.class)
                    .sessionManagement()
                    .sessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
            // @formatter:on
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        if (profiles.contains(DEV_PROFILE)) {
            auth
                    .authenticationProvider(fakeAuthenticationProvider());
        }

        if (profiles.contains(TEST_PROFILE)) {
            auth
                    .authenticationProvider(samlAuthenticationProvider())
                    .authenticationProvider(fakeAuthenticationProvider());
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

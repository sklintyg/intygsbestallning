/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.intygsbestallning.web.controller.AppConfigController.APPCONFIG_REQUEST_MAPPING;
import static se.inera.intyg.intygsbestallning.web.controller.RequestErrorController.IB_SPRING_SEC_ERROR_CONTROLLER_PATH;
import static se.inera.intyg.intygsbestallning.web.controller.SessionStatController.SESSION_STAT_REQUEST_MAPPING;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.storage.EmptyStorageFactory;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import se.inera.intyg.intygsbestallning.web.auth.fake.FakeAuthenticationFilter;
import se.inera.intyg.intygsbestallning.web.auth.fake.FakeAuthenticationProvider;
import se.inera.intyg.intygsbestallning.web.auth.service.IntygsbestallningUserDetailsService;

@EnableWebSecurity
@ComponentScan({"se.inera.intyg.infra.security.authorities", "org.springframework.security.saml"})
public class SecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean, DisposableBean {

    private static final String FAKE_PROFILE = "fake";
    private static final String SAML_PROFILE = "saml";
    public static final String FAKE_LOGOUT_URL = "/logout";
    public static final String FAKE_LOGIN_URL = "/welcome.html";
    public static final String SAML_LOGOUT_URL = "/saml/logout";
    public static final String SAML_LOGIN_URL = "/saml/login";
    public static final String SUCCESSFUL_LOGOUT_REDIRECT_URL = "/#/loggedout/m";

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
        this.profiles = Arrays.asList(context.getEnvironment().getActiveProfiles());
    }

    private void shutdown() {
        this.multiThreadedHttpConnectionManager.shutdown();
    }

    @Value("${inera.saml.sp.metadata}")
    private String spMetadataLocation;

    @Value("${inera.saml.idp.metadata}")
    private String idpMetadataLocation;

    @Value("${inera.saml.keystore.file}")
    private Resource keyStoreFile;

    @Value("${inera.saml.keystore.alias}")
    private String keystoreAlias;

    @Value("${inera.saml.keystore.password}")
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
    public static AuthenticationFailureHandler failureHandler() {
        // Let our custom RequestErrorController handle any authentication failures
        return new ForwardAuthenticationFailureHandler(IB_SPRING_SEC_ERROR_CONTROLLER_PATH);
    }

    @Bean
    public static OrRequestMatcher loginRequestMatcher() {
        ArrayList<RequestMatcher> matchers = Lists.newArrayList(
                // new AntPathRequestMatcher("/saml/**"),
                new AntPathRequestMatcher("/maillink/**", null));
        return new OrRequestMatcher(matchers);
    }

    /*
     * =====================================================
     *
     * SAML-stuff
     *
     * =====================================================
     */
    // SAML 2.0 WebSSO Assertion Consumer

    @Bean
    @Profile({SAML_PROFILE})
    public JKSKeyManager keyManager() {
        Map<String, String> map = new HashMap<>();
        map.put(keystoreAlias, keystorePassword);
        return new JKSKeyManager(keyStoreFile, keystorePassword, map, keystoreAlias);
    }

    @Bean
    @Profile({SAML_PROFILE})
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }

    // SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
    @Bean
    @Profile({SAML_PROFILE})
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    // SAML 2.0 Web SSO profile
    @Bean
    @Profile({SAML_PROFILE})
    public WebSSOProfile webSSOprofile() {
        return new WebSSOProfileImpl();
    }

    // SAML 2.0 Holder-of-Key Web SSO profile
    @Bean
    @Profile({SAML_PROFILE})
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SingleLogoutProfile logoutprofile() {
        return new SingleLogoutProfileImpl();
    }

    @Bean
    @Profile({SAML_PROFILE})
    public HTTPSOAP11Binding soapBinding() {
        return new HTTPSOAP11Binding(parserPool());
    }

    @Bean
    @Profile({SAML_PROFILE})
    public HTTPRedirectDeflateBinding redirectDeflateBinding() {
        return new HTTPRedirectDeflateBinding(parserPool());
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SAMLBinding httpPostBinding() {
        return new HTTPPostBinding(parserPool(), velocityEngine());
    }

    // Processor
    @Bean
    @Profile({SAML_PROFILE})
    public SAMLProcessorImpl processor() {
        Collection<SAMLBinding> bindings = new ArrayList<>();
        bindings.add(redirectDeflateBinding());
        bindings.add(httpPostBinding());
        return new SAMLProcessorImpl(bindings);
    }

    // Logger for SAML messages and events
    @Bean
    @Profile({SAML_PROFILE})
    public SAMLDefaultLogger samlLogger() {
        SAMLDefaultLogger logger = new SAMLDefaultLogger();
        logger.setLogErrors(true);
        logger.setLogMessages(true);
        return logger;
    }

    @Bean
    @Profile({SAML_PROFILE, FAKE_PROFILE})
    public static HttpSessionRequestCache requestCache() {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setRequestMatcher(loginRequestMatcher());
        return requestCache;
    }

    @Bean
    @Profile({SAML_PROFILE})
    public IntygsbestallningSAMLEntryPoint samlEntryPoint() {
        IntygsbestallningSAMLEntryPoint entryPoint = new IntygsbestallningSAMLEntryPoint();
        WebSSOProfileOptions options = new WebSSOProfileOptions();
        options.setIncludeScoping(false);
        options.setAuthnContexts(Lists.newArrayList("http://id.sambi.se/loa/loa3"));
        entryPoint.setDefaultProfileOptions(options);
        return entryPoint;
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SAMLContextProvider contextProvider() {
        SAMLContextProviderImpl contextProvier = new SAMLContextProviderImpl();
        contextProvier.setKeyManager(keyManager());
        contextProvier.setStorageFactory(new EmptyStorageFactory());
        return contextProvier;
    }

    /*
     * Keeping this since we might need it in OpenShift.
     * Should be removed or used before production.
     */
    // @Bean
    // @Profile({ "prod", "ib-security-test", "ib-security-prod" })
    // public SAMLContextProviderLB contextProvider() {
    // SAMLContextProviderLB contextProvider = new SAMLContextProviderLB();
    // contextProvider.setScheme("https");
    // contextProvider.setServerName("${ib.server}");
    // contextProvider.setServerPort(443);
    // contextProvider.setIncludeServerPortInRequestURL(false);
    // contextProvider.setContextPath("/");
    // contextProvider.setKeyManager(keyManager());
    // return contextProvider;
    // }

    @Bean
    @Profile({SAML_PROFILE})
    public FilterChainProxy samlFilter() throws Exception {
        List<SecurityFilterChain> chains = new ArrayList<>();
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher(SAML_LOGIN_URL + "/**"), samlEntryPoint()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher(SAML_LOGOUT_URL + "/**"), samlLogoutFilter()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/SSO/**"), samlWebSSOProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/SingleLogout/**"), samlLogoutProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(
                new AntPathRequestMatcher("/saml/metadata/**"), samlMetadataDisplayFilter()));
        return new FilterChainProxy(chains);
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SecurityContextLogoutHandler logoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
        successLogoutHandler.setDefaultTargetUrl(SUCCESSFUL_LOGOUT_REDIRECT_URL);
        successLogoutHandler.setAlwaysUseDefaultTargetUrl(true);
        return successLogoutHandler;
    }

    @Bean
    @Profile({SAML_PROFILE})
    public MetadataDisplayFilter samlMetadataDisplayFilter() {
        return new MetadataDisplayFilter();
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
    }

    @Bean
    @Profile({SAML_PROFILE})
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[]{logoutHandler()},
                new LogoutHandler[]{logoutHandler()});
    }

    @Bean
    @Profile({SAML_PROFILE})
    public static SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/");
        handler.setAlwaysUseDefaultTargetUrl(false);
        handler.setRequestCache(requestCache());
        return handler;
    }

    @Bean
    @Profile({SAML_PROFILE})
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
    @Profile({SAML_PROFILE})
    public SAMLAuthenticationProvider samlAuthenticationProvider() {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setUserDetails(userDetailsService);
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        return samlAuthenticationProvider;
    }

    @Bean
    @Profile({SAML_PROFILE})
    public CachingMetadataManager metadata() throws MetadataProviderException, IOException {
        return new CachingMetadataManager(Lists.newArrayList(
                extendedMetadataDelegateSP(), extendedMetadataDelegateIdp()));
    }

    @Bean
    @Profile({SAML_PROFILE})
    public ExtendedMetadataDelegate extendedMetadataDelegateSP() throws MetadataProviderException {
        File spMetadataFile = new File(spMetadataLocation);
        FilesystemMetadataProvider spMetadataProvider = new FilesystemMetadataProvider(spMetadataFile);
        spMetadataProvider.setParserPool(parserPool());
        ExtendedMetadata extendedMetadata = generateExtendedMetadataSP();

        ExtendedMetadataDelegate delegate = new ExtendedMetadataDelegate(spMetadataProvider, extendedMetadata);
        delegate.setMetadataTrustCheck(true);

        return delegate;
    }

    @NotNull
    private ExtendedMetadata generateExtendedMetadataSP() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setAlias("defaultAlias");
        extendedMetadata.setLocal(true);
        extendedMetadata.setSslSecurityProfile("metaiop");
        extendedMetadata.setSecurityProfile("metaiop");
        extendedMetadata.setSignMetadata(true);
        extendedMetadata.setSigningKey(keystoreAlias);
        extendedMetadata.setEncryptionKey(keystoreAlias);
        // extendedMetadata.setRequireArtifactResolveSigned(true);
        extendedMetadata.setRequireLogoutRequestSigned(false);
        extendedMetadata.setRequireLogoutResponseSigned(false);
        return extendedMetadata;
    }

    @Bean
    @Profile({SAML_PROFILE})
    public ExtendedMetadataDelegate extendedMetadataDelegateIdp() throws MetadataProviderException {
        File idpMetadataFile = new File(idpMetadataLocation);
        FilesystemMetadataProvider idpMetadataProvider = new FilesystemMetadataProvider(idpMetadataFile);
        idpMetadataProvider.setParserPool(parserPool());
        ExtendedMetadata extendedMetadata = generateExtendedMetadataIdp();

        ExtendedMetadataDelegate delegate = new ExtendedMetadataDelegate(idpMetadataProvider, extendedMetadata);
        delegate.setMetadataTrustCheck(false);

        return delegate;
    }

    @NotNull
    private ExtendedMetadata generateExtendedMetadataIdp() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setAlias("defaultAlias");
        return extendedMetadata;
    }

    @Bean
    @Profile({FAKE_PROFILE})
    public FakeAuthenticationProvider fakeAuthenticationProvider() {
        FakeAuthenticationProvider fakeAuthenticationProvider = new FakeAuthenticationProvider();
        fakeAuthenticationProvider.setUserDetails(userDetailsService);
        return fakeAuthenticationProvider;
    }

    @Bean
    @Profile({FAKE_PROFILE})
    public FakeAuthenticationFilter fakeAuthenticationFilter() throws Exception {
        FakeAuthenticationFilter fakeAuthenticationFilter = new FakeAuthenticationFilter();
        fakeAuthenticationFilter.setAuthenticationManager(authenticationManager());
        fakeAuthenticationFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        fakeAuthenticationFilter.setAuthenticationSuccessHandler(fakeSuccessHandler());
        fakeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler());
        return fakeAuthenticationFilter;
    }

    @Bean
    @Profile({FAKE_PROFILE})
    public SavedRequestAwareAuthenticationSuccessHandler fakeSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/");
        handler.setAlwaysUseDefaultTargetUrl(false);
        handler.setRequestCache(requestCache());
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
    public void configure(WebSecurity web) throws Exception {
        // All static client resources could be completely ignored by Spring Security.
        // This is also needed for a IE11 font loading bug where Springs Security default no-cache headers
        // will stop IE from loading fonts properly.
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // These should always be permitted
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/welcome-assets/**").permitAll()
                .antMatchers("/version.html").permitAll()
                .antMatchers("/public-api/version").permitAll()
                .antMatchers("/version-assets/**").permitAll()
                .antMatchers("/favicon*").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/components/**").permitAll()
                .antMatchers("/services/**").permitAll()
                .antMatchers(APPCONFIG_REQUEST_MAPPING).permitAll()
                .antMatchers(SESSION_STAT_REQUEST_MAPPING + "/**").permitAll();

        if (profiles.contains(FAKE_PROFILE) && profiles.contains(SAML_PROFILE)) {
            configureSamlWithFake(http);
        } else if (profiles.contains(FAKE_PROFILE)) {
            configureFake(http);
        } else if (profiles.contains(SAML_PROFILE)) {
            configureSaml(http);
        } else {
            configureNone(http);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        if (profiles.contains(FAKE_PROFILE)) {
            auth.authenticationProvider(fakeAuthenticationProvider());
        }

        if (profiles.contains(SAML_PROFILE)) {
            auth.authenticationProvider(samlAuthenticationProvider());
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint loginUrlauthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(FAKE_LOGIN_URL);
    }

    private void configureFake(HttpSecurity http) throws Exception {
        configureFakePermitAllPaths(http);

        // Unauthenticated requests matching loginRequestMatcher will be sent to fake login flow
        http
                .authorizeRequests().antMatchers("/**").fullyAuthenticated()

                .and().exceptionHandling()
                .defaultAuthenticationEntryPointFor(loginUrlauthenticationEntryPoint(), loginRequestMatcher())
                .defaultAuthenticationEntryPointFor(new Http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE)

                .and().csrf().disable().headers().frameOptions().disable()

                .and().logout().invalidateHttpSession(true).logoutUrl(FAKE_LOGOUT_URL).logoutSuccessUrl(FAKE_LOGIN_URL)

                .and().addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)

                .sessionManagement().sessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
    }

    private void configureSaml(HttpSecurity http) throws Exception {
        // Unauthenticated requests matching loginRequestMatcher will be sent to saml login flow
        http
                .authorizeRequests().antMatchers("/**").fullyAuthenticated()

                .and().exceptionHandling()
                .defaultAuthenticationEntryPointFor(samlEntryPoint(), loginRequestMatcher())
                .defaultAuthenticationEntryPointFor(new Http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE)

                .and().csrf().disable().headers().frameOptions().disable()

                .and().addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)

                .sessionManagement().sessionAuthenticationStrategy(registerSessionAuthenticationStrategy());

        // Handled by SAML
        http.logout().disable();
    }

    private void configureSamlWithFake(HttpSecurity http) throws Exception {
        configureFakePermitAllPaths(http);

        // Unauthenticated requests matching loginRequestMatcher will be sent to saml login flow
        http
                .authorizeRequests().antMatchers("/**").fullyAuthenticated()

                .and().exceptionHandling()
                .defaultAuthenticationEntryPointFor(samlEntryPoint(), loginRequestMatcher())
                .defaultAuthenticationEntryPointFor(new Http403ForbiddenEntryPoint(), AnyRequestMatcher.INSTANCE)

                .and().csrf().disable().headers().frameOptions().disable()

                .and().logout().invalidateHttpSession(true).logoutUrl(FAKE_LOGOUT_URL).logoutSuccessUrl(FAKE_LOGIN_URL)

                .and().addFilterAfter(samlFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)

                .sessionManagement().sessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
    }

    private void configureNone(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/**").denyAll()

                .and().httpBasic().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }

    private void configureFakePermitAllPaths(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(FAKE_LOGIN_URL).permitAll()
                .antMatchers("/api/stub/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .antMatchers("/h2-console/**").permitAll();
    }
}

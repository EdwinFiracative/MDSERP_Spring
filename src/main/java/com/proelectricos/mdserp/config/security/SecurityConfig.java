package com.proelectricos.mdserp.config.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class SecurityConfig {

    public static final String AUTHORITY_ADMIN = "ROLE_ADMIN_MDS_ERP";
    //public static final String AUTHORITY_USER = "ROLE_OFFLINE_ACCESS";

    public static final String AUTHORITY_USER = "ROLE_CON_VIEW_PROYTABLEROS";

    @NonNull
    private final ToolUserAuthenticationEntryPointHandler toolUserAuthenticationEntryPointHandler;
    @NonNull
    private final ToolUserAccessDeniedHandler toolUserAccessDeniedHandler;
    @NonNull
    private final SecurityConfigProperties securityConfigProperties;


    @Bean
    public SecurityFilterChain resourceServerFilterChain(final HttpSecurity http,
                                                         HandlerMappingIntrospector introspect) throws Exception {

        String[] allowedPaths = securityConfigProperties.allowedPaths().toArray(String[]::new);


        http.cors(Customizer.withDefaults()) // this is added for connection from UI
                .headers(headers -> headers.frameOptions(
                        FrameOptionsConfig::sameOrigin)) // this line is added to h2 UI
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(allowedPaths).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // permit connexions for CORS preflight requests
                        .anyRequest().hasAnyAuthority(AUTHORITY_USER))
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                                .accessDeniedHandler(toolUserAccessDeniedHandler)
                                .authenticationEntryPoint(toolUserAuthenticationEntryPointHandler))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(toolUserAuthenticationEntryPointHandler))
                // sonar can cause issue
                .csrf(AbstractHttpConfigurer::disable)
                // sonar can cause issue
                .sessionManagement(
                        sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .logout(AbstractHttpConfigurer::disable) // default spring logout feature
                .formLogin(AbstractHttpConfigurer::disable);// default spring login feature
        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        final JwtAndGrantedRolesConverter rolesConverter = new JwtAndGrantedRolesConverter();
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(rolesConverter);
        return jwtAuthenticationConverter;
    }


}
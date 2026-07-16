package campus_care.config;

import campus_care.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final OAuthSuccessHandler successHandler;

    private final JwtFilter jwtFilter;
    @Value("${frontend.url}")
    private String frontendUrl;
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        /* ================= GOOGLE ACCOUNT SELECT ================= */

        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository,
                        "/oauth2/authorization"
                );

        OAuth2AuthorizationRequestResolver customResolver =
                new OAuth2AuthorizationRequestResolver() {

                    @Override
                    public OAuth2AuthorizationRequest resolve(
                            jakarta.servlet.http.HttpServletRequest request
                    ) {

                        return customizeRequest(
                                defaultResolver.resolve(request)
                        );
                    }

                    @Override
                    public OAuth2AuthorizationRequest resolve(
                            jakarta.servlet.http.HttpServletRequest request,
                            String clientRegistrationId
                    ) {

                        return customizeRequest(
                                defaultResolver.resolve(
                                        request,
                                        clientRegistrationId
                                )
                        );
                    }

                    private OAuth2AuthorizationRequest customizeRequest(
                            OAuth2AuthorizationRequest request
                    ) {

                        if (request == null) {
                            return null;
                        }

                        return OAuth2AuthorizationRequest
                                .from(request)
                                .additionalParameters(params ->
                                        params.put(
                                                "prompt",
                                                "select_account"
                                        )
                                )
                                .build();
                    }
                };

        http

                /* ================= CORS ================= */

                .cors(Customizer.withDefaults())

                /* ================= CSRF ================= */

                .csrf(csrf -> csrf.disable())

                /* ================= FORM LOGIN ================= */

                .formLogin(form -> form.disable())

                .httpBasic(httpBasic -> httpBasic.disable())

                /* ================= SESSION ================= */

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                /* ================= SECURITY HEADERS ================= */

                .headers(headers -> headers
                        .frameOptions(frame ->
                                frame.sameOrigin()
                        )
                )

                /* ================= ROUTES ================= */

                .authorizeHttpRequests(auth -> auth

                        /* ---------- PUBLIC ROUTES ---------- */

                        .requestMatchers(

                                "/auth/register",
                                "/auth/verify-otp",
                                "/auth/verify-college-id",
                                "/auth/login",
                                "/admin/login",
                                "/oauth2/**",
                                "/login/**",

                                "/error",
                                "/favicon.ico",

                                "/uploads/**"

                        ).permitAll()

                        /* ---------- ADMIN ROUTES ---------- */

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        /* ---------- STUDENT ROUTES ---------- */

                        .requestMatchers("/student/**")
                        .hasRole("STUDENT")

                        /* ---------- EVERYTHING ELSE ---------- */

                        .anyRequest().authenticated()
                )

                /* ================= EXCEPTION HANDLING ================= */

                .exceptionHandling(ex -> ex

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            "Unauthorized"
                                    );
                                }
                        )

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.sendError(
                                            HttpServletResponse.SC_FORBIDDEN,
                                            "Access Denied"
                                    );
                                }
                        )
                )

                /* ================= GOOGLE OAUTH ================= */

                .oauth2Login(oauth -> oauth

                        .authorizationEndpoint(endpoint ->
                                endpoint.authorizationRequestResolver(
                                        customResolver
                                )
                        )

                        .successHandler(successHandler)

                        .failureHandler((req, res, ex) -> {
                            ex.printStackTrace();

                            res.getWriter().println(ex.getMessage());
                        })
                )

                /* ================= LOGOUT ================= */

                .logout(logout -> logout

                        .logoutSuccessHandler(
                                (request, response, authentication) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_OK
                                    );
                                }
                        )
                );

        /* ================= JWT FILTER ================= */

        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
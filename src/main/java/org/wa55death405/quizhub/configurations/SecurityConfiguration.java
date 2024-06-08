package org.wa55death405.quizhub.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/*
    * This configuration class is responsible for
    * setting up the security of the application
    * mainly using Spring Security
    * it also sets up the CORS policy
 */

@Configuration
public class SecurityConfiguration {
    @Value("${app.CORS.ALLOWED-ORIGINS}")
    private String ALLOWED_ORIGINS;
    @Value("${app.CORS.ALLOWED-METHODS}")
    private String ALLOWED_METHODS;
    @Value("${app.CORS.ALLOWED-HEADERS}")
    private String ALLOWED_HEADERS;
    @Value("${app.CORS.EXPOSED-HEADERS}")
    private String EXPOSED_HEADERS;
    @Value("${app.CORS.ALLOW-CREDENTIALS}")
    private String ALLOW_CREDENTIALS;
    @Value("${app.CORS.MAX-AGE}")
    private String MAX_AGE;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorize) -> authorize
////                .requestMatchers("/resource/**").hasAuthority("USER")
//                .anyRequest().permitAll()
//        );
////                .formLogin(Customizer.withDefaults());
//        return http.build();
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        List<String> allowedOrigins = Arrays.asList(ALLOWED_ORIGINS.split(","));
        List<String> allowedMethods = Arrays.asList(ALLOWED_METHODS.split(","));
        List<String> allowedHeaders = Arrays.asList(ALLOWED_HEADERS.split(","));
        List<String> exposedHeaders = Arrays.asList(EXPOSED_HEADERS.split(","));
        boolean allowCredentials = Boolean.parseBoolean(ALLOW_CREDENTIALS);
        long maxAge = Long.parseLong(MAX_AGE);

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins(allowedOrigins.toArray(new String[0]))
                        .allowedMethods(allowedMethods.toArray(new String[0]))
                        .allowedHeaders(allowedHeaders.toArray(new String[0]))
                        .exposedHeaders(exposedHeaders.toArray(new String[0]))
                        .allowCredentials(allowCredentials)
                        .maxAge(maxAge);
            }
        };
    }
}

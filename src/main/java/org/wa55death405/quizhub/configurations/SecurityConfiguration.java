package org.wa55death405.quizhub.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    * This configuration class is responsible for
    * setting up the security of the application
    * using Spring Security
 */

@Configuration
public class SecurityConfiguration {


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
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8080","http://localhost:4200");
            }
        };
    }
}

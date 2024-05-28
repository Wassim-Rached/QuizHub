package org.wa55death405.quizhub.configurations;

import com.github.javafaker.Faker;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    this class is the main configuration class
    it contains all the beans that are used in the application,
    but they are too small to be in a separate configuration class
 */

@Configuration
public class MainConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }
}

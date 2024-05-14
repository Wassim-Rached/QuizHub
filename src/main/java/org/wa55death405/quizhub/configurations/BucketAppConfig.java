package org.wa55death405.quizhub.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wa55death405.quizhub.interceptors.RateLimitInterceptor;

@Configuration
@RequiredArgsConstructor
@Profile({"prod", "exp"})
public class BucketAppConfig implements WebMvcConfigurer {
    private final RateLimitInterceptor interceptor;

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/**");
    }

}

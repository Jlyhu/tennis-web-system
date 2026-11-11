package com.fsteni58.tennis.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IdentidadConfig implements WebMvcConfigurer {

    private final ControlDeIdentidad controlDeIdentidad;

    public IdentidadConfig(ControlDeIdentidad controlDeIdentidad) {
        this.controlDeIdentidad = controlDeIdentidad;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controlDeIdentidad)
                .addPathPatterns("/carrito/**", "/tickets/**", "/inventario/**", "/productos/**");
    }
}
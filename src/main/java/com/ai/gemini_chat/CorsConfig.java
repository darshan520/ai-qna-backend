package com.ai.gemini_chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/qna/**")
                .allowedOrigins(
                        "http://localhost:5174",
                        "https://ai-qna-frontend.vercel.app",
                        "https://ai-qna-frontend-git-main-darshan520s-projects.vercel.app",
                        "https://ai-qna-frontend-qdt6emdy7-darshan520s-projects.vercel.app"
                )
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
                .allowedHeaders("*")
                .allowCredentials(false);

    }

}

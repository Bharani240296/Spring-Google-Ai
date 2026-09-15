package com.springai.BootAi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootAiApplication {
//spring.ai.openai.api-key=${GROQ_API_KEY}
//
//spring.ai.openai.base-url=https://api.groq.com/openai/v1
//
//spring.ai.openai.chat.options.model=llama-3.3-70b-versatile
	public static void main(String[] args) {
		SpringApplication.run(BootAiApplication.class, args);
	}

}

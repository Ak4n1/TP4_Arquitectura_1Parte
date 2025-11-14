package com.tudai.monopatines.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway Application
 * 
 * <p>Gateway principal del sistema de monopatines. Enruta las peticiones
 * a los diferentes microservicios del sistema.</p>

 */
@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}


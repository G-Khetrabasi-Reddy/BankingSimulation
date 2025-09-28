package org.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

// Defines the base URL for your entire API
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Tells Jersey to scan the 'org.bank.controller' package for resources
        packages("org.bank.controller");
        register(JacksonFeature.class);
        register(new ObjectMapper().registerModule(new JavaTimeModule()));
    }
}

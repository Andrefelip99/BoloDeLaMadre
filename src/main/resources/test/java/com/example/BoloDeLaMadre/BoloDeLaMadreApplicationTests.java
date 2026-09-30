package com.example.BoloDeLaMadre;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BoloDeLaMadreApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void apiRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void seededAdminCanAccessApiWithBasicAuthentication() throws Exception {
        String credentials = java.util.Base64.getEncoder().encodeToString("admin:admin123".getBytes());
        mockMvc.perform(get("/api/categorias").header("Authorization", "Basic " + credentials))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }
}

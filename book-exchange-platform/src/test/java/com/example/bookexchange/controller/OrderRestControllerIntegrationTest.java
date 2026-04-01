package com.example.bookexchange.controller;

import com.example.bookexchange.dto.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderRestControllerIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createOrder_asAdmin_shouldReturnCreated() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setBookId(1L);
        request.setBuyerId(2L);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createOrder_asUser_shouldBeForbidden() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setBookId(1L);
        request.setBuyerId(2L);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getOrders_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateOrderStatus_asUser_shouldBeForbidden() throws Exception {
        mockMvc.perform(patch("/api/orders/1/status").param("status", "APPROVED"))
            .andExpect(status().isForbidden());
    }
}

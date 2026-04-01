package com.example.bookexchange.controller;

import com.example.bookexchange.dto.BookRequest;
import com.example.bookexchange.entity.BookStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookRestControllerIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void getBooks_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_shouldReturnCreated() throws Exception {
        BookRequest request = new BookRequest();
        request.setTitle("New Book");
        request.setAuthor("Author");
        request.setPrice(BigDecimal.valueOf(100));
        request.setStatus(BookStatus.AVAILABLE);
        request.setSellerId(1L);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteBook_asUser_shouldBeForbidden() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
            .andExpect(status().isForbidden());
    }
}

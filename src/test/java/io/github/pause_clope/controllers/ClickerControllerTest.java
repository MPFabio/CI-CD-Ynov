package io.github.pause_clope.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pause_clope.dto.SaveRequest;
import io.github.pause_clope.entities.UserData;
import io.github.pause_clope.services.ClickerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    value = ClickerController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@ActiveProfiles("test")
class ClickerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClickerService clickerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPostClicker_ReturnsSaved() throws Exception {
        SaveRequest request = new SaveRequest();
        request.setClicks(42L); // assuming SaveRequest has a clicks field

        mockMvc.perform(post("/clicker/testUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));

        Mockito.verify(clickerService).postClicker(eq("testUser"), any(SaveRequest.class));
    }

    @Test
    void testGetClicker_UserFound() throws Exception {
        UserData userData = new UserData();
        userData.setClicks(100L);

        Mockito.when(clickerService.getByNickname("testUser"))
                .thenReturn(Optional.of(userData));

        mockMvc.perform(get("/clicker/testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void testGetClicker_UserNotFound() throws Exception {
        Mockito.when(clickerService.getByNickname("unknownUser"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/clicker/unknownUser"))
                .andExpect(status().isNotFound());
    }
}

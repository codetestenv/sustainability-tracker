package com.sustainabilitytracker.sustainabilitytracker;

import com.sustainabilitytracker.sustainabilitytracker.controllers.EmissionController;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.EmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmissionController.class)
class EmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmissionService emissionService;

    @Test
    void submitEmission_ShouldReturn201() throws Exception {
        EmissionResponse response = new EmissionResponse();
        response.setId(1L);

        when(emissionService.submitEmission(any())).thenReturn(response);

        String json = """
                {
                    "companyId": 1,
                    "departmentId": 1,
                    "co2Amount": 1250.5,
                    "recordedAt": "2025-06-01"
                }
                """;

        mockMvc.perform(post("/api/v1/emissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void approveEmission_ShouldReturn200() throws Exception {
        when(emissionService.approveEmission(1L)).thenReturn(new EmissionResponse());

        mockMvc.perform(put("/api/v1/emissions/1/approve"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectEmission_ShouldReturn200() throws Exception {
        when(emissionService.rejectEmission(1L, "Invalid data")).thenReturn(new EmissionResponse());

        String json = """
                {
                    "reason": "Invalid data"
                }
                """;

        mockMvc.perform(put("/api/v1/emissions/1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void unauthorizedAccess_ShouldReturn403() throws Exception {
        when(emissionService.approveEmission(anyLong()))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("Access denied"));

        mockMvc.perform(put("/api/v1/emissions/1/approve"))
                .andExpect(status().isForbidden());
    }
}
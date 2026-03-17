package com.nanochat.taxengine.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaxEngineControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldComputeTaxFromApi() throws Exception {
        String body = """
                {
                  "taxpayerId": "PAN123",
                  "age": 35,
                  "residencyStatus": "RESIDENT",
                  "regime": "OLD",
                  "financialYear": "2024-25",
                  "assessmentYear": "2025-26",
                  "facts": {
                    "salaryIncome": 1200000,
                    "hraReceived": 240000,
                    "rentPaid": 300000,
                    "section80cInvestment": 180000,
                    "tds": 90000
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/tax/compute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grossIncome").value(1200000))
                .andExpect(jsonPath("$.deductions").value(150000))
                .andExpect(jsonPath("$.exemptions").value(180000))
                .andExpect(jsonPath("$.finalTaxPayable").value(85000.00));
    }
}

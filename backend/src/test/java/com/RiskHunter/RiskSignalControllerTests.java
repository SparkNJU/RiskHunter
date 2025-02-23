package com.RiskHunter;/*
 * @date 02/23 18:54
 */


import com.RiskHunter.DTO.RiskSignalQueryDTO;
import com.RiskHunter.po.RiskSignal;
import com.RiskHunter.service.RiskSignalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
        import static org.junit.jupiter.api.Assertions.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RiskSignalControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RiskSignalService riskSignalService;

    private RiskSignal testSignal;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testSignal = new RiskSignal();
        testSignal.setAnalysis("Initial Test Analysis");
        testSignal.setEmp(1.0);
        testSignal.setExchangeRate(7.2);

        riskSignalService.save(testSignal);
    }

    @Test
    void createRiskSignal() throws Exception {
        RiskSignal newSignal = new RiskSignal();
        newSignal.setAnalysis("New Risk Analysis");
        newSignal.setEmp(1.5);
        newSignal.setExchangeRate(6.8);

        mockMvc.perform(post("/api/risk-signals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSignal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.analysis").value("New Risk Analysis"));

        // 验证数据库
        RiskSignal saved = riskSignalService.getById(newSignal.getId());
        assertNotNull(saved);
        assertEquals(1.5, saved.getEmp());
    }

    @Test
    void listRiskSignalsWithDefaultParams() throws Exception {
        mockMvc.perform(get("/api/risk-signals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.current").value(1))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.records", hasSize(greaterThan(0))));
    }

    @Test
    void listRiskSignalsWithTimeRange() throws Exception {
        String startTime = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME);
        String endTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        mockMvc.perform(get("/api/risk-signals")
                        .param("startTime", startTime)
                        .param("endTime", endTime)
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void updateRiskSignal() throws Exception {
        testSignal.setAnalysis("Updated Analysis");

        mockMvc.perform(put("/api/risk-signals/{id}", testSignal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.analysis").value("Updated Analysis"));

        RiskSignal updated = riskSignalService.getById(testSignal.getId());
        assertEquals("Updated Analysis", updated.getAnalysis());
    }

    @Test
    void deleteRiskSignal() throws Exception {
        mockMvc.perform(delete("/api/risk-signals/{id}", testSignal.getId()))
                .andExpect(status().isOk());

        RiskSignal deleted = riskSignalService.getById(testSignal.getId());
        assertNull(deleted);
    }

    @Test
    void advancedSearch() throws Exception {
        RiskSignalQueryDTO queryDTO = new RiskSignalQueryDTO();
        queryDTO.setKeyword("Test");
        queryDTO.setMinEmp(0.5);
        queryDTO.setMaxEmp(1.5);
        queryDTO.setPage(1);
        queryDTO.setSize(10);

        mockMvc.perform(post("/api/risk-signals/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records", hasSize(greaterThan(0))));
    }

    @Test
    void advancedSearchWithDateRange() throws Exception {
        RiskSignalQueryDTO queryDTO = new RiskSignalQueryDTO();
        queryDTO.setStartTime(LocalDateTime.now().minusHours(1));
        queryDTO.setEndTime(LocalDateTime.now());
        queryDTO.setMinExchangeRate(7.0);
        queryDTO.setPage(1);
        queryDTO.setSize(10);

        mockMvc.perform(post("/api/risk-signals/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records[0].exchangeRate", greaterThanOrEqualTo(7.0)));
    }
}
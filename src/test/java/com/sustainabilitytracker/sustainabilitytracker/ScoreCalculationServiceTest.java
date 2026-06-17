package com.sustainabilitytracker.sustainabilitytracker;

import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import com.sustainabilitytracker.sustainabilitytracker.repositories.EmissionRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.EnergyRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.WaterRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.WasteRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.SocialRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.GovernanceRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.SustainabilityScoreRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.ScoreCalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreCalculationServiceTest {

    @Mock
    private EmissionRepository emissionRepository;
    @Mock
    private EnergyRepository energyRepository;
    @Mock
    private WaterRepository waterRepository;
    @Mock
    private WasteRepository wasteRepository;
    @Mock
    private SocialRepository socialRepository;
    @Mock
    private GovernanceRepository governanceRepository;
    @Mock
    private SustainabilityScoreRepository scoreRepository;

    @InjectMocks
    private ScoreCalculationService scoreCalculationService;

    @Test
    void calculateAndSaveScore_WithAllData_ShouldReturnCorrectScore() {
        when(emissionRepository.getTotalCo2(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(850));
        when(energyRepository.getTotalKwh(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(5000));
        when(energyRepository.getTotalRenewableKwh(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(3500));
        when(waterRepository.getTotalConsumedLiters(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(15000));
        when(waterRepository.getTotalRecycledLiters(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(9000));
        when(wasteRepository.getTotalKg(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(2000));
        when(wasteRepository.getTotalRecycledKg(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(1200));

        SustainabilityScore score = scoreCalculationService
                .calculateAndSaveScore(1L, LocalDate.now().minusMonths(1), LocalDate.now());

        assertNotNull(score);
        assertNotNull(score.getTotalScore());
        assertNotNull(score.getGrade());
    }

    @Test
    void determineGrade_Score95_ReturnsA() {
        String grade = scoreCalculationService.determineGrade(95.0);
        assertEquals("A", grade);
    }

    @Test
    void determineGrade_Score30_ReturnsF() {
        String grade = scoreCalculationService.determineGrade(30.0);
        assertEquals("F", grade);
    }
}

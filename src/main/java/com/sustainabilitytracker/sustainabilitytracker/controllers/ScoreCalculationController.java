package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import com.sustainabilitytracker.sustainabilitytracker.services.ScoreCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreCalculationController {

    private final ScoreCalculationService scoreCalculationService;

    @PostMapping("/calculate")
    public ResponseEntity<SustainabilityScore> calculateAndSaveScore(
            @RequestParam Long companyId,
            @RequestParam LocalDate periodStart,
            @RequestParam LocalDate periodEnd) {

        SustainabilityScore score = scoreCalculationService
                .calculateAndSaveScore(companyId, periodStart, periodEnd);

        return ResponseEntity.ok(score);
    }

    @GetMapping("/latest/{companyId}")
    public ResponseEntity<SustainabilityScore> getLatestScore(@PathVariable Long companyId) {
        return ResponseEntity.ok(scoreCalculationService.getLatestScore(companyId));
    }

    @GetMapping("/history/{companyId}")
    public ResponseEntity<List<SustainabilityScore>> getScoreHistory(@PathVariable Long companyId) {
        return ResponseEntity.ok(scoreCalculationService.getScoreHistory(companyId));
    }
}

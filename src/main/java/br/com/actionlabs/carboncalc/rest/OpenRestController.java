package br.com.actionlabs.carboncalc.rest;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.service.CarbonCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open")
@RequiredArgsConstructor
@Slf4j
public class OpenRestController {

    private final CarbonCalculatorService calculatorService;

    @PostMapping("start-calc")
    public ResponseEntity<StartCalcResponseDTO> startCalculation(
            @RequestBody StartCalcRequestDTO request) {

        log.info("Starting carbon calculation - user: {}", request.getName());

        StartCalcResponseDTO response = calculatorService.startCalculation(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("info")
    public ResponseEntity<UpdateCalcInfoResponseDTO> updateInfo(
            @RequestBody UpdateCalcInfoRequestDTO request) {

        log.info("Receiving consumption data - ID: {}", request.getId());

        UpdateCalcInfoResponseDTO response = calculatorService.updateInformation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("result/{id}")
    public ResponseEntity<CarbonCalculationResultDTO> getResult(@PathVariable String id) {

        log.info("Fetching final result - ID: {}", id);

        CarbonCalculationResultDTO response = calculatorService.getResult(id);

        return ResponseEntity.ok(response);
    }
}
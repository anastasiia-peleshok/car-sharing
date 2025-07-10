package com.example.carsharing.controller;

import com.example.carsharing.dto.feature.FeatureDto;
import com.example.carsharing.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController {
    private final FeatureService featureService;

    /**
     * Get all features.
     */
    @Operation(summary = "Get all features", description = "Retrieve a list of all available features.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<FeatureDto> getFeatures(Pageable pageable) {
        return featureService.getFeatures(pageable);
    }

    /**
     * Get feature details by ID.
     */
    @Operation(summary = "Get feature by ID", description = "Retrieve details of a specific feature by its ID.")
    @GetMapping("/{featureId}")
    @ResponseStatus(HttpStatus.OK)
    public FeatureDto getFeatureById(@PathVariable UUID featureId) {
        return featureService.getFeatureById(featureId);
    }

    /**
     * Create a new feature.
     */
    @Operation(summary = "Create feature", description = "Create and save a new feature.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeatureDto saveFeature(@RequestBody FeatureDto featureDto) {
        return featureService.saveFeature(featureDto);
    }

    /**
     * Update an existing feature.
     */
    @Operation(summary = "Update feature", description = "Update an existing feature by its ID.")
    @PutMapping("/{featureId}")
    @ResponseStatus(HttpStatus.OK)
    public FeatureDto updateFeature(@PathVariable UUID featureId, @RequestBody FeatureDto featureDto) {
        return featureService.updateFeature(featureId, featureDto);
    }

    /**
     * Delete a feature by ID.
     */
    @Operation(summary = "Delete feature", description = "Delete a specific feature by its ID.")
    @DeleteMapping("/{featureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeature(@PathVariable UUID featureId) {
        featureService.deleteFeatureById(featureId);
    }
}

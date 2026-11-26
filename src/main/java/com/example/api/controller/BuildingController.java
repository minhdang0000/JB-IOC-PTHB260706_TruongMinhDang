package com.example.api.controller;

import com.example.api.model.dto.ApiResponse;
import com.example.api.model.dto.BuildingCreateRequest;
import com.example.api.model.dto.BuildingUpdateRequest;
import com.example.api.model.entity.Building;
import com.example.api.service.BuildingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buildings")
public class BuildingController {
    private final BuildingService buildingService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Building>>> getAllBuilding(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Building> building = buildingService.getAllBuildings(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Building>>builder()
                .success(true)
                .message("Successfully retrieved buildings")
                .data(building)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse<Page<Building>>> searchBuildingsByBuildingName(
            @RequestParam(required = false, defaultValue = "") String buildingName,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable){
        Page<Building> building = buildingService.searchBuildingsByBuildingName(buildingName, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Building>>builder()
                .success(true)
                .message("Successfully searched buildings by building name")
                .data(building)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/search/status")
    public ResponseEntity<ApiResponse<Page<Building>>> searchBuildingsByStatus(
            @RequestParam(required = false) Short status,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable){
        Page<Building> building = buildingService.searchBuildingsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<Building>>builder()
                .success(true)
                .message("Successfully searched buildings by building status")
                .data(building)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Building>> createBuilding(@Valid @ModelAttribute BuildingCreateRequest request) {
        Building createdBuilding = buildingService.createBuilding(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<Building>builder()
                        .success(true)
                        .message("Created building successfully")
                        .data(createdBuilding)
                        .errors(null)
                        .httpStatus(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Building>> updateBuilding(@PathVariable Long id, @Valid @ModelAttribute BuildingUpdateRequest request) {
        Building updatedBuilding = buildingService.updateBuilding(id, request);
        return ResponseEntity.ok(ApiResponse.<Building>builder()
                .success(true)
                .message("Updated building successfully")
                .data(updatedBuilding)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Building>> updateBuildingStatus(@PathVariable Long id, @RequestParam Short status) {
        Building updatedBuilding = buildingService.updateBuildingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.<Building>builder()
                .success(true)
                .message("Updated building status successfully")
                .data(updatedBuilding)
                .errors(null)
                .httpStatus(HttpStatus.OK.value())
                .build());
    }
}

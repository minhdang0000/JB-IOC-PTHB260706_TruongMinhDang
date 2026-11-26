package com.example.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BuildingUpdateRequest {
    @NotBlank
    @Size(max = 100)
    private String buildingName;
    @NotNull
    @Positive
    private Double buildingArea;
    @NotBlank
    @Size(max = 10)
    private String areaUnit;
    @NotNull
    private LocalDate startDate;
    @NotNull
    @Positive
    private Integer time;
    @NotBlank
    @Size(max = 10)
    private String timeUnit;
    private MultipartFile designFile;
    @NotBlank
    @Size(max = 255)
    private String content;
}

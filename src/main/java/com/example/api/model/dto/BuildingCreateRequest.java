package com.example.api.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BuildingCreateRequest {
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
    @NotNull
    private MultipartFile designFile;
    @NotBlank
    @Size(max = 255)
    private String content;
    @Min(value = 0)
    @Max(value = 2)
    private Short status;
}

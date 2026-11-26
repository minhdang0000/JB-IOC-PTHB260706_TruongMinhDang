package com.example.api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "buildings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "building_name", length = 100, unique = true, nullable = false)
    private String buildingName;
    @Column(name = "building_area", nullable = false)
    private Double buildingArea;
    @Column(name = "area_unit", length = 10, nullable = false)
    private String areaUnit;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private Integer time;
    @Column(length = 10, nullable = false)
    private String timeUnit;
    @Column(length = 255 ,nullable = false)
    private String design;
    @Column(length = 255 ,nullable = false)
    private String content;
    private Short status;

    @PrePersist
    public void prePersist(){
        if(this.status == null){
            this.status = 1;
        }
    }
}

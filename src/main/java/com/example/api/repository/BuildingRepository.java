package com.example.api.repository;

import com.example.api.model.entity.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    boolean existsByBuildingNameIgnoreCase(String buildingName);
    boolean existsByBuildingNameIgnoreCaseAndIdNot(String buildingName, Long id);
    Page<Building> findByBuildingNameContainingIgnoreCase(String buildingName, Pageable pageable);
    Page<Building> findByStatus(Short status, Pageable pageable);
}

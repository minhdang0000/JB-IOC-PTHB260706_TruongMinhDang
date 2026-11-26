package com.example.api.service;

import com.example.api.exception.DuplicateResourceException;
import com.example.api.exception.InvalidStatusTransitionException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.model.dto.BuildingCreateRequest;
import com.example.api.model.dto.BuildingUpdateRequest;
import com.example.api.model.entity.Building;
import com.example.api.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private static final int PAGE_SIZE = 5;
    @Value("${app.upload.dir:uploads/designs}")
    private String uploadDir;

    public Page<Building> getAllBuildings(Pageable pageable) {
        int pageNumber = 0;
        if(pageable != null && pageable.getPageNumber() > 0){
            pageNumber = pageable.getPageNumber();
        }
        Pageable fixedPageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id").ascending());
        return buildingRepository.findAll(fixedPageable);
    }

    public Page<Building> searchBuildingsByBuildingName(String buildingName, Pageable pageable) {
        int pageNumber = 0;
        if(pageable != null && pageable.getPageNumber() > 0){
            pageNumber = pageable.getPageNumber();
        }
        Pageable fixedPageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id").ascending());
        Page<Building> buildingPage;
        if(buildingName != null && !buildingName.trim().isEmpty()){
            buildingPage = buildingRepository.findByBuildingNameContainingIgnoreCase(buildingName.trim(), fixedPageable);
        }else{
            buildingPage = buildingRepository.findAll(fixedPageable);
        }
        return buildingPage;
    }

    public Page<Building> searchBuildingsByStatus(Short status, Pageable pageable) {
        int pageNumber = 0;
        if(pageable != null && pageable.getPageNumber() > 0){
            pageNumber = pageable.getPageNumber();
        }
        Pageable fixedPageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("id").ascending());
        Page<Building> buildingPage;
        if(status != null){
            if(status != 0 && status != 1 && status != 2){
                throw new IllegalArgumentException("Status is not valid");
            }
            buildingPage = buildingRepository.findByStatus(status, fixedPageable);
        }else{
            buildingPage = buildingRepository.findAll(fixedPageable);
        }
        return buildingPage;
    }

    public Building createBuilding(BuildingCreateRequest request){
        if(request.getStatus() != null){
            if(request.getStatus() != 0 && request.getStatus() != 1 && request.getStatus() != 2){
                throw new IllegalArgumentException("Status is not valid");
            }
        }
        String normalizedName = request.getBuildingName().trim();
        if(buildingRepository.existsByBuildingNameIgnoreCase(normalizedName)){
            throw new DuplicateResourceException("Building already exists");
        }
        String designPath = saveDesignImage(request.getDesignFile());
        Building building = Building.builder()
                .buildingName(normalizedName)
                .buildingArea(request.getBuildingArea())
                .areaUnit(request.getAreaUnit())
                .startDate(request.getStartDate())
                .time(request.getTime())
                .timeUnit(request.getTimeUnit())
                .design(designPath)
                .content(request.getContent())
                .status(request.getStatus())
                .build();
        return buildingRepository.save(building);
    }

    public Building updateBuilding(Long id, BuildingUpdateRequest request){
        Building building = buildingRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Building not found"));
        String normalizedName = request.getBuildingName().trim();
        if(buildingRepository.existsByBuildingNameIgnoreCaseAndIdNot(normalizedName, id)){
            throw new DuplicateResourceException("Building already exists");
        }
        if(request.getDesignFile() != null){
            String designPath = saveDesignImage(request.getDesignFile());
            building.setDesign(designPath);
        }
        building.setBuildingName(normalizedName);
        building.setBuildingArea(request.getBuildingArea());
        building.setAreaUnit(request.getAreaUnit());
        building.setStartDate(request.getStartDate());
        building.setTime(request.getTime());
        building.setTimeUnit(request.getTimeUnit());
        building.setContent(request.getContent());
        return buildingRepository.save(building);
    }

    public Building updateBuildingStatus(Long id, Short status) {
        Building building = buildingRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Building not found"));
        if(status != null){
            if(status != 0 && status != 1 && status != 2){
                throw new IllegalArgumentException("Status is not valid");
            }
        }
        if(building.getStatus() != null && building.getStatus() == 2){
            throw new InvalidStatusTransitionException("Can not change this building status");
        }
        building.setStatus(status);
        return buildingRepository.save(building);
    }

    public String saveDesignImage(MultipartFile file){
        if(file.isEmpty() || file == null){
            throw new IllegalArgumentException("File cannot be empty or null");
        }
        try{
            Path rootPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(rootPath);
            String originalFilename = file.getOriginalFilename();
            String ext = "";
            if(originalFilename != null && originalFilename.contains(".")){
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID() + ext;
            Path targetLocation = rootPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String normalizedUploadDir = uploadDir.replace("\\", "/");
            return "/" + normalizedUploadDir + "/" + storedFilename;
        }catch (IOException e){
            throw new RuntimeException("Unable to save design image" + e);
        }
    }
}

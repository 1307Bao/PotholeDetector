package com.example.demo.mapper;

import com.example.demo.dto.response.PotholeResponse;
import com.example.demo.entity.Pothole;
import com.example.demo.entity.PotholeDetected;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface PotholeMapper {
    PotholeResponse toPotholeResponse(Pothole pothole);

}

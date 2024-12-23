package com.example.demo.mapper;

import com.example.demo.dto.request.RegisterByUserRequest;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    User toUser(RegisterByUserRequest request);
}

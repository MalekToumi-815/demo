package com.example.demo.web.mapper;

import com.example.demo.domain.model.User;
import com.example.demo.web.dto.CreateUserRequestdto;
import com.example.demo.web.dto.UserResponsedto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullname", expression = "java(entity.getName() + \" \" + entity.getSurname())")
    @Mapping(target = "email", source = "email")
    UserResponsedto toDto(User entity);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "surname", source = "surname")
    @Mapping(target = "email", source = "email")
    User toEntity(CreateUserRequestdto dto);
}

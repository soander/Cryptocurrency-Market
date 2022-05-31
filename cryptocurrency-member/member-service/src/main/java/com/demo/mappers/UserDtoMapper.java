package com.demo.mappers;

import com.demo.domain.User;
//import com.demo.dto.UserDto;
import com.demo.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * entity2Dto -> Dto2Entity
 */
@Mapper(componentModel = "spring")
public interface UserDtoMapper {

     // Get instance
    UserDtoMapper INSTANCE =  Mappers.getMapper(UserDtoMapper.class);

    // Entity -> Dto
    UserDto convert2Dto(User source) ;

    // Dto -> Entity
    User convert2Entity(UserDto source) ;

    // Entity list -> Dto list
    List<UserDto> convert2Dto(List<User> source) ;

    // Dto list -> Entity list
    List<User> convert2Entity(List<UserDto> source) ;
}

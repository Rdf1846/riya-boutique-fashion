package com.riyaboutique.auth.mapper;

import com.riyaboutique.auth.dto.SignupRequestDto;
import com.riyaboutique.auth.dto.UserDetailsDto;
import com.riyaboutique.auth.entity.UserEntity;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserMapperClass {

    public UserEntity mapUserRequestDtoToUserEntity(SignupRequestDto signupRequestDto)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupRequestDto.getFirstName());
        userEntity.setLastName(signupRequestDto.getLastName());
        userEntity.setPassword(signupRequestDto.getPassword());
        userEntity.setEmail(signupRequestDto.getEmail());

        return userEntity;

    }

    public UserDetailsDto mapUserEntityToUserDetailsDto(UserEntity userEntity)
    {
        UserDetailsDto userDetailsDto = UserDetailsDto.builder().firstName(userEntity.getFirstName()).lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .build();

        return userDetailsDto;
    }
}

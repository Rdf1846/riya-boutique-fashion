package com.riyaboutique.auth.mapper;

import com.riyaboutique.auth.dto.SignupRequestDto;
import com.riyaboutique.auth.dto.UserDetailsResponseDto;
import com.riyaboutique.auth.entity.UserEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserMapperClass {

    private final PasswordEncoder passwordEncoder;

    public UserMapperClass(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public UserEntity mapUserRequestDtoToUserEntity(SignupRequestDto signupRequestDto)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupRequestDto.getFirstName());
        userEntity.setLastName(signupRequestDto.getLastName());
        userEntity.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        userEntity.setEmail(signupRequestDto.getEmail());

        return userEntity;

    }

    public UserDetailsResponseDto mapUserEntityToUserDetailsDto(UserEntity userEntity)
    {
        UserDetailsResponseDto userDetailsResponseDto = UserDetailsResponseDto.builder().firstName(userEntity.getFirstName()).lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .build();

        return userDetailsResponseDto;
    }
}

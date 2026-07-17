package com.riyaboutique.auth.service;

import com.riyaboutique.auth.dto.SignupRequestDto;
import com.riyaboutique.auth.dto.UserDetailsDto;
import com.riyaboutique.auth.entity.UserEntity;
import com.riyaboutique.auth.exception.UserAlreadyExistsException;
import com.riyaboutique.auth.exception.UserNotFoundException;
import com.riyaboutique.auth.mapper.UserMapperClass;
import com.riyaboutique.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapperClass userMapperClass;

    public UserService(UserRepository userRepository, UserMapperClass userMapperClass)
    {
        this.userRepository = userRepository;
        this.userMapperClass = userMapperClass;
    }



    public String signUp(SignupRequestDto signupRequestDto)
    {

        if(userRepository.existsByEmail(signupRequestDto.getEmail()))
        {
            throw new UserAlreadyExistsException("User with same email already registered.");
        }
        UserEntity userEntity = userMapperClass.mapUserRequestDtoToUserEntity(signupRequestDto);
        userRepository.save(userEntity);
        return "user details successfully saved into database";
    }

    public List<UserDetailsDto> findAllUsers()
    {
        List<UserEntity> allUserEntityList = userRepository.findAll();
       List<UserDetailsDto> userDetailsDtoList =  allUserEntityList.stream().map(userMapperClass::mapUserEntityToUserDetailsDto).toList();
       if(!userDetailsDtoList.isEmpty()) {
           log.info("All users details successfully fetched");
       }
       return userDetailsDtoList;
    }


    public UserDetailsDto findUserById(Long id)
    {
        log.info("Finding user with id : {}",id);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not exists"));
        UserDetailsDto userDetailsDto = userMapperClass.mapUserEntityToUserDetailsDto(userEntity);
        log.debug("User successfully find with id : {}",id);
        return userDetailsDto;
    }

    public String deleteById(Long id) {

        log.info("Deleting user with id: {}",id);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not exists"));

            userRepository.deleteById(id);
            return "User Deleted successfully from database";

    }

    public UserDetailsDto updateUserById(Long id, SignupRequestDto signupRequestDto) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User Not exists"));

        log.info("Updating user profile");

        if(signupRequestDto.getFirstName() != null)
        {
            userEntity.setFirstName(signupRequestDto.getFirstName());
        }
        if(signupRequestDto.getLastName() != null)
        {
            userEntity.setLastName(signupRequestDto.getLastName());
        }
        if(signupRequestDto.getEmail() != null)
        {
            userEntity.setEmail(signupRequestDto.getEmail());
        }
        if(signupRequestDto.getPassword() != null)
        {
            userEntity.setPassword(signupRequestDto.getPassword());
        }

       UserEntity updatedEntity = userRepository.save(userEntity);

        UserDetailsDto userDetailsDto = userMapperClass.mapUserEntityToUserDetailsDto(updatedEntity);

        log.info("User details successfully updated");
        return userDetailsDto;
    }
}

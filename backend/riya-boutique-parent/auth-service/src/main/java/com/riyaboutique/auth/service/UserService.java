package com.riyaboutique.auth.service;

import com.riyaboutique.auth.dto.LoginRequestDto;
import com.riyaboutique.auth.dto.SignupRequestDto;
import com.riyaboutique.auth.dto.UserDetailsResponseDto;
import com.riyaboutique.auth.entity.UserEntity;
import com.riyaboutique.auth.exception.UserAlreadyExistsException;
import com.riyaboutique.auth.exception.UserNotFoundException;
import com.riyaboutique.auth.mapper.UserMapperClass;
import com.riyaboutique.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapperClass userMapperClass;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapperClass userMapperClass, AuthenticationManager authenticationManager, JwtService jwtService)
    {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapperClass = userMapperClass;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

    public List<UserDetailsResponseDto> findAllUsers()
    {
        List<UserEntity> allUserEntityList = userRepository.findAll();
       List<UserDetailsResponseDto> userDetailsResponseDtoList =  allUserEntityList.stream().map(userMapperClass::mapUserEntityToUserDetailsDto).toList();
       if(!userDetailsResponseDtoList.isEmpty()) {
           log.info("All users details successfully fetched");
       }
       return userDetailsResponseDtoList;
    }


    public UserDetailsResponseDto findUserById(Long id)
    {
        log.info("Finding user with id : {}",id);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not exists"));
        UserDetailsResponseDto userDetailsResponseDto = userMapperClass.mapUserEntityToUserDetailsDto(userEntity);
        log.debug("User successfully find with id : {}",id);
        return userDetailsResponseDto;
    }

    public String deleteById(Long id) {

        log.info("Deleting user with id: {}",id);
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not exists"));

            userRepository.deleteById(id);
            return "User Deleted successfully from database";

    }

    public UserDetailsResponseDto updateUserById(Long id, SignupRequestDto signupRequestDto) {
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

        UserDetailsResponseDto userDetailsResponseDto = userMapperClass.mapUserEntityToUserDetailsDto(updatedEntity);

        log.info("User details successfully updated");
        return userDetailsResponseDto;
    }

    public String login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        String jwtToken = jwtService.generateToken(loginRequestDto.getEmail());
        return jwtToken;
    }
}

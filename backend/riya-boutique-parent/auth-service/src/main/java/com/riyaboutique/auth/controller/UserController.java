package com.riyaboutique.auth.controller;

import com.riyaboutique.auth.dto.SignupRequestDto;
import com.riyaboutique.auth.dto.UserDetailsDto;
import com.riyaboutique.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public String welcome()
    {
        return "Welcome to the RIYA FASHION HOUSE Authentication Service";
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto signupRequestDto)
    {
        String result = userService.signUp(signupRequestDto);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/allusers")
    public ResponseEntity<List<UserDetailsDto>> getAllUser()
    {
       List<UserDetailsDto> allUserList =  userService.findAllUsers();
       return ResponseEntity.ok(allUserList);

    }


    @GetMapping("/findbyid/{id}")
    public ResponseEntity<UserDetailsDto> findById(@PathVariable Long id)
    {
       return ResponseEntity.ok(userService.findUserById(id));
    }

    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id)
    {
      return ResponseEntity.ok(userService.deleteById(id));
    }


    @PutMapping("/updateuser/{id}")
    public ResponseEntity<UserDetailsDto> updateUserById(@PathVariable Long id,@Valid @RequestBody SignupRequestDto signupRequestDto)
    {
        System.out.println("Update of record started");
        return ResponseEntity.ok(userService.updateUserById(id, signupRequestDto));
    }


}

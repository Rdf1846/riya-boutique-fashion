package com.riyaboutique.auth.dto;

import com.riyaboutique.auth.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "First Name can't be blank")
    private String firstName;


    private String lastName;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Must be valid email format : XXX@Domain.com")
    private String email;

    @NotNull
    @Size(min = 8, message = "Password must be of minimum 8 characters in length")
    private String password;


}

package com.riyaboutique.auth.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private String firstName;
    private String lastName;
    private String email;

}

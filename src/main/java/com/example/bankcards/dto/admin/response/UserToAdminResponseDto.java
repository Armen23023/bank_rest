package com.example.bankcards.dto.admin.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserToAdminResponseDto {

    private UUID ref;

    private String firstName;

    private String lastName;

    private boolean blocked;

    private String email;

}

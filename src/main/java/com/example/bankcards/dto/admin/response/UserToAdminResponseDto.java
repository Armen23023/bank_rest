package com.example.bankcards.dto.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserToAdminResponseDto")
public class UserToAdminResponseDto {

    @Schema(description = "Reference", example = "0be8df4e-7d3a-4037-b4dc-cf44a51c617e")
    private UUID ref;

    @Schema(description = "First Name", example = "John")
    private String firstName;

    @Schema(description = "Last Name", example = "John")
    private String lastName;

    @Schema(description = "Deleted", example = "true")
    private boolean blocked;

    @Schema(description = "Email", example = "john.doe@gmail.com")
    private String email;

}

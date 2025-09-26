package com.example.bankcards.dto.mapper;


import com.example.bankcards.dto.admin.response.UserToAdminDetailedResponseDto;
import com.example.bankcards.entity.User;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserToUserResponseDtoMapper implements Function<User, UserToAdminDetailedResponseDto> {

    @Override
    public UserToAdminDetailedResponseDto apply(User user) {
        UserToAdminDetailedResponseDto dto = new UserToAdminDetailedResponseDto();
        dto.setRef(user.getRef());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setDeletedDateTime(user.getDeletedDateTime());
        dto.setDeletedDateTime(user.getDeletedDateTime());
        return dto;
    }
}

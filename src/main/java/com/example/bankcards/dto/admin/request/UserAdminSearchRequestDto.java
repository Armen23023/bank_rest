package com.example.bankcards.dto.admin.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminSearchRequestDto implements Serializable {
    private boolean includeBlocked;
}

package com.example.bankcards.util;

import static com.example.bankcards.config.SecurityConstants.USER_ROLE;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserRole;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateUserUtils {
    /**
     * Create new user based on the params provided.
     * @param firstName of the user.
     * @param lastName of the user.
     * @param password of the user if present.
     * @param email of the user.
     * @return created {@link User}
     */
    public User createNewUser(
            final String firstName,
            final String lastName,
            final String password,
            final String email) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        UserRole userRole = new UserRole();
        userRole.setValue(USER_ROLE);
        userRole.setUser(user);

        user.setUserRoles(List.of(userRole));

        return user;
    }
}

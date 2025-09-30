package com.example.bankcards;

import com.example.bankcards.entity.User;
import com.example.bankcards.entity.UserRole;
import com.example.bankcards.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));

            UserRole role = new UserRole();
            role.setValue("ADMIN");
            role.setUser(admin);

            UserRole role2 = new UserRole();
            role2.setValue("USER");
            role2.setUser(admin);

            admin.setUserRoles(List.of(role, role2));

            userRepository.save(admin);
        }
    }
}


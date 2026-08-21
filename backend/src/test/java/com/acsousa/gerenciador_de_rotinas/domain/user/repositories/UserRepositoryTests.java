package com.acsousa.gerenciador_de_rotinas.domain.user.repositories;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    private String existingLogin;
    private String notExistingLogin;
    private String existingEmail;
    private String notExistingEmail;

    @BeforeEach
    void setUp() throws Exception {
        existingLogin = "batman";
        notExistingLogin = "robin";
        existingEmail = "bruce.wayne@email.com";
        notExistingEmail = "dick.grayson@email.com";
    }

    @Test
    public void shouldReturnUserModelWhenLoginExists() {
        UserModel userModel = userRepository.findByLogin(existingLogin);
        Assertions.assertNotNull(userModel);
    }

    @Test
    public void shouldReturnNullWhenLoginDoesNotExist() {
        UserModel userModel = userRepository.findByLogin(notExistingLogin);
        Assertions.assertNull(userModel);
    }

    @Test
    public void shouldReturnUserModelWhenEmailExists() {
        UserModel userModel = userRepository.findByEmail(existingEmail);
        Assertions.assertNotNull(userModel);
    }

    @Test
    public void shouldReturnNullWhenEmailDoesNotExist() {
        UserModel userModel = userRepository.findByEmail(notExistingEmail);
        Assertions.assertNull(userModel);
    }
}

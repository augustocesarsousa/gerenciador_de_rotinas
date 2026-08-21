package com.acsousa.gerenciador_de_rotinas.domain.user.services;

import com.acsousa.gerenciador_de_rotinas.common.exceptions.ResourceNotFoundException;
import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserStatus;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserCreateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserUpdateRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.specifications.UserQueryFilter;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceIT {
    @Autowired
    private UserService userService;

    private UserCreateRecord validUserCreateRecord;
    private Long notExistingId;
    private Pageable pageable;
    private UserQueryFilter userQueryFilter;

    @BeforeEach
    void setUp() throws Exception {
        validUserCreateRecord = UserFactory.createUserCreateRecord();
        notExistingId = 1000L;
        pageable = PageRequest.of(0, 10);
        userQueryFilter = new UserQueryFilter();
    }

    @Test
    public void shouldPersistUserInDatabaseWhenValidData() {
        UserResponseRecord userResponse = userService.create(validUserCreateRecord);

        Assertions.assertNotNull(userResponse);
        Assertions.assertNotNull(userResponse.id());
        Assertions.assertEquals("Hal Jordan", userResponse.name());
        Assertions.assertEquals(UserStatus.ACTIVE, userResponse.status());
        Assertions.assertNotNull(userResponse.createdAt());
    }

    @Test
    public void shouldReturnUserFromDatabaseWhenExistingId() {
        UserResponseRecord userResponse = userService.create(validUserCreateRecord);
        UserResponseRecord userFound = userService.findById(userResponse.id());

        Assertions.assertNotNull(userFound);
        Assertions.assertEquals(userFound.id(), userResponse.id());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(notExistingId);
        });
    }

    @Test
    public void shouldReturnPagedResultsWhenFindAll() {
        Page<UserResponseRecord> userResponsePage = userService.findAll(userQueryFilter, pageable);

        Assertions.assertNotNull(userResponsePage);
        Assertions.assertFalse(userResponsePage.isEmpty());
    }

    @Test
    public void shouldUpdateUserInDatabaseWhenValidData() {
        UserResponseRecord userResponse = userService.create(validUserCreateRecord);

        UserUpdateRecord updateRecord = new UserUpdateRecord(
                "John Stewart",
                userResponse.login(),
                "",
                userResponse.email(),
                userResponse.status(),
                validUserCreateRecord.roles(),
                validUserCreateRecord.userIdEdit()
        );

        UserResponseRecord userUpdated = userService.update(userResponse.id(), updateRecord);

        Assertions.assertEquals("John Stewart", userUpdated.name());
        Assertions.assertEquals(userUpdated.id(), userResponse.id());
    }
}

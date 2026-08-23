package com.acsousa.gerenciador_de_rotinas.domain.user.usecases;

import com.acsousa.gerenciador_de_rotinas.domain.user.models.UserModel;
import com.acsousa.gerenciador_de_rotinas.domain.user.records.UserResponseRecord;
import com.acsousa.gerenciador_de_rotinas.domain.user.repositories.UserRepository;
import com.acsousa.gerenciador_de_rotinas.domain.user.specifications.UserQueryFilter;
import com.acsousa.gerenciador_de_rotinas.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FindAllUsersUseCaseTests {
    @InjectMocks
    private FindAllUsersUseCase findAllUsersUseCase;

    @Mock
    private UserRepository userRepository;

    private UserQueryFilter queryFilter;
    private Pageable pageable;
    private UserModel userModel;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        queryFilter = new UserQueryFilter();
        pageable = PageRequest.of(0, 10);
        userModel = UserFactory.createUserModel();
        PageImpl<UserModel> page = new PageImpl<>(List.of(userModel));

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
    }

    @Test
    public void shouldReturnPageOfUsersWhenQueryFiltersApplied() {
        Page<UserResponseRecord> resultPage = findAllUsersUseCase.execute(queryFilter, pageable);

        Assertions.assertNotNull(resultPage);
        Assertions.assertEquals(1, resultPage.getTotalElements());
        Assertions.assertEquals("Hal Jordan", resultPage.getContent().get(0).name());
    }
}

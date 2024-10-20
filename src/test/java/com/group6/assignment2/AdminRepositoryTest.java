package com.group6.assignment2;

import com.group6.assignment2.entity.Admin;
import com.group6.assignment2.entity.Role;
import com.group6.assignment2.repository.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AdminRepositoryTest {

    @MockBean
    private AdminRepository mockAdminRepository;

    @BeforeEach
    public void beforeEach(){
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setRole(Role.ADMIN);
        admin.setEmail("admin@email.com");
        admin.setPassword("password");
        admin.setUsername("admin");
        mockAdminRepository.save(admin);
    }
    @AfterEach
    public void afterEach(){
        mockAdminRepository.deleteAll();
    }

    @Test
    void saveAdminEntity() {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setRole(Role.ADMIN);
        admin.setEmail("admin@email.com");
        admin.setPassword("password");
        admin.setUsername("admin");
        mockAdminRepository.save(admin);
    }
    @Test
    void deleteAdminEntity() {
        Long id = 1L;
        Admin admin = new Admin();
        admin.setId(id);

        // Mock the repository methods
        when(mockAdminRepository.findById(id)).thenReturn(Optional.of(admin));

        Admin foundAdmin = mockAdminRepository.findById(id).orElseThrow();
        mockAdminRepository.delete(foundAdmin);

        verify(mockAdminRepository, times(1)).delete(foundAdmin);

    }
}

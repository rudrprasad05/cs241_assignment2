package com.group6.assignment2;

import com.group6.assignment2.entity.Student;
import com.group6.assignment2.entity.Role;
import com.group6.assignment2.repository.StudentRepository;
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
public class StudentRepositoryTest {

    @MockBean
    private StudentRepository mockStudentRepository;

    @BeforeEach
    public void beforeEach(){
        Student student = new Student();
        student.setId(1L);
        student.setRole(Role.STUDENT);
        student.setEmail("admin@email.com");
        student.setPassword("password");
        student.setUsername("admin");
        mockStudentRepository.save(student);
    }
    @AfterEach
    public void afterEach(){
        mockStudentRepository.deleteAll();
    }

    @Test
    void saveStudentEntity() {
        Student student = new Student();
        student.setId(1L);
        student.setRole(Role.ADMIN);
        student.setEmail("student@email.com");
        student.setPassword("password");
        student.setUsername("student");
        mockStudentRepository.save(student);
    }
    @Test
    void deleteStudentEntity() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        // Mock the repository methods
        when(mockStudentRepository.findById(id)).thenReturn(Optional.of(student));

        Student foundStudent = mockStudentRepository.findById(id).orElseThrow();
        mockStudentRepository.delete(foundStudent);

        verify(mockStudentRepository, times(1)).delete(foundStudent);

    }

    @Test
    void readStudentEntity() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        // Mock the repository methods
        when(mockStudentRepository.findById(id)).thenReturn(Optional.of(student));

        Student foundStudent = mockStudentRepository.findById(id).orElseThrow();

    }

    @Test
    void updateStudentEntity() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        // Mock the repository methods
        when(mockStudentRepository.findById(id)).thenReturn(Optional.of(student));

        student.setPersonalEmail("student-updated@email.com");

        Student foundStudent = mockStudentRepository.findById(id).orElseThrow();
    }
}

package com.group6.assignment2;

import com.group6.assignment2.entity.Notification;
import com.group6.assignment2.repository.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class NotificationRepositoryTest {

    @MockBean
    private NotificationRepository mockNotificationRepository;

    @BeforeEach
    public void beforeEach(){
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setSeen(false);
        notification.setType(Notification.NotificationType.INFO);
        mockNotificationRepository.save(notification);
    }
    @AfterEach
    public void afterEach(){
        mockNotificationRepository.deleteAll();
    }

    @Test
    void saveNotificationEntity() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setSeen(false);
        notification.setType(Notification.NotificationType.INFO);
        mockNotificationRepository.save(notification);
    }

    @Test
    void deleteNotificationEntity() {
        Long id = 1L;
        Notification notification = new Notification();  // Create a mock notification object
        notification.setId(id);

        // Mock the repository methods
        when(mockNotificationRepository.findById(id)).thenReturn(Optional.of(notification));

        Notification foundNotification = mockNotificationRepository.findById(id).orElseThrow();
        mockNotificationRepository.delete(foundNotification);

        verify(mockNotificationRepository, times(1)).delete(foundNotification);

    }
}

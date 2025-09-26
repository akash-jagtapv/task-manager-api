package com.taskmanager.service;

import com.taskmanager.entity.Task;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("test Task");
        testTask.setDescription("Test Description");
        testTask.setCompleted(false);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        List<Task> expectedTasks = Arrays.asList(testTask, new Task());
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks();

        // Assert
        assertEquals(2, actualTasks.size());
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        Task actualTask = taskService.getTaskById(1L);

        // Assert
        assertEquals(testTask, actualTask);
        assertEquals("test Task", actualTask.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskById_WhenTaskNotExists_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(999L)
        );

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(taskRepository).findById(999L);
    }

    @Test
    void saveTask_ShouldReturnSavedTask() {
        // Arrange
        when(taskRepository.save(testTask)).thenReturn(testTask);

        // Act
        Task savedTask = taskService.saveTask(testTask);

        // Assert
        assertEquals(testTask, savedTask);
        verify(taskRepository).save(testTask);
    }

    @Test
    void deleteTask_ShouldCallRepositoryDelete() {
        // Arrange
        Long taskId = 1L;

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).deleteById(taskId);
    }
}

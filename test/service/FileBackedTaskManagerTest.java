package service;

import exception.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.file.FileBackedTaskManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager manager;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        try {
            tempFile = File.createTempFile("tasks", ".csv");
            manager = new FileBackedTaskManager(tempFile);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        duration = Duration.ofMinutes(2);
        startTime = LocalDateTime.of(2024, 12, 10, 2, 3);
        endTime = LocalDateTime.now().plus(duration);
    }

    @Test
    void shouldSaveAndLoadTasksCorrectly() {
        Task task = new Task(1, "Завершить уровень", "Пройти сложный уровень в игре", Status.NEW,duration,startTime);
        manager.addTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size(), "Должна быть одна задача");
        assertEquals(task, loadedManager.getTask(1), "Загруженная задача должна совпадать с сохранённой");
    }

    @Test
    void shouldSaveAndLoadEpicsAndSubtasksCorrectly() {
        Epic epic = new Epic(3, "Выполнить квесты", "Завершить все квесты в игре", Status.NEW, duration, startTime, endTime);
        SubTask subTask1 = new SubTask(4, "Собрать артефакты", "Собрать все редкие артефакты в игре", Status.NEW,duration,startTime,endTime,epic.getId());
        SubTask subTask2 = new SubTask(5, "Пройти подземелье", "Пройти подземелье в поисках редких предметов", Status.NEW, epic.getId());

        manager.addEpic(epic);
        manager.addSubtask(subTask1);
        manager.addSubtask(subTask2);


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllEpics().size(), "Должен быть один эпик");
        assertEquals(epic, loadedManager.getEpic(3), "Загруженный эпик должен совпадать с сохранённым");

        assertEquals(2, loadedManager.getAllSubtasks().size(), "Должны быть две подзадачи");
        assertTrue(loadedManager.getSubtask(4).equals(subTask1), "Загруженная подзадача 1 должна совпадать с сохранённой");
        assertTrue(loadedManager.getSubtask(5).equals(subTask2), "Загруженная подзадача 2 должна совпадать с сохранённой");

        assertTrue(loadedManager.getEpic(3).getSubTasks().contains(4), "Эпик должен содержать ID подзадачи 4");
        assertTrue(loadedManager.getEpic(3).getSubTasks().contains(5), "Эпик должен содержать ID подзадачи 5");
    }

    @Test
    void shouldHandleEmptyFileCorrectly() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty(), "Список задач должен быть пуст");
        assertTrue(loadedManager.getAllEpics().isEmpty(), "Список эпиков должен быть пуст");
        assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пуст");
    }

    @Test
    void shouldThrowExceptionForCorruptedFile() throws IOException {
        File tempFile = File.createTempFile("tasks", ".csv");
        tempFile.deleteOnExit();

        Files.writeString(tempFile.toPath(), "id,type,name,status,description,epic,duration,startTime\n1,Task,Завершить уровень,NEW,Пройти сложный уровень в игре,,60,10.11.2024 12:00:00\n");

        ManagerSaveException exception = assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile);
        });

        assertTrue(exception.getMessage().contains("Ошибка при разборе строки"), "Исключение должно сообщать об ошибке разбора");
    }
}

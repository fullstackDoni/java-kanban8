package service;
import org.junit.jupiter.api.BeforeEach;
import service.file.FileBackedTaskManager;
import java.io.File;
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
    }
}

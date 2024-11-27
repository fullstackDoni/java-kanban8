package model;

import model.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task{
    private final int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime,LocalDateTime endTime, int epicId) {
        super(id, name, description, status, duration, startTime);
        this.getEndTime();
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}

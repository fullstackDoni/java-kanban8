package model;

import model.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{

    private final List<Integer> subTasksIds = new ArrayList<>();
    private final LocalDateTime endTime;

    public Epic(String name, String description, LocalDateTime endTime) {
        super(name, description);
        this.endTime = endTime;
    }

    public Epic(int id, String name, String description, LocalDateTime endTime) {
        super(id, name, description);
        this.endTime = endTime;
    }

    public Epic(int id, String name, String description, Status status, LocalDateTime endTime) {
        super(id, name, description, status);
        this.endTime = endTime;
    }

    public Epic(String name, String description, Status status, Duration duration, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime endTime1) {
        super(name, description,status, duration, startTime,endTime);
        this.endTime = endTime1;
    }

    public Epic(String name, String description, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, description, duration, startTime);
        this.endTime = endTime;
    }

    public Epic(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, status, duration, startTime);
        this.endTime = endTime;
    }


    public List<Integer> getSubTasks() {
        return new ArrayList<>(subTasksIds);
    }

    public void addSubTask(int id) {
        subTasksIds.add(id);
    }

    public void removeSubTask(int id) {
        subTasksIds.remove(Integer.valueOf(id));
    }

    public void clearSubTasks() {
        subTasksIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasksIds=" + subTasksIds +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", duration=" + getDuration() +
                '}';
    }
}

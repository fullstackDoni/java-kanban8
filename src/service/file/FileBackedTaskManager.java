package service.file;

import exception.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.enums.Status;
import model.enums.TaskType;
import service.managers.InMemoryTaskManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(SubTask subtask) {
        super.addSubtask(subtask);
        save();
    }


    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }


    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        save();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        save();
    }

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic,startTime,duration,endTime\n");
            for (Task task : getAllTasks()) {
                writer.write(toCSV(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toCSV(epic) + "\n");
            }
            for (SubTask subtask : getAllSubtasks()) {
                writer.write(toCSV(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }


    private String toCSV(Task task) {
        String type = task instanceof Epic ? "EPIC" : (task instanceof SubTask ? "SUBTASK" : "TASK");
        String epicId = task instanceof SubTask ? String.valueOf(((SubTask) task).getEpicId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                type,
                task.getName(),
                task.getStatus().name(),
                task.getDescription(),
                task.getStartTime(),
                task.getDuration(),
                epicId
        );
    }

    private Task fromCSV(String[] fields) {
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[1];
        String description = fields[2];
        Status status = Status.valueOf(fields[3]);
        Duration duration = Duration.parse(fields[4]);
        LocalDateTime startTime = LocalDateTime.parse(fields[5]);
        LocalDateTime endTime = LocalDateTime.parse(fields[6]);
        int epicId = fields.length > 7 ? Integer.parseInt(fields[7]) : -1;

        switch (type) {
            case TASK:
                return new Task(id,name,description,status,duration,startTime);
            case EPIC:
                return new Epic(id,name,description,status,startTime);
            case SUBTASK:
                if (epicId == -1) {
                    throw new IllegalArgumentException("SubTask требует указания epicId");
                }
                return new SubTask(id, name, description, status, duration, startTime,endTime, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        int maxId = 0;
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                return manager;
            }
            for (String line : lines.subList(1, lines.size())) {
                if (line.isBlank()) continue;
                try {
                    String[] fields = line.split(",");
                    Task task = manager.fromCSV(fields);

                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                    if (task instanceof Epic) {
                        manager.epics.put(task.getId(), (Epic) task);
                    } else if (task instanceof SubTask) {
                        manager.subtasks.put(task.getId(), (SubTask) task);
                        Epic epic = manager.epics.get(((SubTask) task).getEpicId());
                        epic.addSubTask(task.getId());
                    } else {
                        manager.tasks.put(task.getId(), task);
                    }
                } catch (Exception e) {
                    throw new ManagerSaveException("Ошибка при разборе строки: " + line, e);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла: " + file.getName(), e);
        }

        manager.id = maxId + 1;
        return manager;
    }

}

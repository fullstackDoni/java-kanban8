package service;
import service.managers.HistoryManager;
import service.managers.InMemoryHistoryManager;
import service.managers.InMemoryTaskManager;
import service.managers.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

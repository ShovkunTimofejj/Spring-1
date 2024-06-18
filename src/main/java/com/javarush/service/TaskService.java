package com.javarush.service;

import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {

    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAllTasks(int offset, int limit) {
        return taskDAO.getAll(offset, limit);
    }
    //toDo возможная ошибка из-за типа метода
    public int getAllCount() {
        return taskDAO.getCount();
    }

    @Transactional
    public Task edit(int id, String description, Status status) {
        final Task taskById = taskDAO.getTaskById(id);
        if (isNull(taskById)){
            throw new RuntimeException("Not found");
        }
        taskById.setDescription(description);
        taskById.setStatus(status);
        taskDAO.createOrUpdateTask(taskById);
        return taskById;
    }

    public Task create(String description, Status status) {
        final Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.createOrUpdateTask(task);
        return task;
    }

    @Transactional
    public void delete(int id) {
        final Task taskById = taskDAO.getTaskById(id);
        if (isNull(taskById)){
            throw new RuntimeException("Not found");
        }
        taskDAO.deleteTask(taskById);
    }
}

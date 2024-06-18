package com.javarush.controller;


import com.javarush.domain.Task;
import com.javarush.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String tasks(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        final List<Task> tasks = taskService.getAllTasks((page - 1) * limit, limit);
        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);
        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if (totalPages > 1) {
            final List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }
        return "tasks";

    }

    @PostMapping("/{id}")
    public String edit(Model model,
                     @PathVariable int id,
                     @RequestBody TaskInfo taskInfo) {
        if (isNull(id) || id <= 0) {
            throw new IllegalArgumentException("id is invalid");
        }

        final Task task = taskService.edit(id, taskInfo.getDescription(), taskInfo.getStatus());

        return tasks(model, 1, 10);
    }

    @PostMapping("/")
    public String add(Model model,
                     @RequestBody TaskInfo taskInfo) {
        final Task task = taskService.create(taskInfo.getDescription(), taskInfo.getStatus());

        return tasks(model, 1, 10);
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                         @PathVariable int id) {
        if (isNull(id) || id <= 0) {
            throw new IllegalArgumentException("id is invalid");
        }
        taskService.delete(id);

        return tasks(model, 1, 10);

    }


}

package com.example.workflow;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@EnableScheduling
public class CompleteSysTasks {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;


    @Scheduled(fixedRate = 60000)
    public void printUserTasks() {
        // Print all Tasks associated to a User at a point of time.
        System.out.println("CompleteTasks");
        int numUsers = 2;
        for (int i = 1; i <= numUsers; i++) {
            String userName = "System" + i;
            List<Task> tasks = taskService.createTaskQuery().taskAssignee(userName).list();
            if (!tasks.isEmpty()) {
                //System.out.println("Completing tasks assigned to user " + userName + ":");
                for (Task task : tasks) {
                    System.out.println("Completing tasks assigned to user " + userName + ":" + "Task Id: " + task.getId() + ", Name: " + task.getName());
                    completeTaskWithApprovalStatus(task);
                }
            } else {
                System.out.println("No tasks assigned to user " + userName);
            }
        }
    }
    public void completeTaskWithApprovalStatus(Task task) {

        System.out.println("Task Status Active or not: "+ !isTaskCompleted(task.getId()));
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalStatus", "approved");
        taskService.complete(task.getId(), variables);
        System.out.println("Task Status Active or not: "+ !isTaskCompleted(task.getId()));    }

    public boolean isTaskCompleted(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task == null;

    }
}
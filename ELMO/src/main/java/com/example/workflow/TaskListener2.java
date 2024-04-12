package com.example.workflow;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component

public class TaskListener2 implements TaskListener {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String processInstanceId = delegateTask.getProcessInstanceId();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        Task currentTask = processEngine.getTaskService().createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        Boolean variableValue = (Boolean) processEngine.getRuntimeService()
                .getVariable(currentTask.getExecutionId(), "variableName");
        if (variableValue == null) {
            processEngine.getRuntimeService()
                    .setVariable(currentTask.getExecutionId(), "variableName", true);
            String newAssignee = assignUser();
            delegateTask.getExecution().setVariable("assignee", newAssignee);
            //taskService.setAssignee(currentTask.getId(), newAssignee);
            runtimeService.createProcessInstanceModification(processInstanceId)
                    .startBeforeActivity(currentTask.getTaskDefinitionKey())
                    .execute();

            System.out.println("Task " + delegateTask.getId() + " reassigned to " + newAssignee);
            System.out.println("Task reassigned successfully.");
        }

    }
    public String assignUser(){
        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        return "UserName" + String.valueOf(randomNumber);
    }
}

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

public class TaskListener1 implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        Object assignee = delegateTask.getExecution().getVariable("assignee");
        System.out.println("Assignee process variable: " + assignee);

    }

}
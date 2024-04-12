package com.example.workflow;

import jakarta.annotation.PostConstruct;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// Creating Users
// Starting Deployment
// Initializing some process variables
@Component
public class Starter {
    //camunda:assignee="${assignee}


    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final IdentityService identityService;

    @Autowired
    public Starter(ProcessEngine processEngine){
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
        this.identityService = processEngine.getIdentityService();
        this.repositoryService = processEngine.getRepositoryService();
    }

    @PostConstruct
    public void startWorkFlowTask(){
        System.out.println("WorkFlowStarted");
        userCreate();
        int number = 5;
        for(int i = 1; i <= number; i++) {
            startWorkFlowTaskInstance();
        }
    }
    private void startWorkFlowTaskInstance() {

        Map<String, Object> variables = new HashMap<>();
        variables.put("starter", "UserName3");
        variables.put("assignee", "");

        Deployment deployment1 = repositoryService.createDeployment().addClasspathResource("SysTask1.bpmn").deploy();
        Deployment deployment2 = repositoryService.createDeployment().addClasspathResource("SysTask2.bpmn").deploy();

        String processDefinitionKey1 = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).singleResult().getKey();
        String processDefinitionKey2 = repositoryService.createProcessDefinitionQuery().deploymentId(deployment2.getId()).singleResult().getKey();

        String processId1 = runtimeService.startProcessInstanceByKey(processDefinitionKey1, variables).getId();
        String processId2 = runtimeService.startProcessInstanceByKey(processDefinitionKey2, variables).getId();
        System.out.println("Process with ID:" + processId1 + " Started");
        System.out.println("Process with ID:" + processId2 + " Started");
    }



    public void userCreate(){
        System.out.println("Users_SysAuto");
        for(int i = 1; i<=2 ; i++){
            String userId = "System" + i;
            User existing = identityService.createUserQuery().userId(userId).singleResult();
            if (existing == null){
                User user = identityService.newUser(userId);
                user.setFirstName("System");
                user.setLastName("Auto" + i);
                //user.setEmail("John.Doe" + i + "@example.com");
                user.setPassword("password123");
                identityService.saveUser(user);
            }
        }
    }
}

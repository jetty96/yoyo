package com.example.workflow;

import jakarta.annotation.PostConstruct;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.identity.Group;
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
        int number = 1;
        for(int i = 1; i <= number; i++) {
            String ProcessId = startWorkFlowTaskInstance1();
            System.out.println(ProcessId);
        }
    }
    private String startWorkFlowTaskInstance(){


        Deployment deployment = repositoryService.createDeployment().addClasspathResource("diagram_2.bpmn").deploy();
        String processDefinitionKey = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult().getKey();
        String processId = runtimeService.startProcessInstanceByKey(processDefinitionKey).getId();
        System.out.println(processDefinitionKey);
        return processId;
    }
    private String startWorkFlowTaskInstance1(){

        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee", "UserName3");
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("diagram_2.bpmn").deploy();
        String processDefinitionKey = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult().getKey();
        String processId = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables).getId();
        System.out.println(processDefinitionKey);
        return processId;
    }
    public void userCreate(){
        System.out.println("Users");
        for(int i = 1; i<=5 ; i++){
            String userId = "UserName" + i;
            User existing = identityService.createUserQuery().userId(userId).singleResult();
            if (existing == null){
                User user = identityService.newUser(userId);
                user.setFirstName("John");
                user.setLastName("Doe" + i);
                user.setEmail("John.Doe" + i + "@example.com");
                user.setPassword("password123");
                identityService.saveUser(user);
            }
        }
    }

    public void groupCreate(){

        Group group = identityService.newGroup("groupId");
        group.setName("Group Name");
        group.setType("Type");
        identityService.saveGroup(group);

        // Add users to the group
        identityService.createMembership("UserName1", "groupId");
        identityService.createMembership("UserName2", "groupId");
        identityService.createMembership("UserName3", "groupId");

        System.out.println("Users added to the group successfully!");
    }

}

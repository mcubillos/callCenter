package com.example.callcenter.service;

import com.example.callcenter.model.Employee;
import com.example.callcenter.model.EmployeeType;
import com.example.callcenter.repository.EmployeeRepository;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class Dispatcher implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dispatcher.class);

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private JmsTemplate jmsTemplate;

    private int counter = 0;

    public int completedJobs() {
        return counter;
    }

    public synchronized void dispatchCall(String destination, String message) {

        try {
            List<Employee> operators = employeeRepo.findByTypeAndIsAvailable(EmployeeType.OPERATOR.getTypeCode(), true);
            List<Employee> supervisors = employeeRepo.findByTypeAndIsAvailable(EmployeeType.SUPERVISOR.getTypeCode(), true);
            List<Employee> directors = employeeRepo.findByTypeAndIsAvailable(EmployeeType.DIRECTOR.getTypeCode(), true);
            if(operators.size() > 0){
                sendCall(operators.get(0), message, destination);
            }else if(supervisors.size() > 0){
                sendCall(supervisors.get(0), message, destination);
            }else if(directors.size() > 0){
                sendCall(directors.get(0), message, destination);
            }else {
                Thread.sleep(5000);
                dispatchCall(destination, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int pendingJobs(String queueName) {
        return jmsTemplate.browse(queueName, (s, qb) -> Collections.list(qb.getEnumeration()).size());
    }

    public boolean isUp() {
        ConnectionFactory connection = jmsTemplate.getConnectionFactory();
        try {
            connection.createConnection().close();
            return true;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof ActiveMQTextMessage) {
            ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;

            try {
                answerCall(textMessage.getText());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            counter++;
        } else {
            LOGGER.error("Message is not a text message " + message.toString());
        }
    }

    private void sendCall(Employee employee, String message, String destination){
        employee.setAvailable(false);
        employeeRepo.save(employee);
        jmsTemplate.convertAndSend(destination, message + ":" + employee.getId());
        LOGGER.info("answering call='{}' by employee'{}'", message, employee.getName());
    }

    private void answerCall(String text) throws InterruptedException {
        LOGGER.info("Processing task " + text );
        Thread.sleep(retrieveRandomTime());
        LOGGER.info("Completed task " + text);
        String[] info = text.split(":");
        Employee employee = employeeRepo.findById(Long.valueOf(info[info.length-1])).get();
        employee.setAvailable(true);
        employeeRepo.save(employee);
    }

    private int retrieveRandomTime(){
        Random r = new Random();
        int low = 5000;
        int high = 10000;
        return r.nextInt(high-low) + low;
    }
}
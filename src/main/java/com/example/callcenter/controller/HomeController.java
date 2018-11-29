package com.example.callcenter.controller;

import com.example.callcenter.model.Call;
import com.example.callcenter.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private Dispatcher dispatcher;

    @Value("${queue.name}")
    private String queueName;

    @Value("${worker.name}")
    private String workerName;

    @Value("${store.enabled}")
    private boolean storeEnabled;

    @Value("${worker.enabled}")
    private boolean workerEnabled;

    @GetMapping("/home")
    public String home(Model model) {
        int pendingMessages = dispatcher.pendingJobs(queueName);
        model.addAttribute("call", new Call());
        model.addAttribute("pendingJobs", pendingMessages);
        model.addAttribute("completedJobs", dispatcher.completedJobs());
        model.addAttribute("isConnected", dispatcher.isUp() ? "yes" : "no");
        model.addAttribute("queueName", this.queueName);
        model.addAttribute("workerName", this.workerName);
        model.addAttribute("isStoreEnabled", this.storeEnabled);
        model.addAttribute("isWorkerEnabled", this.workerEnabled);
        return "home";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute Call call) {
        for (long i = 0; i < call.getQuantity(); i++) {
            String id = UUID.randomUUID().toString();
            dispatcher.dispatchCall(queueName, id);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value="/metrics", produces="text/plain")
    public String metrics() {
        int totalMessages = dispatcher.pendingJobs(queueName);
        return "# HELP messages Number of messages in the queueService\n"
                + "# TYPE messages gauge\n"
                + "messages " + totalMessages;
    }

    @RequestMapping(value="/health")
    public ResponseEntity health() {
        HttpStatus status;
        if (dispatcher.isUp()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

}

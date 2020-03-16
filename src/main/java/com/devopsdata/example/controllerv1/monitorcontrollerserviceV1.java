package com.devopsdata.example.controllerv1;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.devopsdata.example.controllerv1.configuration.monitorConfiguration;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@RestController
public class monitorcontrollerserviceV1 {

    private static final Logger logger = LoggerFactory.getLogger(monitorcontrollerserviceV1.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private monitorConfiguration configuration;

    @RequestMapping("/getVersion")
    public String getVersion() {
        String version = String.format(getClass().getPackage().getImplementationVersion());
       // return String.format(getClass().getPackage().getImplementationVersion());
        logger.info("Running healthcheck - version returned: "+version);
        return version;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command =  "dcos config set core.dcos_url https://"+configuration.getDCOSMesosURL()+";dcos auth login --username username --password password;dcos marathon deployment list";
        System.out.println("URL :"+ configuration.getDCOSMesosURL());
        processBuilder.command("bash", "-c", command);
        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Process executed Successfully!");
                System.out.println(output);
                sendEmail(output.toString());
                } else {
            }
            logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String Emessage) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo("xxx@gmail.com");
        helper.setText(Emessage);
        helper.setSubject("From Marathon Monitor Service");
        sender.send(message);
    }
}

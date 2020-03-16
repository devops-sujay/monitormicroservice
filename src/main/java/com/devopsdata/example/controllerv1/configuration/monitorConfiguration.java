package com.devopsdata.example.controllerv1.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.datasource")
public class monitorConfiguration {

    private String dcosmesosurl;
    private String username;
    private String password;

    public String getDCOSMesosURL() {
        return dcosmesosurl;
    }

    public void setDCOSMesosURL(String dcosmesosurl) {
        this.dcosmesosurl = dcosmesosurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Profile("dev")
    @Bean
    public String devDCOSURL() {
        System.out.println("DEV");
        System.out.println(dcosmesosurl);
        return dcosmesosurl;
    }

    @Profile("staging")
    @Bean
    public String stagingDCOSURL() {
        System.out.println(dcosmesosurl);
        return dcosmesosurl;
    }

    @Profile("prod")
    @Bean
    public String prodDCOSURL() {
        System.out.println(dcosmesosurl);
        return dcosmesosurl;
    }
}

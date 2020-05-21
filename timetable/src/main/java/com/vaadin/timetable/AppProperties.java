package com.vaadin.timetable;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppProperties {

    private String url;

    private String username;

    private String password;

    public AppProperties(@Value("${spring.datasource.url}") String url,@Value("${spring.datasource.username}") String username,@Value("${spring.datasource.password}") String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

      public String getUrl() {
        return url;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

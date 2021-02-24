package com.sh.maleki.mankalah.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
public class ShowActiveProfile {

    @Autowired
    Environment environment;

    @Bean(name="showMyProfile")
    public String showMyProfile(){
        System.out.println("********************Active Profiles:" + Arrays.toString(environment.getActiveProfiles()));
        return environment.getActiveProfiles().toString();
    }
}

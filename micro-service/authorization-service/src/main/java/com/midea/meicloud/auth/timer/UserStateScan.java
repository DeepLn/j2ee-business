package com.midea.meicloud.auth.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserStateScan {


    @Scheduled(fixedRate = 1000*30)
    public void timerDeleteLogin() {

    }
}

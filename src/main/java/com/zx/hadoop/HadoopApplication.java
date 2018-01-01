package com.zx.hadoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * author:ZhengXing
 * datetime:2017-12-29 22:00
 */
@SpringBootApplication
@RestController
public class HadoopApplication {



    public static void main(String[] args) {
        SpringApplication.run(HadoopApplication.class, args);
    }

    @RequestMapping("/")
    public String a() {
        return "a";
    }


}

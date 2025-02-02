package com.example.springcloud.eurekaribbonclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author clz
 * @date 2019/02/27 14:58
 */
@RestController
public class RibbonController {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    @GetMapping("hi")
    public String hi(@RequestParam(required = false,defaultValue = "springclouddemo") String name){
        return restTemplate.getForObject("http://eureka-client/hi?name=" + name, String.class);
    }

    public String hiError(String name){
        return "hi, " + name +", sorry, error exists!";
    }


    @Autowired
    private LoadBalancerClient loadBalancer;

    @GetMapping("testribbon")
    public String testRibbon(){
        ServiceInstance instance = loadBalancer.choose("eureka-client");
        return instance.getHost() + ":" + instance.getPort();
    }
}
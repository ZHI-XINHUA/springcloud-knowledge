package cloud.controller;

import cloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/{userId}")
    public User findUser(@PathVariable  String userId){
        System.out.println("userId="+userId);
      return  restTemplate.getForObject("http://provider-user/user/"+userId,User.class);
    }

    @GetMapping("/testLaladBalance")
    public void testLaladBalance(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("provider-user");
        // 打印当前选择的是哪个节点
        System.out.println("访问"+serviceInstance.getServiceId()+"---》"+serviceInstance.getHost()+":"+serviceInstance.getPort());
    }
}

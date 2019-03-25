package cloud.controller;

import cloud.entity.User;
import cloud.feign.UserFeginClient2;
import cloud.feign.UserFeginClient3;
import cloud.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private UserFeginClient2 userFeginClient2;

    @Autowired
    private UserFeginClient3 userFeginClient3;


    @GetMapping("/{userId}")
    public User findUser(@PathVariable  String userId){
      return userFeignClient.findUser(userId);
    }

    @GetMapping("/findUser2/{userId}")
    public User findUser2(@PathVariable  String userId){
        return userFeginClient2.findUser(userId);
    }

    @GetMapping("/findUser3/{userId}")
    public User findUser3(@PathVariable  String userId){
        return userFeginClient3.findUser(userId);
    }


}

package cloud.controller;

import cloud.entity.User;
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


    @GetMapping("/{userId}")
    public User findUser(@PathVariable  String userId){
      return userFeignClient.findUser(userId);
    }


}

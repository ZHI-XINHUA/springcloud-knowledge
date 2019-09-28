package com.zxh.feignserver;

import com.zxh.feignserver.fallback.UserFeignClientFallback;
import com.zxh.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 调用远程服务User的feign接口
 */
@FeignClient(name = "provider-user",fallback = UserFeignClientFallback.class) //provider-user是服务应用名称
public interface UserFeignClient {

    @GetMapping("/user/{userId}")
    UserVO getUser(@PathVariable String userId);

    @GetMapping("/hello/say")
    String sayHi(@RequestParam String name);

    @GetMapping("/hello/say1")
    String sayHi(@RequestHeader String name, @RequestHeader int age);

   @PostMapping("/hello/say2")
   String sayHi(@RequestBody UserVO user);
}

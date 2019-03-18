package cloud.controller;

import cloud.entity.User;
import cloud.feign.UserFeignClient;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/movie")
@Import(FeignClientsConfiguration.class)
public class MovieController {
    private UserFeignClient userUserFeignClient;

    private UserFeignClient adminUserFeignClient;

    @Autowired
    public MovieController(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        // 这边的decoder、encoder、client、contract，可以debug看看是什么实例。
        this.userUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("user", "123456"))
                .target(UserFeignClient.class, "http:///provider-user-with-auth/");
        this.adminUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("admin", "123456"))
                .target(UserFeignClient.class, "http://provider-user-with-auth/");
    }

    @GetMapping("/user-user/{userId}")
    public User findByIdUser(@PathVariable  String userId) {
        return this.userUserFeignClient.findUser(userId);
    }

    @GetMapping("/user-admin/{userId}")
    public User findByIdAdmin(@PathVariable  String userId) {
        return this.adminUserFeignClient.findUser(userId);
    }


}

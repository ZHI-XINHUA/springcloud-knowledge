package com.zxh.springcloud.controller;

import com.zxh.springcloud.entities.CommonResult;
import com.zxh.springcloud.entities.Payment;
import com.zxh.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PaymentController {

    @Autowired //@Autowired是默认按照类型装配的 @Resource默认是按照名称装配的
    PaymentService paymentService;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("====插入成功===");
        if(result>0){
            return new CommonResult(200,"插入成功",null);
        }else{
            return new CommonResult(500,"插入失败",null);
        }
    }


    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("====查询结果成功，payment="+payment);
        int i =10;
        if(payment!=null){
            return  new CommonResult(200,"查询成功",payment);
        }else{
            return new CommonResult(500,"没有查询记录",null);
        }
    }
}

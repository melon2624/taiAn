package com.taian.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/taian")
public class UIcontroller {

    @GetMapping("/price")
    public String toPricePage() {
        return "forward:/price.html";   // 强制转发到静态资源处理器
    }
  /*  @GetMapping("/price2")
    public String toPricePage2() {
        return "forward:/price2.html";   // 强制转发到静态资源处理器
    }*/

    @GetMapping("/updatePrice")
    public String updatePrice() {
        return "forward:/updatePrice.html";
    }

    @GetMapping("/cryptoPrice")
    public String toCryptoPricePage() {
        return "forward:/crypto-price.html";   // 返回加密货币价格页面
    }
}

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
    @GetMapping("/updatePrice")
    public String updatePrice() {
        return "forward:/updatePrice.html";
    }

    /** Bootstrap/CSS 学习中心入口 */
    @GetMapping("/learn")
    public String toLearnHub() {
        return "forward:/learn/index.html";
    }

    /** JavaScript 学习中心入口 */
    @GetMapping("/learn/js")
    public String toJsLearnHub() {
        return "forward:/learn/js/index.html";
    }
}

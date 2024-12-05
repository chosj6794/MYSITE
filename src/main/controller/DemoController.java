package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired; // 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.model.domain.TestDB;
import com.example.demo.model.service.TestService; // TestService 임포트
import java.util.List;


@Controller // 컨트롤러 어노테이션 명시
public class DemoController { 

    @Autowired // TestService 주입
    private TestService testService; // TestService 필드 추가

    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 방갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }

    @GetMapping("/hello2") // 새로운 URL 맵핑
    public String hello2(Model model) {
        // 5개의 속성 추가
        model.addAttribute("name", "홍길동.");
        model.addAttribute("greeting", "방갑습니다.");
        model.addAttribute("when", "오늘.");
        model.addAttribute("weather", "날씨는.");
        model.addAttribute("feel", "매우 좋습니다.");
        return "hello2"; // hello2.html 연결
    }

    @GetMapping("/about_detailed")
    public String about() {
        return "about_detailed";
    }

    @GetMapping("/test1")
    public String thymeleaf_test1(Model model) {
        model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
        model.addAttribute("data2", "태그의 속성 값");
        model.addAttribute("link", 01);
        model.addAttribute("name", "홍길동");
        model.addAttribute("para1", "001");
        model.addAttribute("para2", 002);
        return "thymeleaf_test1";
    }

    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        List<TestDB> users = testService.findAllUsers(); // 모든 사용자 가져오기
        model.addAttribute("users", users); // 모델에 사용자 추가
        return "testdb"; // testdb.html 연결
    }
}

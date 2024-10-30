package com.example.demo.controller;

import com.example.demo.model.service.TestService; // 서비스 클래스 연동
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.model.domain.TestDB;

@Controller
public class DemoController {

    @Autowired
    private TestService testService; // TestService를 Autowired로 주입

    // "/hello" URL로 들어오는 HTTP GET 요청을 처리
    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 방갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }

    @GetMapping("/hello2")
    public String hello2(Model model) {
        model.addAttribute("data2", "홍길동님.");
        model.addAttribute("data3", "방갑습니다.");
        model.addAttribute("data4", "오늘.");
        model.addAttribute("data5", "날씨는.");
        model.addAttribute("data6", "매우 좋습니다.");
        return "hello2"; // hello2.html 페이지 반환
    }

    @GetMapping("/about_detailed")
    public String about(Model model) {
        return "about_detailed"; // about_detailed.html 페이지 반환
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("postId", id);
        return "post_detail"; // post_detail.html 페이지 반환
    }

    @GetMapping("/thymeleaf_test1")
    public String thymeleaf_test1(Model model) {
        // 태그가 포함된 문자열을 안전하게 출력하려면 th:utext를 사용해야 함
        model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
        model.addAttribute("data2", "태그의 속성 값");
        model.addAttribute("link", 1);
        model.addAttribute("name", "홍길동");
        model.addAttribute("para1", "001");
        model.addAttribute("para2", "002");
        return "thymeleaf_test1"; // thymeleaf_test1.html 페이지 반환
    }

    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        // TestDB 엔티티에서 데이터를 가져와 모델에 추가
        TestDB test = testService.findByName("홍길동");
        TestDB test1 = testService.findByName("아저씨");
        TestDB test2 = testService.findByName("꾸러기");

        model.addAttribute("data4", test);
        model.addAttribute("data5", test1);
        model.addAttribute("data6", test2);

        // 디버그를 위해 콘솔에 출력
        System.out.println("데이터 출력 디버그 : " + test);
        System.out.println("데이터 출력 디버그 : " + test1);
        System.out.println("데이터 출력 디버그 : " + test2);
        
        return "testdb"; // testdb.html 페이지 반환
    }
}

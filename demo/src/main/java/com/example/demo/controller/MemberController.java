package com.example.demo.controller;

import com.example.demo.model.service.MemberService;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest; // HttpServletRequest 임포트
import javax.servlet.http.HttpServletResponse; // HttpServletResponse 임포트
import javax.servlet.http.HttpSession; // HttpSession 임포트
import java.util.UUID; // UUID 임포트

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member_login") // 로그인 페이지 연결
    public String member_login() {
        return "login"; // .HTML 연결
    }

    @PostMapping("/api/login_check") // 로그인(아이디, 패스워드) 체크
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model, HttpServletRequest request2, HttpServletResponse response) {
        try {
            HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
            if (session != null) {
                session.invalidate(); // 기존 세션 무효화
                Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID 초기화
                cookie.setPath("/"); // 쿠키 경로
                cookie.setMaxAge(0); // 쿠키 삭제(0으로 설정)
                response.addCookie(cookie); // 응답으로 쿠키 전달
            }
            session = request2.getSession(true); // 새로운 세션 생성

            // 로그인 체크 로직
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드 확인
            String sessionId = UUID.randomUUID().toString(); // 임의의 고유 ID로 세션 생성
            String email = request.getEmail(); // 이메일 얻기
            
            session.setAttribute("userId", sessionId); // 세션에 사용자 ID 설정
            session.setAttribute("email", email); // 세션에 이메일 설정
            
            model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
            return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

    @GetMapping("/api/logout") // 로그아웃 버튼 동작
    public String member_logout(Model model, HttpServletRequest request2, HttpServletResponse response) {
        try {
            HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
            if (session != null) {
                session.invalidate(); // 기존 세션 무효화
                Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID 초기화
                cookie.setPath("/"); // 쿠키 경로 설정
                cookie.setMaxAge(0); // 쿠키 만료를 0으로 설정 (쿠키 삭제)
                response.addCookie(cookie); // 응답으로 쿠키 전달
            }
            // 새로운 세션 생성 (필요 시)
            session = request2.getSession(true); // 새로운 세션 생성
            System.out.println("세션 userId: " + session.getAttribute("userId")); // 초기화 후 IDE 터미널에 세션 값 출력
            return "login"; // 로그인 페이지로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }
}

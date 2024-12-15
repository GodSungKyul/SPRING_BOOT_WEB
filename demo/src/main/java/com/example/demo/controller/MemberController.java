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
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model) {
        try {
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드 반환
            model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
            return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

    // 로그인 체크 메서드 추가
    public Member loginCheck(String email, String rawPassword) {
        Member member = memberRepository.findByEmail(email); // 이메일 조회
        if (member == null) {
            throw new IllegalArgumentException("등록되지 않은 이메일입니다.");
        }
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) { // 비밀번호 확인
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member; // 인증 성공 시 회원 객체 반환
    }
}

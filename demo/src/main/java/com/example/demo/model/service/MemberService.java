package com.example.demo.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.domain.Member;
import com.example.demo.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional // 트랜잭션 처리(클래스 내 모든 메소드 대상)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder

    // 이메일 중복 검사 메서드
    private void validateDuplicateMember(AddMemberRequest request) {
        Member findMember = memberRepository.findByEmail(request.getEmail()); // 이메일 존재 유무 확인
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다."); // 예외 처리
        }
    }

    // 회원 가입 메서드
    public Member saveMember(AddMemberRequest request) {
        validateDuplicateMember(request); // 이메일 체크
        String encodedPassword = passwordEncoder.encode(request.getPassword()); // 비밀번호 해싱
        request.setPassword(encodedPassword); // 암호화된 비밀번호 설정
        return memberRepository.save(request.toEntity()); // Member 객체 저장
    }

    // 추가적인 회원 관련 메서드 구현 가능
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null); // 회원 조회
    }

    public void delete(Long id) {
        memberRepository.deleteById(id); // 회원 삭제
    }

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

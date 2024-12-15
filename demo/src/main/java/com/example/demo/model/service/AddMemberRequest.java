package com.example.demo.model.service;

import com.example.demo.model.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data // getter, setter, toString 등 자동 생성
@Builder // 빌더 패턴을 위한 어노테이션
public class AddMemberRequest {
    private String name;
    private String email;
    private String password;
    private String age;
    private String mobile;
    private String address;

    public Member toEntity() { // Member 객체 생성
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .age(age)
                .mobile(mobile)
                .address(address)
                .build();
    }
}

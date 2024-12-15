package com.example.demo.model.service;

import lombok.*; // 어노테이션 자동 생성
import com.example.demo.model.domain.Article;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddArticleRequest {
    private String title;
    private String content;
    private String user;
    private String newdate;
    private String count;
    private String likec;

    // DTO를 Article 엔티티로 변환하는 메서드
    public Board toEntity() { 
        return Board.builder()
            .title(title)
            .content(content)
            .build();
    }
}

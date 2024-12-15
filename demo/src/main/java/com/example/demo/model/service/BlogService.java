package com.example.demo.model.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BlogRepository;
import com.example.demo.model.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class BlogService {

    @Autowired
    private final BlogRepository blogRepository;

    @Autowired
    private final BoardRepository boardRepository; // 변경된 보드 리포지토리 선언

    // 게시글 수정 (변경하지 않음)
    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id); // 단일글조회
        optionalArticle.ifPresent(article -> { // 값이 있으면
            article.update(request.getTitle(), request.getContent()); // 값을 수정
            blogRepository.save(article); // Article 객체에 저장
        });
    }

    // 새로운 메소드: 게시판 전체 목록 조회 (페이지네이션 포함)
    public Page<Board> findAll(Pageable pageable) { // 게시판 전체 목록 조회
        return boardRepository.findAll(pageable);
    }

    // 새로운 메소드: 게시판 특정 글 조회
    public Optional<Board> findById(Long id) { // 게시판 특정 글 조회
        return boardRepository.findById(id);
    }

    // 게시글 저장 (변경하지 않음)
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    // 게시판 저장 (새로운 메소드 추가)
    public Board save(AddArticleRequest request) {
        // DTO가 없는 경우 직접 Board 객체를 생성하여 저장
        return boardRepository.save(request.toEntity());
    }

    // 게시글 삭제 (변경하지 않음)
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    // 새로운 메소드: 키워드로 게시판 글 검색 (대소문자 무시)
    public Page<Board> searchByKeyword(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable); // LIKE 검색 제공
    }
}

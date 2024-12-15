package com.example.demo.model.service;
import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    // 기존의 보드 리포지토리를 주석처리합니다.
    // @Autowired
    // private final BoardRepository boardRepository;
    // 변경된 이름으로 보드 리포지토리를 선언합니다.
    @Autowired
    private final BoardRepository blogRepository; // 리포지토리 선언
    // 기존 메소드는 주석처리합니다.
    // 게시판 특정 글 조회
    // public Optional<Article> findById(Long id) {
    //     return blogRepository.findById(id);
    // }
    // 게시판 전체 목록 조회
    // public List<Board> findAllBoard(){
    //     return boardRepository.findAll();
    // }
    // 게시글 수정 (변경하지 않음)
    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id); // 단일글조회
        optionalArticle.ifPresent(article -> { //값이 있으면
            article.update(request.getTitle(), request.getContent()); // 값을 수정
            blogRepository.save(article); // Article 객체에 저장
        });
    }
    // 기존 게시판 전체 목록 조회 메소드 주석처리
    // public List<Article> findAll() {
    //     return blogRepository.findAll();
    // }
    // 새로운 메소드: 게시판 전체 목록 조회
    public List<Board> findAll() { // 게시판 전체 목록 조회
        return blogRepository.findAll();
    }
    // 새로운 메소드: 게시판 특정 글 조회
    public Optional<Board> findById(Long id) { // 게시판 특정 글 조회
        return blogRepository.findById(id);
    }
    // 게시글 저장 (변경하지 않음)
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
    // 게시글 삭제 (변경하지 않음)
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}
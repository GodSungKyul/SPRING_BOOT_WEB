package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    BlogService blogService; 

    // 게시판 상세 보기 (board_view.html)
    @GetMapping("/board_view/{id}")
    public String boardView(Model model, @PathVariable Long id) {
        Optional<Board> board = blogService.findById(id); // 게시판 글 조회

        if (board.isPresent()) {
            model.addAttribute("boards", board.get()); // 조회한 게시글을 모델에 추가
        } else {
            // 처리 로직 추가: 게시글이 없을 경우 오류 페이지 반환
            return "/error_page/article_error";
        }
        return "board_view"; // board_view.html 연결
    }

    // 게시판 리스트 보기 (board_list.html) - 수정된 부분
    @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String boardList(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
        PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
        Page<Board> list; // Page를 반환
        
        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
        } else {
            list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
        }
        
        model.addAttribute("boards", list); // 모델에 추가
        model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
        model.addAttribute("currentPage", page); // 페이지 번호
        model.addAttribute("keyword", keyword); // 키워드
        return "board_list"; // .HTML 연결
    }

    // 게시글 추가 맵핑 예시
    @GetMapping("/api/articles")
    public String addArticle(AddArticleRequest request, Model model) {
        blogService.save(request);
        return "redirect:/article_list"; // 게시글 추가 후 목록 페이지로 이동
    }

    // 게시글 삭제
    @DeleteMapping("/api/article_delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }

    // 게시글 수정
    @GetMapping("/article_edit/{id}")
    public String articleEdit(Model model, @PathVariable Long id) {
        Optional<Article> list = blogService.findById(id);
        if (list.isPresent()) {
            model.addAttribute("article", list.get());
        } else {
            return "/error_page/article_error";
        }
        return "article_edit";
    }

    @GetMapping("/board_write")
    public String board_write() {
        return "board_write";
    }
}

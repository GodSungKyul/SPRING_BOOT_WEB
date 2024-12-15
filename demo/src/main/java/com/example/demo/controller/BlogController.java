package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    BlogService blogService; 

    // 기존 코드는 유지하고 필요한 새로운 맵핑만 추가

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

    // 게시판 리스트 보기 (board_list.html)
    @GetMapping("/board_list")
    public String boardList(Model model) {
        List<Board> list = blogService.findAllBoard(); // 모든 게시판 글 조회
        model.addAttribute("boards", list); // 모델에 게시판 데이터 추가
        return "board_list"; // board_list.html로 이동
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
}

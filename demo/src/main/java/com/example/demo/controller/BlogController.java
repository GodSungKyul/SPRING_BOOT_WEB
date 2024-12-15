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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // 게시판 상세 보기 (board_view.html)
    @GetMapping("/board_view/{id}")
    public String boardView(Model model, @PathVariable Long id) {
        Optional<Board> board = blogService.findById(id); // 게시판 글 조회

        if (board.isPresent()) {
            model.addAttribute("board", board.get()); // 조회한 게시글을 모델에 추가
        } else {
            return "error_page/article_error"; // 게시글이 없을 경우 오류 페이지 반환
        }
        return "board_view"; // board_view.html 연결
    }

    // 게시판 리스트 보기 (board_list.html)
    @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String boardList(Model model, 
                            @RequestParam(defaultValue = "0") int page, 
                            @RequestParam(defaultValue = "") String keyword,
                            HttpSession session) { // 세션 객체 전달
        
        String userId = (String) session.getAttribute("userId"); // 세션 아이디 존재 확인
        String email = (String) session.getAttribute("email"); // 세션에서 이메일 확인
        
        if (userId == null) {
            return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
        }

        PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
        Page<Board> list;

        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
        } else {
            list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
        }

        model.addAttribute("boards", list); // 모델에 추가
        model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
        model.addAttribute("currentPage", page); // 페이지 번호
        model.addAttribute("keyword", keyword); // 키워드
        model.addAttribute("userId", userId); // 사용자 ID 추가
        model.addAttribute("email", email); // 로그인 사용자(이메일) 추가

        return "board_list"; // .HTML 연결
    }

    // 게시글 추가
    @GetMapping("/api/articles")
    public String addArticle(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);
        return "redirect:/article_list"; // 게시글 추가 후 목록 페이지로 이동
    }

    // 게시글 삭제
    @DeleteMapping("/api/article_delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list"; // 삭제 후 게시글 목록으로 리다이렉트
    }

    // 게시글 수정
    @GetMapping("/article_edit/{id}")
    public String articleEdit(Model model, @PathVariable Long id) {
        Optional<Article> article = blogService.findById(id);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
        } else {
            return "error_page/article_error"; // 게시글이 없을 경우 오류 페이지 반환
        }
        return "article_edit"; // 수정 페이지 연결
    }

    // 게시글 작성 페이지
    @GetMapping("/board_write")
    public String boardWrite() {
        return "board_write"; // 글쓰기 페이지 연결
    }
}

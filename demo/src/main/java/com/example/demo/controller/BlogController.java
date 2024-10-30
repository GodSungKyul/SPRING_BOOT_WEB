package com.example.demo.controller;

import com.example.demo.model.domain.Article;
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
@RequiredArgsConstructor
public class BlogController {

    @Autowired
    BlogService blogService; 

    @GetMapping("/article_list")
    public String getArticles(Model model) {
        List<Article> articles = blogService.findAll();
        model.addAttribute("articles", articles);
        return "article_list";  // article_list 페이지로 이동
    }

    // GET 요청으로 게시글 추가 (GET 요청으로 변경)
    @GetMapping("/api/articles")
    public String addArticle(AddArticleRequest request, Model model) {
        blogService.save(request);
        return "redirect:/article_list";  // 게시글 추가 후 목록 페이지로 리다이렉트
    }

    @PutMapping("/api/article_edit/{id}")
 public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
 blogService.update(id, request);
 return "redirect:/article_list"; // 글 수정 이후 .html 연결
}

@DeleteMapping("/api/article_delete/{id}")
 public String deleteArticle(@PathVariable Long id) {
 blogService.delete(id);
 return "redirect:/article_list";
 }

 @GetMapping("/article_edit/{id}") // 게시판링크지정
 public String article_edit(Model model, @PathVariable Long id) {
     Optional<Article> list = blogService.findById(id); // 선택한게시판글
     if (list.isPresent()) {
         model.addAttribute("article", list.get()); // 존재하면 Article 객체를 모델에 추가
     } else {
         // 처리할 로직 추가(예: 오류 페이지로 리다이렉트, 예외 처리 등)
         return "/error_page/article_error"; //  오류 처리 페이지로 연결(이름 수정됨)
     }
     return "article_edit"; // article_edit.html 페이지 반환
 }
     }
 




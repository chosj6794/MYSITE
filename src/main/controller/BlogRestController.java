package com.example.demo.controller;

import com.example.demo.model.domain.Article;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RequiredArgsConstructor
// @RestController // @Controller + @ResponseBody
// public class BlogRestController {
//     private final BlogService blogService;

//     @PostMapping("/api/articles")
//     public ResponseEntity<Article> addArticle(@ModelAttribute AddArticleRequest request) {
//         Article savedArticle = blogService.save(request);
//         return ResponseEntity.status(HttpStatus.CREATED)
//                              .body(savedArticle);
//     }

//     @GetMapping("/favicon.ico")
//     public void favicon() {
//         // 아무 작업도 하지 않음
//     }
// }

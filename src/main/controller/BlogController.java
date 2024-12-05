package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;


@Controller
public class BlogController {
    @Autowired
    private BlogService blogService; // BlogService 주입

    // @GetMapping("/article_list")
    // public String articleList(Model model) {
    //     // DB에서 게시글 목록을 가져옴
    //     List<Article> articles = blogService.findAll();
    //     model.addAttribute("articles", articles); // 모델에 추가
    //     return "article_list"; // article_list.html 연결
    // }

    // @PostMapping("/articles") // 게시글 추가를 위한 매핑
    // public String addArticle(@ModelAttribute AddArticleRequest request) {
    //     blogService.save(request); // 게시글 저장
    //     return "redirect:/article_list"; // 전체 목록으로 리다이렉트
    // }

    // @GetMapping("/article_edit/{id}")
    // public String article_edit(Model model, @PathVariable String id) {
    //     try {
    //         Long articleId = Long.parseLong(id); // 문자열을 Long으로 변환
    //         Optional<Article> articleOptional = blogService.findById(articleId);
    //         if (articleOptional.isPresent()) {
    //             model.addAttribute("article", articleOptional.get());
    //         } else {
    //             return "error_page/article_error"; // 존재하지 않는 게시글은 기본 오류 페이지
    //         }
    //     } catch (NumberFormatException e) {
    //         return "error_page/article_error2"; // 문자열 변환 오류 시 새로운 에러 페이지로 연결
    //     }
    //     return "article_edit"; // .HTML 연결
    // }

    // @PostMapping("/api/article_edit/{id}") // 게시글 수정 요청
    // public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    //     blogService.update(id, request); // 게시글 수정
    //     return "redirect:/article_list"; // 글 수정 이후 목록으로 리다이렉트
    // }

    // @PostMapping("/api/article_delete/{id}") // 게시글 삭제 요청
    // public String deleteArticle(@PathVariable Long id) {
    //     blogService.delete(id); // 게시글 삭제
    //     return "redirect:/article_list"; // 삭제 후 목록으로 리다이렉트
    // }

    @GetMapping("/board_view/{id}") // 게시판 링크 지정
    public String board_view(Model model, @PathVariable Long id) {
    Optional<Board> boardOptional = blogService.findById(id); // 선택한 게시판 글
    if (boardOptional.isPresent()) {
    model.addAttribute("board", boardOptional.get()); // 존재할 경우 실제 Article 객체를 모델에 추가
    model.addAttribute("boards", blogService.findAll()); 
    } else {
    // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
    return "/error_page/article_error"; // 오류 처리 페이지로 연결
    }
    return "board_view"; // .HTML 연결
    }

    @GetMapping("/board_edit/{id}") // 게시글 수정 페이지
    public String boardEdit(Model model, @PathVariable Long id) {
    Optional<Board> boardOptional = blogService.findById(id); // 선택한 게시글
    if (boardOptional.isPresent()) {
        model.addAttribute("board", boardOptional.get()); // 게시글 정보를 모델에 추가
    } else {
        return "error_page/article_error"; // 오류 처리 페이지로 연결
    }
    return "board_edit"; // board_edit.html 연결
}

@PostMapping("/api/article_edit/{id}") // 게시글 수정 처리
public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    Board board = Board.builder() // 빌더 패턴 사용
            .title(request.getTitle())
            .content(request.getContent())
            .user(request.getUser())
            .newdate(request.getNewdate())
            .count(request.getCount())
            .likec(request.getLikec())
            .build();

    blogService.update(id, board); // 게시글 업데이트
    return "redirect:/board_list"; // 수정 후 게시글 목록으로 리다이렉트  
}

@GetMapping("/board_write")
public String board_write() {
return "board_write";
}

@PostMapping("/api/boards") // 글쓰기 게시판 저장
public String addboards(@ModelAttribute AddArticleRequest request) {
blogService.save(request);
return "redirect:/board_list"; // .HTML 연결
}
@GetMapping("/board_list") // 새로운 게시판 링크 지정
public String board_list(
    Model model, 
    @RequestParam(defaultValue = "0") int page, 
    @RequestParam(defaultValue = "") String keyword,
    HttpSession session) { // 세션 객체 전달
    String userId = (String) session.getAttribute("userId"); // 세션 아이디 존재 확인
    String email = (String) session.getAttribute("email"); // 세션에서 이메일 확인
    if (userId == null) {
        return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
    }
    System.out.println("세션 userId: " + userId); // 서버 IDE 터미널에 세션 값 출력
    PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
    Page<Board> list; // Page를 반환
    if (keyword.isEmpty()) {
        list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
    } else {
        list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
    }
    int startNum = (pageable.getPageNumber() * pageable.getPageSize()) + 1; // 시작 번호 계산
    model.addAttribute("boards", list); // 모델에 추가
    model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
    model.addAttribute("currentPage", page); // 페이지 번호
    model.addAttribute("keyword", keyword); // 키워드
    model.addAttribute("startNum", startNum);
    model.addAttribute("email", email); // 로그인 사용자(이메일)

    return "board_list"; // .HTML 연결
}


@PostMapping("/api/board_delete") // 게시글 삭제 요청
public String deleteArticle(@RequestParam Long id) {
    blogService.deleteById(id); // 서비스에서 삭제 메소드 호출
    return "redirect:/board_list"; // 삭제 후 게시글 목록으로 리다이렉트
}
}

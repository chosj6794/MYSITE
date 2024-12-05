package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.Member_Service;

import jakarta.validation.Valid;
import java.util.UUID;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.demo.model.domain.Member;
import org.springframework.web.bind.annotation.*;



@Controller
public class MemberController {
    private final Member_Service memberService;

    public MemberController(Member_Service memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/join_new") // 회원 가입 페이지 연결
    public String join_new() {
        return "join_new"; // .HTML 연결
    }

    @PostMapping("/api/members") // 회원 가입 저장
    public String addmembers(@Valid @ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "join_end"; // .HTML 연결
    }

    @GetMapping("/join_end") // 회원 가입 완료 페이지 연결
    public String join_end() {
        return "join_end"; // .HTML 연결
    }

    @GetMapping("/login") // 로그인 페이지 연결
    public String login() {
        return "login"; // .HTML 연결
    }

    @PostMapping("/api/login_check") // 로그인(아이디, 패스워드) 체크
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model,HttpServletRequest request2, HttpServletResponse response) {
        try {
            HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
        if (session != null) {
        session.invalidate(); // 기존 세션 무효화
        Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID 초기화
        cookie.setPath("/"); // 쿠키 경로
        cookie.setMaxAge(0); // 쿠키 삭제(0으로 설정)
        response.addCookie(cookie); // 응답으로 쿠키 전달   
        }
        session = request2.getSession(true); // 새로운 세션 생성

            Member member = memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드 반환
            String sessionId = UUID.randomUUID().toString(); // 임의의 고유 ID로 세션 생성
            String email = request.getEmail(); // 이메일 얻기
            session.setAttribute("userId", sessionId); // 아이디 이름 설정
            session.setAttribute("email", email); // 이메일 설정
            model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
            return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }       
    public ResponseEntity<String> registerMember(@Valid @RequestBody AddMemberRequest request) {
        memberService.addMember(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/api/logout") // 로그아웃 버튼 동작
    public String member_logout(Model model, HttpServletRequest request2, HttpServletResponse response) {
    try {
        HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
            session.invalidate(); // 기존 세션 무효화
            Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID is the default session cookie name
            cookie.setPath("/"); // Set the path for the cookie
            cookie.setMaxAge(0); // Set cookie expiration to 0 (removes the cookie)
            response.addCookie(cookie); // Add cookie to the response
            session = request2.getSession(true); // 새로운 세션 생성s
            System.out.println("세션 userId: " + session.getAttribute("userId" )); // 초기화 후 IDE 터미널에 세션 값 출력
            return "login"; // 로그인 페이지로 리다이렉트
    } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
    }
    }
}

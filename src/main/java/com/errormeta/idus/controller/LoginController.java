package com.errormeta.idus.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.errormeta.idus.config.CommonFunc;
import com.errormeta.idus.models.Member;
import com.errormeta.idus.services.MemberService;

@RestController
public class LoginController {
	private MemberService memberService;

	public LoginController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/api/login")
	public ResponseEntity<Object> login(@RequestParam(name = "id", required = true) String id,
									    @RequestParam(name = "password", required = true) String password) {
		try {
			Map<String, Object> request = new HashMap<String, Object>();
			request.put("id", id);
			request.put("password", password);
		
			Object member = memberService.login(request);
			Member m = (Member) member;
			memberService.tokenRefresh(m.getMember_id(), m.getMember_idx());
			return CommonFunc.responseJson(200, "0000", "Member Login Success", member);
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}

	@PostMapping("/api/logout")
	public ResponseEntity<Object> logout(@RequestParam(name = "member_idx", required = true) int member_idx,
										 @RequestParam(name = "login_token", required = true) String login_token) {
		try {
			Map<String, Object> request = new HashMap<String, Object>();
			request.put("member_idx", member_idx);
			request.put("login_token", login_token);
			int result = memberService.tokenValidation(request);
			if (result == 1) {
				memberService.tokenDelete(member_idx);
				return CommonFunc.responseJson(200, "0000", "Member Logout Success");
			} else {
				return CommonFunc.responseJson(200, "2000", "Member Logout Fail");
			}
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}
}

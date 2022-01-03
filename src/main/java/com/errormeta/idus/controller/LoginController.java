package com.errormeta.idus.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.errormeta.idus.config.CommonFunc;
import com.errormeta.idus.middleware.ApiMiddleware;
import com.errormeta.idus.models.Member;
import com.errormeta.idus.services.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class LoginController {
	private MemberService memberService;
	private ApiMiddleware apiMiddleware;

	public LoginController(MemberService memberService) {
		this.apiMiddleware = new ApiMiddleware();
		this.memberService = memberService;
	}

	@ApiOperation(value="회원 로그인(인증)", notes="회원 로그인 처리")
	@PostMapping("/login")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "Authorization", paramType = "header", defaultValue = "YmFja3BhY2tlcjo6aWR1cw=="),
		@ApiImplicitParam(name = "id", value = "아이디", dataType = "String", paramType = "query", defaultValue = "1", required = true),
		@ApiImplicitParam(name = "password", value = "비밀번호", dataType = "String", paramType = "query", defaultValue = "1", required = true),
	})
	public ResponseEntity<Object> login(@RequestHeader Map<String, String> headers,
										@RequestParam(name = "id", required = true) String id,
									    @RequestParam(name = "password", required = true) String password) {
		try {
			if (apiMiddleware.apiTokenValidation(headers)) return CommonFunc.responseJson(401, "8888", "Invalid Access Key");
			Map<String, Object> request = new HashMap<String, Object>();
			request.put("id", id);
			request.put("password", password);
		
			Object member = memberService.login(request);
			Member m = (Member) member;
			String login_token = memberService.tokenRefresh(m.getMember_id(), m.getMember_idx());
			ObjectMapper om = new ObjectMapper();
			Map<String,Object> jo = om.convertValue(m, Map.class);
			
			jo.put("login_token", login_token); 
			return CommonFunc.responseJson(200, "0000", "Member Login Success", jo);
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}

	@ApiOperation(value="회원 로그아웃", notes="회원 로그아웃 처리")
	@PostMapping("/logout")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "Authorization", paramType = "header", defaultValue = "YmFja3BhY2tlcjo6aWR1cw=="),
		@ApiImplicitParam(name = "member_idx", value = "회원식별자", dataType = "Integer", paramType = "query", defaultValue = "1", required = true),
		@ApiImplicitParam(name = "login_token", value = "로그인토큰", dataType = "String", paramType = "query", defaultValue = "1", required = true),
	})
	public ResponseEntity<Object> logout(@RequestHeader Map<String, String> headers,
										 @RequestParam(name = "member_idx", required = true) int member_idx,
										 @RequestParam(name = "login_token", required = true) String login_token) {
		try {
			if (apiMiddleware.apiTokenValidation(headers)) return CommonFunc.responseJson(401, "8888", "Invalid Access Key");
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

package com.errormeta.idus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.errormeta.idus.config.CommonFunc;
import com.errormeta.idus.middleware.ApiMiddleware;
import com.errormeta.idus.models.Member;
import com.errormeta.idus.models.Orders;
import com.errormeta.idus.services.MemberService;
import com.errormeta.idus.services.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RequestMapping("/api")
@Api(value = "회원")
@RestController
public class MemberController {

	private MemberService memberService;
	private OrderService orderService;
	private ApiMiddleware apiMiddleware;

	public MemberController(MemberService memberService, OrderService orderService) {
		this.apiMiddleware = new ApiMiddleware();
		this.memberService = memberService;
		this.orderService = orderService;
	}

	@ApiOperation(value="회원 가입", notes="회원 가입 처리")
	@PostMapping("/join")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "Authorization", paramType = "header", defaultValue = "YmFja3BhY2tlcjo6aWR1cw=="),
		@ApiImplicitParam(name = "id", value = "아이디", dataType = "String", paramType = "query", defaultValue = "errormeta", required = true),
		@ApiImplicitParam(name = "name", value = "이름", dataType = "String", paramType = "query", defaultValue = "이경필", required = true),
		@ApiImplicitParam(name = "nick", value = "별명", dataType = "String", paramType = "query", defaultValue = "error", required = true),
		@ApiImplicitParam(name = "password", value = "비밀번호", dataType = "String", paramType = "query", defaultValue = "idus1229!@", required = true),
		@ApiImplicitParam(name = "tel", value = "전화번호", dataType = "String", paramType = "query", defaultValue = "01020863327", required = true),
		@ApiImplicitParam(name = "email", value = "이메일", dataType = "String", paramType = "query", defaultValue = "errormeta@gmail.com", required = true),
		@ApiImplicitParam(name = "gender", value = "성별", dataType = "String", paramType = "query", defaultValue = "M", required = false)
	})
	public ResponseEntity<Object> join(@RequestHeader Map<String, String> headers,
									   @RequestParam(name = "id", required = true) String id,
									   @RequestParam(name = "name", required = true) String name,
									   @RequestParam(name = "nick", required = true) String nick,
									   @RequestParam(name = "password", required = true) String password,
									   @RequestParam(name = "tel", required = true) String tel,
									   @RequestParam(name = "email", required = true) String email,
									   @RequestParam(name = "gender", required = false, defaultValue="") String gender) {
		try {
			if (apiMiddleware.apiTokenValidation(headers)) return CommonFunc.responseJson(401, "8888", "Invalid Access Key");
			Map<String, Object> request = new HashMap<String, Object>();
			request.put("id", id);
			request.put("name", name);
			request.put("nick", nick);
			request.put("password", password);
			request.put("tel", tel);
			request.put("email", email);
			if (gender.isEmpty() == false) {
				request.put("gender", gender);	
			}
			
			boolean result = memberService.join(request);
			//boolean result = true;
			if (result) {
				return CommonFunc.responseJson(201, "0000", "Member Join Success");	
			} else {
				return CommonFunc.responseJson(409, "9998", "Member Join Fail");
			}
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}

	@ApiOperation(value="단일 회원 상세 정보 조회", notes="회원 상세정보 조회")
	@GetMapping("/member/{member_idx}")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "Authorization", paramType = "header", defaultValue = "YmFja3BhY2tlcjo6aWR1cw=="),
		@ApiImplicitParam(name = "member_idx", value = "회원식별자", dataType = "Integer", paramType = "path", defaultValue = "1", required = true),
	})
	public ResponseEntity<Object> memberDetail(@RequestHeader Map<String, String> headers, @PathVariable int member_idx) {
		try {
			if (apiMiddleware.apiTokenValidation(headers)) return CommonFunc.responseJson(401, "8888", "Invalid Access Key");
			Object member = memberService.show(member_idx);
			return CommonFunc.responseJson(200, "0000", "Member Info Success", member);
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}

	@ApiOperation(value="단일 회원의 주문 목록 조회", notes="회원 주문정보 조회")
	@GetMapping("/order/{member_idx}")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "Authorization", paramType = "header", defaultValue = "YmFja3BhY2tlcjo6aWR1cw=="),
		@ApiImplicitParam(name = "member_idx", value = "회원식별자", dataType = "Integer", paramType = "path", defaultValue = "1", required = true),
	})
	public ResponseEntity<Object> memberOrders(@RequestHeader Map<String, String> headers, @PathVariable int member_idx) {
		try {
			if (apiMiddleware.apiTokenValidation(headers)) return CommonFunc.responseJson(401, "8888", "Invalid Access Key");
			List<Orders> orders = orderService.list(member_idx);
			List<Object> lo = new ArrayList<Object>();
			for (Orders o : orders) {
				lo.add(o);
			}
			return CommonFunc.responseJson(200, "0000", "Member Order Success", lo);
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}

	@ApiOperation(value="여러 회원 목록 조회", notes="회원 목록 조회")
	@GetMapping("/member")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "Authorization", paramType = "header", defaultValue = "YmFja3BhY2tlcjo6aWR1cw=="),
		@ApiImplicitParam(name = "page", value = "페이지번호", dataType = "Integer", paramType = "query", defaultValue = "1", required = true),
		@ApiImplicitParam(name = "id", value = "아이디", dataType = "String", paramType = "query", defaultValue = "idus", required = false),
		@ApiImplicitParam(name = "email", value = "이메일", dataType = "String", paramType = "query", defaultValue = "error", required = false)
	})
	public ResponseEntity<Object> memberList(@RequestHeader Map<String, String> headers,
											 @RequestParam(name = "page", required = true) int page,
											 @RequestParam(name = "id", required = false) String id,
											 @RequestParam(name = "email", required = false) String email) {
		try {
			if (apiMiddleware.apiTokenValidation(headers)) return CommonFunc.responseJson(401, "8888", "Invalid Access Key");
			Map<String, Object> request = new HashMap<String, Object>();
			request.put("page", page);
			if (id != null) {
				request.put("id", id);
			}
			if (email != null) {
				request.put("email", email);	
			}
	
			List<Member> member = memberService.list(request);
			List<Object> lo = new ArrayList<Object>();
			for (Member m : member) {
				lo.add(m);
			}
			return CommonFunc.responseJson(200, "0000", "Member List Success", lo);
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}
}
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.errormeta.idus.config.CommonFunc;
import com.errormeta.idus.models.Member;
import com.errormeta.idus.models.Orders;
import com.errormeta.idus.services.MemberService;
import com.errormeta.idus.services.OrderService;

@RestController
public class MemberController {

	private MemberService memberService;
	private OrderService orderService;

	public MemberController(MemberService memberService, OrderService orderService) {
		this.memberService = memberService;
		this.orderService = orderService;
	}

	@PostMapping("/api/join")
	public ResponseEntity<Object> join(@RequestParam(name = "id", required = true) String id,
									   @RequestParam(name = "name", required = true) String name,
									   @RequestParam(name = "nick", required = true) String nick,
									   @RequestParam(name = "password", required = true) String password,
									   @RequestParam(name = "tel", required = true) String tel,
									   @RequestParam(name = "email", required = true) String email,
									   @RequestParam(name = "gender", required = false, defaultValue="") String gender) {
		try {
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

	@GetMapping("/api/member/{member_idx}")
	public ResponseEntity<Object> memberDetail(@PathVariable int member_idx) {
		try {
			Object member = memberService.show(member_idx);
			return CommonFunc.responseJson(200, "0000", "Member Info Success", member);
		} catch (Exception e) {
			return CommonFunc.responseJson(500, "9999", "Internal Server Error :: " + e.getMessage());
		}
	}

	@GetMapping("/api/order/{member_idx}")
	public ResponseEntity<Object> memberOrders(@PathVariable int member_idx) {
		try {
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

	@GetMapping("/api/member")
	public ResponseEntity<Object> memberList() {
		try {
			List<Member> member = memberService.list();
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
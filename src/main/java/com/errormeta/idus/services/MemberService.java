package com.errormeta.idus.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.errormeta.idus.models.Member;
import com.errormeta.idus.models.Orders;
import com.errormeta.idus.repository.MemberRepository;

@Service
public class MemberService {
	private MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public boolean join(Map<String, Object> request) {
		try {
			this.memberRepository.join(request);
			return true;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}
	}
	public List<Member> list(Map<String, Object> request) {
		try {
			List<Member> result = this.memberRepository.memberList(request);
			return result;	
		} catch (Exception e) {
			return null;
		}
	}

	public Member show(int member_idx) {
		try {
			Member result = this.memberRepository.memberDetail(member_idx);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public Member login(Map<String, Object> request) {
		try {
			Member result = this.memberRepository.login(request);
			return result;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return null;
		}
	}

	public String tokenRefresh(String id, int idx) {
		try {
			String result = this.memberRepository.tokenRefresh(id, idx);
			return result;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public int tokenValidation(Map<String, Object> request) {
		try {
			int result = this.memberRepository.tokenValidation(request);
			return result;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return -1;
		}
	}

	public void tokenDelete(int member_idx) {
		try {
			this.memberRepository.tokenDelete(member_idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

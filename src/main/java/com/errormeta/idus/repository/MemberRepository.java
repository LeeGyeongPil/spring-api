package com.errormeta.idus.repository;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.errormeta.idus.config.CommonFunc;
import com.errormeta.idus.models.Member;

@Repository
public class MemberRepository {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public MemberRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void join(Map<String, Object> request) {
		String sql;
		try {
			sql = "INSERT INTO Member SET "
					+ "		member_id = :id,"
					+ "		member_name = :name,"
					+ "		member_nickname = :nick,"
					+ "		member_password = '" + CommonFunc.encryptSHA256(request.get("password").toString()) + "',"
					+ "		member_tel = :tel,"
					+ "		member_email = :email";
			if (request.get("gender") != null) {
				sql = sql + ", member_gender = :gender ";
			}
			namedParameterJdbcTemplate.update(sql, request);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Member> memberList() {
		String sql = "SELECT"
				+"		member_idx,"
				+"		member_id,"
				+"		member_name,"
				+"		member_nickname,"
				+"		member_tel,"
				+"		member_email,"
				+"		member_gender,"
				+"		join_datetime,"
				+"		last_login_datetime"
				+"	FROM"
				+"		Member"
				+"	WHERE"
				+"		1 = 1";
		RowMapper<Member> memberMapper = (rs, rowNum) -> {
			Member member = new Member();
			member.setMember_idx(rs.getInt("member_idx"));
			member.setMember_id(rs.getString("member_id"));
			member.setMember_name(rs.getString("member_name"));
			member.setMember_nickname(rs.getString("member_nickname"));
			member.setMember_tel(rs.getString("member_tel"));
			member.setMember_email(rs.getString("member_email"));
			member.setMember_email(rs.getString("member_gender"));
			member.setJoin_datetime(rs.getDate("join_datetime"));
			member.setLast_login_datetime(rs.getDate("last_login_datetime"));
			return member;
		};
		return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), memberMapper);
	}

	public Member memberDetail(int member_idx) {
		String sql = "SELECT"
				+"		member_idx,"
				+"		member_id,"
				+"		member_name,"
				+"		member_nickname,"
				+"		member_tel,"
				+"		member_email,"
				+"		member_gender,"
				+"		join_datetime,"
				+"		last_login_datetime"
				+"	FROM"
				+"		Member"
				+"	WHERE"
				+"		member_idx = " + member_idx;
		RowMapper<Member> memberMapper = (rs, rowNum) -> {
			Member member = new Member();
			member.setMember_idx(rs.getInt("member_idx"));
			member.setMember_id(rs.getString("member_id"));
			member.setMember_name(rs.getString("member_name"));
			member.setMember_nickname(rs.getString("member_nickname"));
			member.setMember_tel(rs.getString("member_tel"));
			member.setMember_email(rs.getString("member_email"));
			member.setMember_gender(rs.getString("member_gender"));
			member.setJoin_datetime(rs.getDate("join_datetime"));
			member.setLast_login_datetime(rs.getDate("last_login_datetime"));
			return member;
		};
		return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), memberMapper);
	}

	public Member login(Map<String, Object> request) {
		try {
			String sql = "SELECT"
					+"		member_idx,"
					+"		member_id,"
					+"		member_name,"
					+"		member_nickname,"
					+"		last_login_datetime"
					+"	FROM"
					+"		Member"
					+"	WHERE"
					+"		member_id = :id"
					+ "		AND member_password = '" + CommonFunc.encryptSHA256(request.get("password").toString()) + "'";

			RowMapper<Member> memberMapper = (rs, rowNum) -> {
				Member member = new Member();
				member.setMember_idx(rs.getInt("member_idx"));
				member.setMember_id(rs.getString("member_id"));
				member.setMember_name(rs.getString("member_name"));
				member.setMember_nickname(rs.getString("member_nickname"));
				member.setLast_login_datetime(rs.getDate("last_login_datetime"));
				
				return member;
			};
			return namedParameterJdbcTemplate.queryForObject(sql, request, memberMapper);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public String tokenRefresh(String id, int idx) {
		try {
			Integer unixTime = (int) System.currentTimeMillis() / 1000;
			String loginToken = CommonFunc.encryptSHA1(id + unixTime.toString() + "idus");

			String sql = "UPDATE Member SET "
					+ "		login_token = '" + loginToken + "',"
					+ "		last_login_datetime = NOW() "
					+ "	WHERE member_idx = " + idx;
			namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());

			return loginToken;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public int tokenValidation(Map<String, Object> request) {
		try {
			String sql = "SELECT"
					+"		COUNT(*)"
					+"	FROM"
					+"		Member"
					+"	WHERE"
					+"		member_idx = '" + request.get("member_idx") + "'"
					+ "		AND login_token = '" + request.get("login_token") + "'";

			return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return -1;
		}
	}

	public void tokenDelete(int member_idx) {
		try {
			String sql = "UPDATE Member SET "
					+ "		login_token = ''"
					+ "	WHERE member_idx = " + member_idx;
			namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

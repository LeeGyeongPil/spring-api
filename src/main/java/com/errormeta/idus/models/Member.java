package com.errormeta.idus.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {
	@ApiModelProperty(example = "회원식별자")
	public Integer member_idx;
	public String member_id;
	public String member_name;
	public String member_nickname;
	public String member_password;
	public String member_tel;
	public String member_email;
	public String member_gender;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	public Date join_datetime;
	public String login_token;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	public Date last_login_datetime;
}

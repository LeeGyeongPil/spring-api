package com.errormeta.idus.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.errormeta.idus.models.BaseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CommonFunc {
	public static ResponseEntity<Object> responseJson(int statusCode, String code, String msg)
	{
		ObjectMapper om = new ObjectMapper();
		HashMap<String,Object> rs = new HashMap<>();
		rs.put("code", code);
		rs.put("message", msg);

		return createReturn(statusCode, rs);
	}

	public static ResponseEntity<Object> responseJson(int statusCode, String code, String msg, Object data)
	{
		ObjectMapper om = new ObjectMapper();
		HashMap<String,Object> rs = new HashMap<>();
		rs.put("code", code);
		rs.put("message", msg);
		Map<String,Object> jo = om.convertValue(data, Map.class);
		rs.put("data", jo);

		return createReturn(statusCode, rs);
	}

	public static ResponseEntity<Object> responseJson(int statusCode, String code, String msg, List<Object> data)
	{
		ObjectMapper om = new ObjectMapper();
		HashMap<String,Object> rs = new HashMap<>();
		rs.put("code", code);
		rs.put("message", msg);
		rs.put("data", data);

		return createReturn(statusCode, rs);
	}

	public static ResponseEntity<Object> createReturn(int statusCode, Object data) {
		HttpStatus httpStatus = null;
		switch (statusCode) {
		case 200:
			httpStatus = HttpStatus.OK;
			break;
		case 201:
			httpStatus = HttpStatus.CREATED;
			break;
		case 400:
			httpStatus = HttpStatus.BAD_REQUEST;
			break;
		case 401:
			httpStatus = HttpStatus.UNAUTHORIZED;
			break;
		case 409:
			httpStatus = HttpStatus.CONFLICT;
			break;
		case 500:
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			break;
		default:
			httpStatus = HttpStatus.OK;
			break;
		}
		return new ResponseEntity<>(data, httpStatus);
	}

    public static String encryptSHA1(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }
 
    public static String encryptSHA256(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}

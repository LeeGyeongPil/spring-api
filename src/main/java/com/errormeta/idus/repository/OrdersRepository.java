package com.errormeta.idus.repository;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.errormeta.idus.models.Orders;

@Repository
public class OrdersRepository {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public OrdersRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public List<Orders> orderList(int member_idx) {
		String sql = "SELECT"
				+"		order_no,"
				+"		product_name,"
				+"		order_price,"
				+"		order_datetime,"
				+"		pay_datetime"
				+"	FROM"
				+"		Orders"
				+"	WHERE"
				+"		member_idx = " + member_idx;
		RowMapper<Orders> orderMapper = (rs, rowNum) -> {
			Orders orders = new Orders();
			orders.setOrder_no(rs.getString("order_no"));
			orders.setProduct_name(rs.getString("product_name"));
			orders.setOrder_price(rs.getInt("order_price"));
			orders.setOrder_datetime(rs.getDate("order_datetime"));
			orders.setPay_datetime(rs.getDate("pay_datetime"));
			return orders;
		};
		return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), orderMapper);
	}

}

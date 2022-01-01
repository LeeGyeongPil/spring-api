package com.errormeta.idus.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.errormeta.idus.models.Orders;
import com.errormeta.idus.repository.OrdersRepository;

@Service
public class OrderService {
	private OrdersRepository orderRepository;

	public OrderService(OrdersRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Orders> list(int member_idx) {
		List<Orders> result = this.orderRepository.orderList(member_idx);
		return result;
	}
}

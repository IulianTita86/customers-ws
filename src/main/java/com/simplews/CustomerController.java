package com.simplews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@PostMapping("/saveOrUpdate")
	public Customer saveOrUpdate(@RequestParam(value="name") String name, @RequestParam(value="email") String email) {
		Customer customer = new Customer(name, email);
		customerService.saveOrUpdate(customer);
		return customer;
	}
	
}

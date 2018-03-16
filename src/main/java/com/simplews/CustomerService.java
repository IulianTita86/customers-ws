package com.simplews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	public static String FILE_PATH = "D:\\customers.json";
	
	public List<Customer> readCustomersFromJsonFIle(){
		List<Customer> customers = new ArrayList<Customer>();
			JSONParser parser = new JSONParser();
			try {
				JSONArray jsonCustomers = (JSONArray) parser.parse(new FileReader(FILE_PATH));
				for(Object o : jsonCustomers) {
					JSONObject jsonCustomer = (JSONObject)o;
					Long id = (Long) jsonCustomer.get("id");
					String name = (String) jsonCustomer.get("name");
					String email = (String) jsonCustomer.get("email");
					customers.add(new Customer(id,name,email));
				}				
			 } catch (FileNotFoundException e) {
		            e.printStackTrace();
		     } catch (IOException e) {
		            e.printStackTrace();
		     } catch (ParseException e) {
		            e.printStackTrace();
		     }
		return customers;
	}
	
	@SuppressWarnings("unchecked")
	public void writeCustomersToJsonFile(List<Customer> customers) {
		JSONArray jsonArray = new JSONArray();
		for (Customer c : customers) {
			JSONObject obj = new JSONObject();
			obj.put("id", c.getId());
			obj.put("name", c.getName());
			obj.put("email", c.getEmail());
			jsonArray.add(obj);
		}		
		try (FileWriter fileWriter = new FileWriter(FILE_PATH)){
			fileWriter.write(jsonArray.toJSONString());
			fileWriter.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public Customer saveOrUpdate(Customer customer) {
		List<Customer> customers = new ArrayList<>();
		File file = new File(FILE_PATH);
		if(file.exists()) {
			// get customers from file
			customers = readCustomersFromJsonFIle();
			Customer customerFound = customers.stream().filter(c -> c.getEmail().equals(customer.getEmail())).findAny().orElse(null);
			if(customerFound == null) {
				// get customers size and set id to the new customer
				long id = setCustomerId(customers);
				customer.setId(id);
				customers.add(customer);
			}else {
				customerFound.setName(customer.getName());
				customer.setId(customerFound.getId());
			}	
			writeCustomersToJsonFile(customers);
		} else {
			long id = setCustomerId(customers);
			customer.setId(id);
			customers.add(customer);
			writeCustomersToJsonFile(customers);
		}		
		return customer;
	}			
	
	// to be able to set the right id for the new customer, i get the size of a list
	private long setCustomerId(List<Customer> customers) {
		long id = customers.size() > 0 ? customers.size() + 1 : 1;
		return id;
	}

}

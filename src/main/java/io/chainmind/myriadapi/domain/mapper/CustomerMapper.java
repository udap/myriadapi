package io.chainmind.myriadapi.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import io.chainmind.myriadapi.domain.dto.CustomerResponse;
import io.chainmind.myriadapi.domain.entity.Customer;

public class CustomerMapper {

	public static CustomerResponse mapToCustomerResponse(Customer customer) {
		CustomerResponse resp = new CustomerResponse();
		resp.setUid(customer.getUid());
		String name = customer.getName();
		if (StringUtils.hasText(name)){
			resp.setName(name);
		}
		resp.setCoverImg(customer.getCoverImg());
		resp.setCellphone(customer.getAccount().getCellphone());
		resp.setTags(customer.getTags());
		return resp;
	}
	public static Page<CustomerResponse> mapToCustomerResponse(Page<Customer> customers) {
		List<CustomerResponse> result = new ArrayList<>();
		customers.forEach(customer -> {
			result.add(mapToCustomerResponse(customer));
		});
		return new PageImpl<CustomerResponse>(result,customers.getPageable(),customers.getTotalElements());
	}


}

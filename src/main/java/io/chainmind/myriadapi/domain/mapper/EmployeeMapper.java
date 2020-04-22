package io.chainmind.myriadapi.domain.mapper;

import org.springframework.beans.BeanUtils;

import io.chainmind.myriadapi.domain.dto.EmployeeResponse;
import io.chainmind.myriadapi.domain.dto.NamedEntityDTO;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;

public class EmployeeMapper {

	public static EmployeeResponse mapToEmploymentResponse(Employee employee) {
		EmployeeResponse resp = new EmployeeResponse();
		BeanUtils.copyProperties(employee, resp);
		Organization org = employee.getOrg();
		resp.setOrg(NamedEntityDTO.build(org.getUid(),org.getName()));
		return resp;
	}

}

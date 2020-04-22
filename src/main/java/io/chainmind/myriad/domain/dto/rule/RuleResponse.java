package io.chainmind.myriad.domain.dto.rule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleResponse {
	private String id;

	// rule name, e.g., "order amount limit"
	// name must be unique in the app scope
	private String name;

	// a description of the rule, e.g. "if order amount is greater than $30 then
	// pass"
	private String description;

	// condition of the rule, e.g. "order.amount > 30"
	// use MVEL expression
	private String condition;

	// actions when condition is met
	// use MVEL expression
	private String action;

	private String type;

	private int priority;

}

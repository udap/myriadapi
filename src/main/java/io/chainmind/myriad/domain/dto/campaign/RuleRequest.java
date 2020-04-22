package io.chainmind.myriad.domain.dto.campaign;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RuleRequest {

    // rule name, e.g., "order amount limit"
    // name must be unique in the app scope
//    @NotBlank(groups = RuleValid.Creator.class)
    @NotBlank
    @Size(max = 45)
    private String name;

    @Size(max = 128)
    private String description;

    // condition of the rule, e.g. "order.amount > 30"
    // use MVEL expression
    @NotNull
    @Size(max = 128)
    private String condition;

    // actions when condition is met
    // use MVEL expression
    @Size(max = 128)
    private String action;

    private Integer priority;


}

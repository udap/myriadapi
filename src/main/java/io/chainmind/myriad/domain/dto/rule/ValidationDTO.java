package io.chainmind.myriad.domain.dto.rule;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidationDTO {

    @NotBlank
    @Size(max = 45)
    private String namespace;

    @NotNull
    @Size(max = 1000)
    private String expression;

    private Integer priority;
    
    private List<Rule> rules;
    
    @Getter
    @Setter
    @Builder
    public static class Rule {
    	private String name;
    	private String option;
    }

}

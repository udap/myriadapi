package io.chainmind.myriad.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaginatedResponse<T> {
    private int total;
    private int page;
    private int size;
    private long totalElements;
    private List<T> entries = new ArrayList<>();

}
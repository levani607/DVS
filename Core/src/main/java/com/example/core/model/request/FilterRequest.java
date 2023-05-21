package com.example.core.model.request;

import lombok.Data;
import org.springframework.data.domain.Sort;
@Data
public class FilterRequest {
    String name;
    Integer page;
    Integer size;
    Sort.Direction direction;
    String sortBy;
}

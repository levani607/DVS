package com.example.processor.model.documents;

import com.github.difflib.patch.DeltaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Change {

    private DeltaType type;
    private Integer position;
    private Integer size;
    private List<String> lines;
}

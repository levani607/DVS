package com.example.processor.model.documents;

import com.github.difflib.patch.DeltaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChange {

    @Field(type = FieldType.Nested,includeInParent = true)
    private List<Change> changes;
    private Long versionId;
}

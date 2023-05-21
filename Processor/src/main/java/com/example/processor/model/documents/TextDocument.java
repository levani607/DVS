package com.example.processor.model.documents;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Document(indexName = "textdocuments")
@AllArgsConstructor
@NoArgsConstructor
public class TextDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Nested,includeInParent = true)
    private List<DocumentChange> documentChanges;

    public TextDocument(String text) {
        this.text = text;
    }
}

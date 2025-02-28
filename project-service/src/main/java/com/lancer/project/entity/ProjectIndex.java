package com.lancer.project.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(indexName = "projects") // Tên index trong Elasticsearch
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectIndex {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String categoryId;

    @Field(type = FieldType.Double)
    private BigDecimal budgetMin;

    @Field(type = FieldType.Double)
    private BigDecimal budgetMax;

    @Field(type = FieldType.Keyword)
    private List<String> skills;

    @Field(type = FieldType.Keyword)
    private String address;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Date)
    private Instant deadline;
}

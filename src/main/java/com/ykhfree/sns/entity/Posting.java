package com.ykhfree.sns.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("posting")
public class Posting {
    @Id
    private Long id;

    private String userId;

    private String contents;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

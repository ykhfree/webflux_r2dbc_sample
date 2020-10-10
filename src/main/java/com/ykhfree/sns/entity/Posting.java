package com.ykhfree.sns.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("posting")
@AllArgsConstructor
@NoArgsConstructor
public class Posting {
    @Id
    private Long id;

    private String userId;

    private String contents;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

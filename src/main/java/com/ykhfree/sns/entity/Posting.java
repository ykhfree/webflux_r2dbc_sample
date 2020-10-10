package com.ykhfree.sns.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Table("posting")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Posting {
    @Id
    private Long id;

    private String userId;

    private String contents;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

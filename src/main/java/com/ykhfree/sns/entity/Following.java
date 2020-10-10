package com.ykhfree.sns.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("following")
public class Following {
    @Id
    private Long id;

    private String userId;

    private String followUserId;

    private LocalDateTime createdDate;
}

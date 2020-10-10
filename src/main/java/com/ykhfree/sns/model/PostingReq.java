package com.ykhfree.sns.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostingReq {

    private Long id;

    private String userId;

    private String contents;

}

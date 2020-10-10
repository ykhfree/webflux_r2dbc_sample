package com.ykhfree.sns.service;

import com.ykhfree.sns.dto.FollowingDto;
import com.ykhfree.sns.dto.PostingDto;
import com.ykhfree.sns.model.FollowingReq;
import com.ykhfree.sns.model.PostingReq;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SnsService {

    Flux<PostingDto> getNewsFeeds(String userId);

    Mono<PostingDto> insertPosting(PostingReq postingReq);

    Mono<PostingDto> updatePosting(PostingReq postingReq);

    Mono<Void> deletePosting(PostingReq postingReq);

    Flux<FollowingDto> getFollowingInfo(String userId);

    Mono<FollowingDto> insertFollowingInfo(FollowingReq followingReq);

    Mono<Void> deleteFollowingInfo(FollowingReq followingReq);
}

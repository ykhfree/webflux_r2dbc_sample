package com.ykhfree.sns.repository;

import com.ykhfree.sns.entity.Following;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FollowingRepository extends ReactiveCrudRepository<Following, Long> {

    @Query("select follow_user_id from following where user_id = :userId")
    Flux<String> findAllByUserId(String userId);

    Mono<Following> findByUserIdAndFollowUserId(String userId, String followUserId);
}

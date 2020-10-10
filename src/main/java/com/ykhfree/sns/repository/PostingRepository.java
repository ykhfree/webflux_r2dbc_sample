package com.ykhfree.sns.repository;

import com.ykhfree.sns.entity.Posting;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostingRepository extends ReactiveCrudRepository<Posting, Long> {

    Mono<Posting> findByIdAndUserId(Long id, String userId);

    Flux<Posting> findByUserIdInOrderByCreatedDateDesc(List<String> ids);

}

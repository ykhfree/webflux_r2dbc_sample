package com.ykhfree.sns.handler;

import com.ykhfree.sns.model.FollowingReq;
import com.ykhfree.sns.model.PostingReq;
import com.ykhfree.sns.service.SnsService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SnsHandler {

    final SnsService snsService;

    public SnsHandler(SnsService snsService) {
        this.snsService = snsService;
    }

    public Mono<ServerResponse> getNewsFeeds(ServerRequest serverRequest) {

        return snsService.getNewsFeeds(serverRequest.pathVariable("userId"))
                .collectList()
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> insertPosting(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(PostingReq.class)
                .map(postingReq -> {
                    postingReq.setUserId(serverRequest.pathVariable("userId"));
                    return postingReq;
                })
                .flatMap(snsService::insertPosting)
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> updatePosting(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(PostingReq.class)
                .map(postingReq -> {
                    postingReq.setId(Long.valueOf(serverRequest.pathVariable("id")));
                    postingReq.setUserId(serverRequest.pathVariable("userId"));
                    return postingReq;
                })
                .flatMap(snsService::updatePosting)
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> deletePosting(ServerRequest serverRequest) {

        PostingReq postingReq = PostingReq.builder()
                .id(Long.valueOf(serverRequest.pathVariable("id")))
                .userId(serverRequest.pathVariable("userId"))
                .build();

        return snsService.deletePosting(postingReq)
                .flatMap(result -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getFollowingInfo(ServerRequest serverRequest) {

        return snsService.getFollowingInfo(serverRequest.pathVariable("userId"))
                .collectList()
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> insertFollowingInfo(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(FollowingReq.class)
                .map(followingReq -> {
                    followingReq.setUserId(serverRequest.pathVariable("userId"));
                    return followingReq;
                })
                .flatMap(snsService::insertFollowingInfo)
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> deleteFollowingInfo(ServerRequest serverRequest) {

        FollowingReq followingReq = FollowingReq.builder()
                .userId(serverRequest.pathVariable("userId"))
                .followUserId(serverRequest.pathVariable("followUserId"))
                .build();

        return snsService.deleteFollowingInfo(followingReq)
                .flatMap(result -> ServerResponse.noContent().build());
    }
}

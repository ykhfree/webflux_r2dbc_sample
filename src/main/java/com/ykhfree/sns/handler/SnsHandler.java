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

        return snsService.getNewsFeeds(getPathVariable(serverRequest, "userId"))
                .collectList()
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> insertPosting(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(PostingReq.class)
                .map(postingReq -> {
                    postingReq.setUserId(getPathVariable(serverRequest, "userId"));
                    return postingReq;
                })
                .flatMap(snsService::insertPosting)
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> updatePosting(ServerRequest serverRequest) {

        Long id = Long.valueOf(getPathVariable(serverRequest, "id"));
        String userId = getPathVariable(serverRequest, "userId");

        return serverRequest.bodyToMono(PostingReq.class)
                .map(postingReq -> {
                    postingReq.setId(id);
                    postingReq.setUserId(userId);
                    return postingReq;
                })
                .flatMap(snsService::updatePosting)
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> deletePosting(ServerRequest serverRequest) {

        PostingReq postingReq = PostingReq.builder()
                .id(Long.valueOf(getPathVariable(serverRequest, "id")))
                .userId(getPathVariable(serverRequest, "userId"))
                .build();

        return snsService.deletePosting(postingReq)
                .flatMap(result -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getFollowingInfo(ServerRequest serverRequest) {

        return snsService.getFollowingInfo(getPathVariable(serverRequest, "userId"))
                .collectList()
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> insertFollowingInfo(ServerRequest serverRequest) {

        String userId = getPathVariable(serverRequest, "userId");

        return serverRequest.bodyToMono(FollowingReq.class)
                .map(followingReq -> {
                    followingReq.setUserId(userId);
                    return followingReq;
                })
                .flatMap(snsService::insertFollowingInfo)
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)));
    }

    public Mono<ServerResponse> deleteFollowingInfo(ServerRequest serverRequest) {

        FollowingReq followingReq = FollowingReq.builder()
                .userId(getPathVariable(serverRequest, "userId"))
                .followUserId(getPathVariable(serverRequest, "followUserId"))
                .build();

        return snsService.deleteFollowingInfo(followingReq)
                .flatMap(result -> ServerResponse.noContent().build());
    }

    private String getPathVariable(ServerRequest serverRequest, String paramName) {

        return serverRequest.pathVariable(paramName);
    }
}

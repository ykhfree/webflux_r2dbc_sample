package com.ykhfree.sns.router;

import com.ykhfree.sns.handler.SnsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class SnsRouter {

    @Bean
    public RouterFunction<?> snsRouters(SnsHandler snsHandler) {

        return RouterFunctions
                .route(GET("/v1/{userId}/feeds").and(accept(MediaType.APPLICATION_JSON)), snsHandler::getNewsFeeds)
                .andRoute(POST("/v1/{userId}/post").and(accept(MediaType.APPLICATION_JSON)), snsHandler::insertPosting)
                .andRoute(PUT("/v1/{id}/{userId}/post").and(accept(MediaType.APPLICATION_JSON)), snsHandler::updatePosting)
                .andRoute(DELETE("/v1/{id}/{userId}/post").and(accept(MediaType.APPLICATION_JSON)), snsHandler::deletePosting)
                .andRoute(GET("/v1/{userId}/follow").and(accept(MediaType.APPLICATION_JSON)), snsHandler::getFollowingInfo)
                .andRoute(POST("/v1/{userId}/follow").and(accept(MediaType.APPLICATION_JSON)), snsHandler::insertFollowingInfo)
                .andRoute(DELETE("/v1/{userId}/{followUserId}/follow").and(accept(MediaType.APPLICATION_JSON)), snsHandler::deleteFollowingInfo);
    }
}

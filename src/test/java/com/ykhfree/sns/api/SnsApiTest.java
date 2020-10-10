package com.ykhfree.sns.api;

import com.ykhfree.sns.config.DatabaseConfiguration;
import com.ykhfree.sns.dto.FollowingDto;
import com.ykhfree.sns.dto.PostingDto;
import com.ykhfree.sns.handler.SnsHandler;
import com.ykhfree.sns.model.FollowingReq;
import com.ykhfree.sns.model.PostingReq;
import com.ykhfree.sns.router.SnsRouter;
import com.ykhfree.sns.service.SnsService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@RunWith(SpringRunner.class)
@WebFluxTest
@AutoConfigureRestDocs
@Import({SnsHandler.class, DatabaseConfiguration.class})
public class SnsApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SnsService snsService;

    @Autowired
    private SnsHandler snsHandler;

    private String userId = "ykhfree";
    private List<PostingDto> postingDtos = new ArrayList<>();

    @Rule
    public final JUnitRestDocumentation jUnitRestDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() {

        webTestClient = WebTestClient.bindToRouterFunction(new SnsRouter().snsRouters(snsHandler))
                .configureClient()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(jUnitRestDocumentation))
                .build();

        postingDtos.add(PostingDto.builder()
                .id(1L)
                .userId("ykhfree")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,10,1,0))
                .build()
        );
        postingDtos.add(PostingDto.builder()
                .id(2L)
                .userId("ykhfree")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,23,1,0))
                .updatedDate(LocalDateTime.of(2020,10,24,1,0))
                .build()
        );
        postingDtos.add(PostingDto.builder()
                .id(3L)
                .userId("superman")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,27,1,0))
                .build()
        );
        postingDtos.add(PostingDto.builder()
                .id(4L)
                .userId("superman")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,29,1,0))
                .build()
        );
        postingDtos.sort((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()));
    }

    @Test
    public void getNewsFeeds() {
        //given
        given(snsService.getNewsFeeds(userId)).willReturn(Flux.fromIterable(postingDtos));

        //when
        List<PostingDto> postingDtos = webTestClient
                .get()
                .uri("/v1/{userId}/feeds", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PostingDto.class)
                .consumeWith(
                        document("get-news-feeds",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userId").description("작성자 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("포스트 ID"),
                                        fieldWithPath("[].userId").type(JsonFieldType.STRING).description("작성자 ID"),
                                        fieldWithPath("[].contents").type(JsonFieldType.STRING).description("피드내용"),
                                        fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("생성일자"),
                                        fieldWithPath("[].updatedDate").type(JsonFieldType.STRING).description("수정일자").optional()
                                )
                        )
                )
                .returnResult()
                .getResponseBody();

        //then
        assertThat(postingDtos).extracting("userId").contains("ykhfree","ykhfree","superman","superman");

    }

    @Test
    public void insertPosting() {
        //given
        String contents = "내용입니다.";
        PostingReq postingReq = PostingReq.builder()
                .userId(this.userId)
                .contents(contents)
                .build();

        PostingDto postingDtoReturn = PostingDto.builder()
                .id(1L)
                .userId(this.userId)
                .contents(contents)
                .createdDate(LocalDateTime.of(2020,10,10,3,20))
                .build();

        given(snsService.insertPosting(postingReq)).willReturn(Mono.just(postingDtoReturn));

        //when
        String body = "{" +
                "\"contents\" : \"" + contents + "\"}";

        PostingDto postingDto = webTestClient
                .post()
                .uri("/v1/{userId}/post", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PostingDto.class)
                .consumeWith(
                        document("insert-posting",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userId").description("작성자 ID")
                                ),
                                requestFields(
                                        fieldWithPath("contents").description("피드내용")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("포스트 ID"),
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("작성자 ID"),
                                        fieldWithPath("contents").type(JsonFieldType.STRING).description("피드내용"),
                                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일자"),
                                        fieldWithPath("updatedDate").type(JsonFieldType.STRING).description("수정일자").optional()
                                )
                        )
                )
                .returnResult()
                .getResponseBody();

        //then
        assertThat(postingDto).extracting("userId").isEqualTo("ykhfree");

    }

    @Test
    public void updatePosting() {
        //given
        String contents = "수정 내용입니다.";
        Long id = 1L;

        PostingReq postingReq = PostingReq.builder()
                .id(id)
                .userId(this.userId)
                .contents(contents)
                .build();

        PostingDto postingDtoReturn = PostingDto.builder()
                .id(id)
                .userId(this.userId)
                .contents(contents)
                .createdDate(LocalDateTime.of(2020,10,11,3,20))
                .updatedDate(LocalDateTime.of(2020,10,12,3,20))
                .build();

        given(snsService.updatePosting(postingReq)).willReturn(Mono.just(postingDtoReturn));

        //when
        String body = "{" +
                "\"contents\" : \"" + contents + "\"}";

        PostingDto postingDto = webTestClient
                .put()
                .uri("/v1/{id}/{userId}/post", 1, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PostingDto.class)
                .consumeWith(
                        document("update-posting",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("포스트 ID"),
                                        parameterWithName("userId").description("작성자 ID")
                                ),
                                requestFields(
                                        fieldWithPath("contents").description("피드내용")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("포스트 ID"),
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("작성자 ID"),
                                        fieldWithPath("contents").type(JsonFieldType.STRING).description("피드내용"),
                                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일자"),
                                        fieldWithPath("updatedDate").type(JsonFieldType.STRING).description("수정일자").optional()
                                )
                        )
                )
                .returnResult()
                .getResponseBody();

        //then
        assertThat(postingDto).extracting("contents").isEqualTo(contents);

    }

    @Test
    public void deletePosting() {
        //given
        Long id = 1L;

        PostingReq postingReq = PostingReq.builder()
                .id(id)
                .userId(this.userId)
                .build();

        given(snsService.deletePosting(postingReq)).willReturn(Mono.empty());

        //when
        webTestClient
                .delete()
                .uri("/v1/{id}/{userId}/post", 1, userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(
                        document("delete-posting",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("포스트 ID"),
                                        parameterWithName("userId").description("작성자 ID")
                                )
                        )
                );
    }

    @Test
    public void getFollowingInfo() {
        //given
        List<FollowingDto> ids = new ArrayList<>();
        ids.add(FollowingDto.builder().followUserId("superman1").build());
        ids.add(FollowingDto.builder().followUserId("superman2").build());
        ids.add(FollowingDto.builder().followUserId("superman3").build());
        given(snsService.getFollowingInfo(userId)).willReturn(Flux.fromIterable(ids));

        //when
        List<FollowingDto> idsReturn = webTestClient
                .get()
                .uri("/v1/{userId}/follow", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(FollowingDto.class)
                .consumeWith(
                        document("get-following-info",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userId").description("작성자 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[].followUserId").type(JsonFieldType.STRING).description("팔로잉 대상자 ID")
                                )
                        )
                )
                .returnResult()
                .getResponseBody();

        //then
        assertThat(idsReturn).extracting("followUserId").contains("superman1","superman2","superman3");

    }

    @Test
    public void insertFollowingInfo() {
        //given
        String followUserId = "superman3";
        FollowingReq followingReq = FollowingReq.builder()
                .userId(this.userId)
                .followUserId(followUserId)
                .build();

        FollowingDto followingDtoReturn = FollowingDto.builder()
                .id(1L)
                .userId(this.userId)
                .followUserId(followUserId)
                .createdDate(LocalDateTime.of(2020,10,10,3,20))
                .build();

        given(snsService.insertFollowingInfo(followingReq)).willReturn(Mono.just(followingDtoReturn));

        //when
        String body = "{" +
                "\"followUserId\" : \"" + followUserId + "\"}";

        FollowingDto followingDto = webTestClient
                .post()
                .uri("/v1/{userId}/follow", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(FollowingDto.class)
                .consumeWith(
                        document("insert-following",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userId").description("작성자 ID")
                                ),
                                requestFields(
                                        fieldWithPath("followUserId").description("팔로우 대상 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("포스트 ID"),
                                        fieldWithPath("userId").type(JsonFieldType.STRING).description("작성자 ID"),
                                        fieldWithPath("followUserId").type(JsonFieldType.STRING).description("팔로우 대상 ID"),
                                        fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일자")
                                )
                        )
                )
                .returnResult()
                .getResponseBody();

        //then
        assertThat(followingDto).extracting("followUserId").isEqualTo(followUserId);

    }

    @Test
    public void deleteFollowing() {
        //given
        String followUserId = "superman3";
        FollowingReq followingReq = FollowingReq.builder()
                .userId(this.userId)
                .followUserId(followUserId)
                .build();

        given(snsService.deleteFollowingInfo(followingReq)).willReturn(Mono.empty());

        //when
        webTestClient
                .delete()
                .uri("/v1/{userId}/{followUserId}/follow", userId, followUserId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(
                        document("delete-following",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("userId").description("작성자 ID"),
                                        parameterWithName("followUserId").description("삭제 대상자 ID")
                                )
                        )
                );
    }
}

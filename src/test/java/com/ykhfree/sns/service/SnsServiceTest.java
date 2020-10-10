package com.ykhfree.sns.service;

import com.ykhfree.sns.dto.FollowingDto;
import com.ykhfree.sns.dto.PostingDto;
import com.ykhfree.sns.entity.Following;
import com.ykhfree.sns.entity.Posting;
import com.ykhfree.sns.model.FollowingReq;
import com.ykhfree.sns.model.PostingReq;
import com.ykhfree.sns.repository.FollowingRepository;
import com.ykhfree.sns.repository.PostingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SnsServiceTest {

    PostingRepository postingRepository = mock(PostingRepository.class);

    FollowingRepository followingRepository = mock(FollowingRepository.class);

    @InjectMocks
    SnsService snsService = new SnsServiceImpl(postingRepository, followingRepository);

    private String userId;
    private String followUserId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.userId = "ykhfree";
        this.followUserId = "ilovelarc";
    }

    @Test
    public void getFeedsTest() {
        mockingRepositoryGetFeeds();
        
        Flux<PostingDto> flux = snsService.getNewsFeeds(userId)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(flux)
                .expectNextCount(4)
                .verifyComplete();

        Mockito.verify(followingRepository, Mockito.atLeastOnce()).findAllByUserId(anyString());
        Mockito.verify(postingRepository, Mockito.atLeastOnce()).findByUserIdInOrderByCreatedDateDesc(anyList());
    }

    @Test
    public void insertPostingTest() {

        String contents = "포스트 입력 테스트";

        mockingRepositoryInsertPosting(contents);

        PostingReq postingReq = PostingReq.builder()
                .userId(this.userId)
                .contents(contents)
                .build();

        Mono<PostingDto> mono = snsService.insertPosting(postingReq)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(mono)
                .assertNext(postingDto -> assertThat(postingDto.getContents().equals(contents)))
                .verifyComplete();

        Mockito.verify(postingRepository, Mockito.atLeastOnce()).save(any(Posting.class));
    }

    @Test
    public void updatePostingTest() {

        Long id = 1L;
        String contents = "포스트 수정 테스트";

        mockingRepositoryUpdatePosting(id, contents);

        PostingReq postingReq = PostingReq.builder()
                .id(id)
                .userId(this.userId)
                .contents(contents)
                .build();

        Mono<PostingDto> mono = snsService.updatePosting(postingReq)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(mono)
                .assertNext(postingDto -> {
                    assertThat(postingDto.getContents().equals(contents));
                    assertThat(postingDto.getCreatedDate().equals(LocalDateTime.of(2020,10,10,3,20)));
                })
                .verifyComplete();

        Mockito.verify(postingRepository, Mockito.atLeastOnce()).findByIdAndUserId(any(Long.class), anyString());
        Mockito.verify(postingRepository, Mockito.atLeastOnce()).save(any(Posting.class));
    }

    @Test
    public void deletePostingTest() {

        Long id = 1L;

        mockingRepositoryDeletePosting(id);

        PostingReq postingReq = PostingReq.builder()
                .id(id)
                .userId(this.userId)
                .build();

        Mono<Void> mono = snsService.deletePosting(postingReq)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(mono)
                .verifyComplete();

        Mockito.verify(postingRepository, Mockito.atLeastOnce()).findByIdAndUserId(any(Long.class), anyString());
        Mockito.verify(postingRepository, Mockito.atLeastOnce()).delete(any(Posting.class));
    }

    @Test
    public void getFollowInfoTest() {
        mockingRepositoryGetFollowInfo();

        Flux<FollowingDto> flux = snsService.getFollowingInfo(userId)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();

        Mockito.verify(followingRepository, Mockito.atLeastOnce()).findAllByUserId(anyString());
    }

    @Test
    public void insertFollowInfoTest() {

        mockingRepositoryInsertFollowing();

        FollowingReq followingReq = FollowingReq.builder()
                .userId(this.userId)
                .followUserId(followUserId)
                .build();

        Mono<FollowingDto> mono = snsService.insertFollowingInfo(followingReq)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(mono)
                .assertNext(followingDto -> assertThat(followingDto.getFollowUserId().equals(this.followUserId)))
                .verifyComplete();

        Mockito.verify(followingRepository, Mockito.atLeastOnce()).save(any(Following.class));
    }

    @Test
    public void deleteFollowInfoTest() {

        mockingRepositoryDeleteFollowing();

        FollowingReq followingReq = FollowingReq.builder()
                .userId(this.userId)
                .followUserId(followUserId)
                .build();

        Mono<Void> mono = snsService.deleteFollowingInfo(followingReq)
                .subscribeOn(Schedulers.single());

        StepVerifier.create(mono)
                .verifyComplete();

        Mockito.verify(followingRepository, Mockito.atLeastOnce()).findByUserIdAndFollowUserId(anyString(), anyString());
        Mockito.verify(followingRepository, Mockito.atLeastOnce()).delete(any(Following.class));
    }

    private void mockingRepositoryGetFeeds() {

        List<String> ids = new ArrayList<>();
        ids.add("ilovelarc");
        ids.add("superman");

        List<String> idsWithUserId = new ArrayList<>(ids);
        idsWithUserId.add(this.userId);

        List<Posting> postings = new ArrayList<>();
        postings.add(Posting.builder()
                .id(1L)
                .userId("ykhfree")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,10,1,0))
                .build()
        );
        postings.add(Posting.builder()
                .id(2L)
                .userId("ykhfree")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,23,1,0))
                .build()
        );
        postings.add(Posting.builder()
                .id(3L)
                .userId("superman")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,27,1,0))
                .build()
        );
        postings.add(Posting.builder()
                .id(4L)
                .userId("superman")
                .contents("피드 내용입니다.")
                .createdDate(LocalDateTime.of(2020,10,29,1,0))
                .build()
        );

        Mockito.when(followingRepository.findAllByUserId(this.userId))
                .thenReturn(Flux.fromIterable(ids));
        Mockito.when(postingRepository.findByUserIdInOrderByCreatedDateDesc(idsWithUserId))
                .thenReturn(Flux.fromIterable(postings));
    }

    private void mockingRepositoryGetFollowInfo() {

        List<String> ids = new ArrayList<>();
        ids.add("superman1");
        ids.add("superman2");
        ids.add("superman3");

        Mockito.when(followingRepository.findAllByUserId(this.userId))
                .thenReturn(Flux.fromIterable(ids));
    }

    private void mockingRepositoryInsertPosting(String contents) {

        Posting postingReturn = Posting.builder()
                .id(1L)
                .userId(this.userId)
                .contents(contents)
                .createdDate(LocalDateTime.of(2020,10,10,3,20))
                .build();

        Mockito.when(postingRepository.save(any(Posting.class)))
                .thenReturn(Mono.just(postingReturn));
    }

    private void mockingRepositoryUpdatePosting(Long id, String contents) {

        Posting posting = Posting.builder()
                .id(id)
                .userId(this.userId)
                .contents("수정 전입니다.")
                .createdDate(LocalDateTime.of(2020,10,9,3,20))
                .build();

        Posting postingReturn = Posting.builder()
                .id(id)
                .userId(this.userId)
                .contents(contents)
                .createdDate(LocalDateTime.of(2020,10,9,3,20))
                .updatedDate(LocalDateTime.of(2020,10,10,3,20))
                .build();

        Mockito.when(postingRepository.findByIdAndUserId(id, userId))
                .thenReturn(Mono.just(posting));
        Mockito.when(postingRepository.save(any(Posting.class)))
                .thenReturn(Mono.just(postingReturn));
    }

    private void mockingRepositoryDeletePosting(Long id) {

        Posting posting = Posting.builder()
                .id(id)
                .userId(this.userId)
                .contents("삭제 대상!!!")
                .createdDate(LocalDateTime.of(2020,10,9,3,20))
                .build();

        Mockito.when(postingRepository.findByIdAndUserId(id, userId))
                .thenReturn(Mono.just(posting));
        Mockito.when(postingRepository.delete(posting))
                .thenReturn(Mono.empty());
    }

    private void mockingRepositoryInsertFollowing() {

        Following following = Following.builder()
                .id(1L)
                .userId(this.userId)
                .followUserId(this.followUserId)
                .createdDate(LocalDateTime.of(2020,10,10,3,20))
                .build();

        Mockito.when(followingRepository.save(any(Following.class)))
                .thenReturn(Mono.just(following));
    }

    private void mockingRepositoryDeleteFollowing() {

        Following following = Following.builder()
                .id(1L)
                .userId(this.userId)
                .followUserId(this.followUserId)
                .createdDate(LocalDateTime.of(2020,10,10,3,20))
                .build();

        Mockito.when(followingRepository.findByUserIdAndFollowUserId(this.userId, this.followUserId))
                .thenReturn(Mono.just(following));
        Mockito.when(followingRepository.delete(following))
                .thenReturn(Mono.empty());
    }
}

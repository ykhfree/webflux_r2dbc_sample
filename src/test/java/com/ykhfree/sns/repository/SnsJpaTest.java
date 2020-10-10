package com.ykhfree.sns.repository;

import com.ykhfree.sns.entity.Following;
import com.ykhfree.sns.entity.Posting;
import com.ykhfree.sns.repository.FollowingRepository;
import com.ykhfree.sns.repository.PostingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SnsJpaTest {

    @Autowired
    PostingRepository postingRepository;

    @Autowired
    FollowingRepository followingRepository;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    public void insertPostings() {
        Posting posting1 = Posting.builder()
                .contents("22222222")
                .userId("ykhfree")
                .createdDate(LocalDateTime.of(2020,10,11,12,12))
                .build();

        Posting posting2 = Posting.builder()
                .contents("51515151")
                .userId("ykhfree")
                .createdDate(LocalDateTime.of(2020,10,11,12,12))
                .build();

        Posting posting3 = Posting.builder()
                .contents("51151511515151")
                .userId("ilovelarc_2")
                .createdDate(LocalDateTime.of(2020,10,11,12,12))
                .build();

        List<Posting> params = new ArrayList<>();
        params.add(posting1);
        params.add(posting2);
        params.add(posting3);

        postingRepository.saveAll(params).blockLast(Duration.ofSeconds(5));

        postingRepository.findAll()
                .doOnNext(posting -> {
                    System.out.println(posting.getId());
                    System.out.println(posting.getContents());
                })
                .blockLast(Duration.ofSeconds(5));
    }

    @Test
    public void insertFollowing() {
        Following following = Following.builder()
                .userId("ykhfree")
                .followUserId("ilovelarc_2")
                .createdDate(LocalDateTime.now())
                .build();

        followingRepository.save(following).block(Duration.ofSeconds(5));
        followingRepository.findAll()
                .doOnNext(following1 -> {
                    System.out.println(following1.getUserId());
                })
                .blockLast(Duration.ofSeconds(5));
    }

    @Test
    public void getPostings() {
        postingRepository.findAll()
                .doOnNext(posting -> {
                    System.out.println(posting.getId());
                    System.out.println(posting.getUserId());
                    System.out.println(posting.getContents());
                })
                .blockLast(Duration.ofSeconds(5));
    }
}

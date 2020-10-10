package com.ykhfree.sns.service;

import com.ykhfree.sns.dto.FollowingDto;
import com.ykhfree.sns.dto.PostingDto;
import com.ykhfree.sns.entity.Following;
import com.ykhfree.sns.entity.Posting;
import com.ykhfree.sns.model.FollowingReq;
import com.ykhfree.sns.model.PostingReq;
import com.ykhfree.sns.repository.FollowingRepository;
import com.ykhfree.sns.repository.PostingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SnsServiceImpl implements SnsService {

    final PostingRepository postingRepository;
    final FollowingRepository followingRepository;

    public SnsServiceImpl(PostingRepository postingRepository, FollowingRepository followingRepository) {
        this.postingRepository = postingRepository;
        this.followingRepository = followingRepository;
    }

    @Override
    public Flux<PostingDto> getNewsFeeds(String userId) {

        return followingRepository.findAllByUserId(userId)
                .log("getNewsFeeds_findAllByUserId")
                .collectList()
                .map(ids -> { ids.add(userId); return ids; })
                .flatMapMany(ids -> postingRepository.findByUserIdInOrderByCreatedDateDesc(ids)
                        .log("getNewsFeeds_findByUserIdInOrderByCreatedDateDesc")
                        .map(this::buildingPostingDto)
                );
    }

    @Override
    public Mono<PostingDto> insertPosting(PostingReq postingReq) {

        Posting posting = Posting.builder()
                .userId(postingReq.getUserId())
                .contents(postingReq.getContents())
                .createdDate(LocalDateTime.now())
                .build();

        return postingRepository.save(posting)
                .log("insertPosting_save")
                .map(this::buildingPostingDto);

    }

    @Override
    public Mono<PostingDto> updatePosting(PostingReq postingReq) {

        return postingRepository.findByIdAndUserId(postingReq.getId(), postingReq.getUserId())
                .log("updatePosting_findById")
                .map(posting -> {
                    posting.setContents(postingReq.getContents());
                    posting.setUpdatedDate(LocalDateTime.now());
                    return posting;
                })
                .flatMap(postingRepository::save)
                .log("updatePosting_save")
                .map(this::buildingPostingDto);
    }

    @Override
    public Mono<Void> deletePosting(PostingReq postingReq) {

        return postingRepository.findByIdAndUserId(postingReq.getId(), postingReq.getUserId())
                .log("deletePosting_findByIdAndUserId")
                .flatMap(postingRepository::delete)
                .log("deletePosting_delete")
                .then();
    }

    @Override
    public Flux<FollowingDto> getFollowingInfo(String userId) {
        return followingRepository.findAllByUserId(userId)
                .map(this::buildingFollowingDtoOnlyString)
                .log("getFollowingInfo_findAllByUserId");
    }

    @Override
    public Mono<FollowingDto> insertFollowingInfo(FollowingReq followingReq) {

        Following following = Following.builder()
                .userId(followingReq.getUserId())
                .followUserId(followingReq.getFollowUserId())
                .createdDate(LocalDateTime.now())
                .build();

        return followingRepository.save(following)
                .log("insertFollowingInfo_save")
                .map(this::buildingFollowingDto);
    }

    @Override
    public Mono<Void> deleteFollowingInfo(FollowingReq followingReq) {

        return followingRepository.findByUserIdAndFollowUserId(followingReq.getUserId(), followingReq.getFollowUserId())
                .log("deleteFollowingInfo_findByUserIdAndFollowUserId")
                .flatMap(followingRepository::delete)
                .log("deleteFollowingInfo_delete")
                .then();
    }

    private PostingDto buildingPostingDto(Posting posting) {

        return PostingDto.builder()
                .id(posting.getId())
                .userId(posting.getUserId())
                .contents(posting.getContents())
                .createdDate(posting.getCreatedDate())
                .updatedDate(posting.getUpdatedDate())
                .build();
    }

    private FollowingDto buildingFollowingDto(Following following) {

        return FollowingDto.builder()
                .id(following.getId())
                .userId(following.getUserId())
                .followUserId(following.getFollowUserId())
                .createdDate(following.getCreatedDate())
                .build();
    }

    private FollowingDto buildingFollowingDtoOnlyString(String followUserId) {

        return FollowingDto.builder()
                .followUserId(followUserId)
                .build();
    }
}

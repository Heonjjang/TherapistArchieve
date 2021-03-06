package com.projectTeam.therapist.repository;

import com.projectTeam.therapist.model.PostCategory;
import com.projectTeam.therapist.model.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/* 더 많은 메소드 키워드들을 찾아보려면...?
 * https://docs.spring.io/spring-data/jpa/docs/2.5.3/reference/html/#jpa.query-methods.query-creation
 */
public interface PostRepository extends JpaRepository<PostDto, Long> {
    List<PostDto> findByPostTitle(String postTitle);
    List<PostDto> findByPostType(PostCategory postType);
    List<PostDto> findByPostTitleOrPostContent(String postTitle, String postContent);

    // 게시글의 제목과 내용을 포함한 페이지를 리턴하는 메서드
    Page<PostDto> findByPostTitleContainingOrPostContentContaining(String postTitle, String postContent, Pageable pageable);
}

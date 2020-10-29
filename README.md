# Spring Webflux + R2DBC 샘플

### 개발 및 빌드환경

* JDK 11
* Spring WebFlux 2.3.4.RELEASE
* Spring DATA R2DBC 2.3.4.RELEASE
* H2 1.4 (파일방식)
* Lombok 1.18.12
* gradle wrapper 6.6.1
* Intellij Idea
* etc

### 빌드 및 실행 방법
- 8080 포트를 사용하므로 해당 포트가 사용중이면 안됨  
- H2 내장DB를 메모리가 아닌 파일방식이므로 아래 명령을 실행하는 계정이 해당 폴더의 읽기, 쓰기권한을 가져야함.
- 실행 방법
    
   ```
   $ ./gradlew bootRun
   or
   gradlew.bat bootRun
   ```

### Application 내용
간단한 SNS 서비스를 구현함. 뉴스피드 조회, 포스팅(등록,수정,삭제), 팔로잉(조회,등록,삭제)

### Entity 구조
(자세한 DDL문은 src/main/resources/schema/schema.sql 참조)

#### posting
- 포스트 글 정보가 저장되는 테이블
   ```
   id (BIGSERIAL) PK  --포스트 ID
   user_id (VARCHAR(50))  --등록자 ID
   contents (VARCHAR(500))  --포스트 글 내용
   created_date (TIMESTAMP)  --등록알자
   updated_date (TIMESTAMP)  --수정알자
   ```
  
#### following
- 팔로잉 관계가 저장되는 테이블
   ```
   id (BIGSERIAL) PK  --팔로잉 ID
   user_id (VARCHAR(50))  --팔로잉 주체 유저 ID
   follow_user_id (VARCHAR(50))  --팔로잉 대상 유저 ID
   created_date (TIMESTAMP)  --등록일자
   ```

### API 기능목록
(상세한 명세는 괄호안 경로의 RestDoc 참고)
1. 뉴스피드 조회 (Spring RestDocs : src/main/resources/static/sns/get-news-feeds.html)
2. 포스트 등록 (Spring RestDocs : src/main/resources/static/sns/insert-posting.html)
3. 포스트 수정 (Spring RestDocs : src/main/resources/static/sns/update-posting.html)
4. 포스트 삭제 (Spring RestDocs : src/main/resources/static/sns/delete-posting.html)
5. 팔로잉 정보 조회 (Spring RestDocs : src/main/resources/static/sns/get-following-info.html.html)
6. 팔로잉 정보 등록 (Spring RestDocs : src/main/resources/static/sns/insert-following.html)
7. 팔로잉 정보 삭제 (Spring RestDocs : src/main/resources/static/sns/delete-following.html)

- API 개발 시 전제했던 상황
  1. 파라미터인 userId(작성자 ID)는 JWT를 복호화 후 추출한 값을 API_GATEWAY로부터 넘겨받았다는 가정하에 개발.
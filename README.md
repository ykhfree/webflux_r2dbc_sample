# 레진엔터테인먼트 과제

### 개발 및 빌드환경

* JDK 11
* Spring WebFlux 2.3.4.RELEASE
* Spring DATA R2DBC 2.3.4.RELEASE
* H2 1.4 R2DBC (파일방식)
* Lombok 1.18.12
* gradle wrapper 6.6.1
* Intellij Idea
* etc

### 빌드 및 실행 방법
- 8080 포트를 사용하므로 해당 포트가 사용중이면 안됨  
- H2 내장DB를 메모리가 아닌 파일방식으로 사용하므로 실행하는 계정이 실행 폴더의 읽기,쓰기권한을 가져야함.
- 해당 프로젝트는 gradle wrapper로 구성되어 있기 때문에 아래의 명령어를 통해 프로젝트를 빌드할 수 있다.
- 실행 방법
    
   ```
   $ ./gradlew bootRun
   or
   gradlew.bat bootRun
   ```

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

### API 기능명세
- src/main/resources/static/sns 하위 Spring RestDocs에 기입
- API 개발 시 전제했던 상황
  1. 파라미터인 userId는 JWT를 복호화 후 추출한 값을 넘겨받는 것을 전제로 개발함.
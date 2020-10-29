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
간단한 SNS 서비스를 구현
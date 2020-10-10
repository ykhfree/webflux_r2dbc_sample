# 레진엔터테인먼트 과제

### 개발 및 빌드환경

* JDK 11
* Spring Boot 2.3.4.RELEASE
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
(자세한 DDL문은 resource/schema/schema.sql 참조)

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

#### signup 계정생성 API
1. method : POST
2. URL : /user/signUp
3. Request Body
   ```json
   {
       "userId": "ykhfree6",
       "userPassword": "ykhfree6!!"
   }
   ```
   content-type : application/json;charset=UTF-8
4. Response
   ```json
   {
       "tokenType": "Bearer",
       "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQyNDI4NTYsInVzZXJJZCI6InlraGZyZWU2In0.oT0QKIDTWLPi3XKbooVW0kUcgpql0ZyaQuKmfq3gkrA",
       "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ2NzMwNTYsInVzZXJJZCI6InlraGZyZWU2In0.QqYY2MgabzNe3-Gn0IVEC2zGY2xP_hE4gj6vbFyPSB8"
   }
   ```
   content-type : application/json;charset=UTF-8


#### signin 로그인 API
1. method : POST
2. URL : /user/signIn
3. Request Body
   ```json
   {
       "userId": "ykhfree6",
       "userPassword": "ykhfree6!!"
   }
   ```
   content-type : application/json;charset=UTF-8
4. Response
   ```json
   {
       "tokenType": "Bearer",
       "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQyNDI4NTYsInVzZXJJZCI6InlraGZyZWU2In0.oT0QKIDTWLPi3XKbooVW0kUcgpql0ZyaQuKmfq3gkrA",
       "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ2NzMwNTYsInVzZXJJZCI6InlraGZyZWU2In0.QqYY2MgabzNe3-Gn0IVEC2zGY2xP_hE4gj6vbFyPSB8"
   }
   ```
   content-type : application/json;charset=UTF-8
   
#### refresh 토큰 재발급 API
1. method : GET
2. URL : /user/refreshToken
3. Request Header
   ```
   Authorization : Bearer ${refreshToken}
   ```
4. Response
   ```json
   {
       "tokenType": "Bearer",
       "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQyNDI4NTYsInVzZXJJZCI6InlraGZyZWU2In0.oT0QKIDTWLPi3XKbooVW0kUcgpql0ZyaQuKmfq3gkrA",
       "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODQ2NzMwNTYsInVzZXJJZCI6InlraGZyZWU2In0.QqYY2MgabzNe3-Gn0IVEC2zGY2xP_hE4gj6vbFyPSB8"
   }
   ```
   content-type : application/json;charset=UTF-8
   
#### 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
1. method : PUT
2. URL : /api/insertData
3. Request Header
   ```
   Authorization : Bearer ${accessToken}
   ```
4. Response (작업성공 여부에 따라 resultMsg에 메시지가 달라짐)
   ```json
   {
       "resultMsg": "success|error|etc_error_msg"
   }
   ```
   content-type : application/json;charset=UTF-8
   
#### 주택금융 공급 금융기관(은행) 목록을 출력하는 API
1. method : GET
2. URL : /api/getAllInstitute
3. Request Header
   ```
   Authorization : Bearer ${accessToken}
   ```
4. Response
   ```json
   {
       "bank": [
           {
               "name": "주택도시기금"
           },
           {
               "name": "국민은행"
           },
           {
               "name": "우리은행"
           },
           {
               "name": "신한은행"
           },
           {
               "name": "한국시티은행"
           },
           {
               "name": "하나은행"
           },
           {
               "name": "농협은행/수협은행"
           },
           {
               "name": "외환은행"
           },
           {
               "name": "기타은행"
           }
       ]
   }
   ```
   content-type : application/json;charset=UTF-8
   
#### 년도별 각 금융기관의 지원금액 합계를 출력하는 API
1. method : GET
2. URL : /api/getSumAmountPerYear
3. Request Header
   ```
   Authorization : Bearer ${accessToken}
   ```
4. Response
   ```json
   {
       "name": "주택금융 공급현황",
       "status": [
           {
               "year": "2005년",
               "total_amount": 48016,
               "detail_amount": {
                   "하나은행": 3122,
                   "농협은행/수협은행": 1486,
                   "우리은행": 2303,
                   "국민은행": 13231,
                   "신한은행": 1815,
                   "주택도시기금": 22247,
                   "외환은행": 1732,
                   "한국시티은행": 704,
                   "기타은행": 1376
               }
           },
           {
               "year": "2006년",
               "total_amount": 41210,
               "detail_amount": {
                   "하나은행": 3443,
                   "농협은행/수협은행": 2299,
                   "우리은행": 4134,
                   "국민은행": 5811,
                   "신한은행": 1198,
                   "주택도시기금": 20789,
                   "외환은행": 2187,
                   "한국시티은행": 288,
                   "기타은행": 1061
               }
           }
       ]
   }
   (이하 생략)
   ```
   content-type : application/json;charset=UTF-8
   
#### 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API
1. method : GET
2. URL : /api/getMaxInstituteByYear
3. Request Header
   ```
   Authorization : Bearer ${accessToken}
   ```
4. Response
   ```json
   {
       "bank": "주택도시기금",
       "year": 2014
   }
   ```
   content-type : application/json;charset=UTF-8
   
#### 전체 년도에서 특정 금융기의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API
1. method : GET
2. URL : /api/getMinMaxInstituteByYear
3. Request Header
   ```
   Authorization : Bearer ${accessToken}
   ```
4. Request QueryParam
   ```
   name : ${특정 금융기관명}
   ```
5. Response
   ```json
   {
       "bank": "신한은행",
       "support_amount": [
           {
               "amount": 100,
               "year": 2006
           },
           {
               "amount": 4073,
               "year": 2017
           }
       ]
   }
   ```
   content-type : application/json;charset=UTF-8
   
#### 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하는 API
1. method : GET
2. URL : /api/getPredictionAmount
3. Request Header
   ```
   Authorization : Bearer ${accessToken}
   ```
4. Request QueryParam
   ```
   name : ${특정 금융기관명},
   month : ${예상값을 알고 싶은 달}
   ```
5. Response
   ```json
   {
       "bank": 47,
       "amount": 3831,
       "month": 2,
       "year": 2018
   }
   ```
   content-type : application/json;charset=UTF-8
   
### 문제해결 전략
- 개발환경
    - Spring boot를 이용
    - DB는 H2를 사용하되 서버 재기동이후에도 데이터가 남아 있을 수 있게 파일저장 방식으로 결정함.
    - 서버 기동 시 csv 데이터를 입력하는 방안에 대해서 고민했지만 등록 API가 따로 존재하므로 생략함.

- signup 계정생성 API
    - 사용자ID와 비밀번호를 client를 통해 수신
    - 비밀번호는 추후 암호화 알고리즘을 편하게 변경할 수 있도록 Spring Security의 createDelegatingPasswordEncoder을 이용해 인코딩
    - 추출된 비밀번호 앞의 {id}는 제거 없이 DB에 저장 (추후 정책에 따라 제거 가능)
    - DB 입력이 끝난 후 jwt token을 생성
    - 전자서명키는 개발당시 임의로 생성한 UUID를 이용
    - accessToken의 유효기간은 30분, refreshToken의 유효기간은 5일로 하되 properties 파일로 수정가능
    - 모든 결과값을 map으로 return
    
- signin 로그인 API
    - 사용자ID와 비밀번호를 client를 통해 수
    - 수신받은 암호와 DB의 비밀번호를 비교
    - 유효한 사용자이면 jwt token 리턴
    
- refresh 토큰 재발급 API
    - Authorization 헤더값 체크
    - Bearer Token이 아니면 오류 발생
    - 수신 받은 token이 refresh token인지 확인하는 로직을 고민했지만 과제명세서에 별다른 언급이 없어 진행하지 않음.
    - refresh token이 유효하면 새로운 jwt token 리턴

- 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API
    - 데이터 입력 전 institute, insitute_credit 테이블의 값들을 모두 삭제
    - apache commons csv를 이용해 csv를 파싱
    - jar 실행 시에도 csv 파일을 찾을 수 있도록 InputStreamReader로 파일 취득
    - 값이 없는 record들은 결과값에 담지 않고 버림
    - 파싱된 record에서 금융기관명을 먼저 추출하되 '연도', '월' 등의 키워드를 제외한 것만 추출
    - 추출 시 '(억원)'등 불필요한 키워드를 제거함
    - 추출된 금융기관명을 테이블에 입력
    - csv의 body 영역 부분도 해당 금융기관 코드와 함께 institute_credit 테이블에 입력
    - 성공여부 return
    
- 주택금융 공급 금융기관(은행) 목록을 출력하는 API 
    - 금융기관 정보가 담긴 institute 테이블을 select 한 후 map으로 return
    
- 년도별 각 금융기관의 지원금액 합계를 출력하는 API
    - institute_credit 테이블에 기록된 year을 중복 제거한 후 List로 추출
    - 그 후 모든 금융기관명도 List로 추출
    - 앞서 추출한 year을 forEach로 돌면서 해당 년도의 금융기관의 년도별 지원금액을 구한다.
    - 단, 해당년도의 모든 금융기관 지원금액 총액을 구하기 위해 AtomicInteger 변수를 따로 만들어 총금액을 지속적으로 더한다. (해당 변수는 년도가 바뀌면 초기화된다)
    - 모든 결과값을 map으로 return
    
- 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API
    - institute_credit 테이블에 기록된 year을 중복 제거한 후 List로 추출
    - 그 후 모든 금융기관명도 List로 추출
    - 앞서 추출한 year을 forEach로 돌면서 각 금융기관의 지원금액을 더한다.년
    - 해당 년도의 특정 금융기관 지원금액 합계가 나오면 maxAmount라는 변수에 기록된 금액과 비교
    - maxAmount에 있는 값이 적다면 년도별 계산된 특정 금융기관의 지원총액을 maxAmount에 넣는다. 더불어 해당년도와 금융기관명도 변수에 저장
    - 마지막까지 남은 변수값들을 map으로 return
    
- 전체 년도에서 특정기관의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API
    - 우선 과제명세서에 나온 (2005~2016)의 의미를 예측함.
        - 2017이 10월까지 있고 과제명세서에 매년 12월의 지원금액 평균값이라고 명시되어 있기에 12월까지의 데이터가 없는 년도는 계산조건에 넣지 말지에 대해 고민함.
        - 하지만 과제명세서에 12월까지 존재하지 않는 년도는 계산에 넣지 않는다는 확실한 명시가 없기에 12월까지 존재하지 않아도 계산 조건에 넣기로 함
        - 또한, '년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API'의 경우 2005~2017로 되어 있기에 단순 오타로 판단함
    - institute_credit 테이블에 기록된 year을 중복 제거한 후 List로 추출
    - 파라미터로 받은 금융기관명을 바탕으로 institute_credit 테이블의 갑을 List로 추출
    - 앞서 추출한 year을 forEach로 돌면서 월별 지원금액을 더한후 Collectors.averagingInt 함수를 이용해 년도별 평균값을 바로 구함.
    - 위 방법을 통해 추출한 년도별 특정기관의 지원금액을 map에 넣고 Collections.min 과 Collections.max 함수를 이용해 최소값과 최대값을 구함.
    - 모든 결과값을 map으로 return
    
- 특정 은행의 특정 달에 대해서 차도 해당 달에 금융지원 금액을 예측하는 API
    - 과거의 데이터를 바탕으로 미래의 데이터 값을 예측하는 알고리즘 조사 진행
    - 금융지원 데이터가 시간과 흐름에 따른 결과이기에 시계열 분석으로 결정
    - MA, ARMA, ARIMA 등의 모형들을 데이터 그래프와 비교해가며 확인한 결과 과거의 데이터가 지니고 있는 추세까지 반영하는 ARIMA 모델로 구현하기로 함
    - 또한 ARIMA 모형이 비정상적 시계열 자료에 대해 분석하는 방법 (금융지원데이터가 지속적으로 증가추세이지만 중간중간 내려가는 추세도 존재하기때문)
    - 금융지원 데이터를 엑셀 그래프로 확인한 결과 계절별 특성이 없기에 seasonal이 아닌 non-seasonal임을 확인
    - 이를 바탕으로 com.github.signaflo.timeseries 소스를 적용
    - https://github.com/signaflo/java-timeseries/wiki/ARIMA-models 및 https://github.com/signaflo/java-timeseries 에 있는 sample을 기반으로 개발함 
    - 먼저 파라미터로 받은 특정 금융기관명을 바탕으로 institute_credit을 List로 추출
    - 상단 List, 예측년도와 월을 predictionAmount 함수에 전달
    - 과제명세서에 있는 근사값에 근접하기 위해 ArimaOrder.order을 지속 수정
    - non-seasonal에 해당하는 order 설정값을 수정 중 과제명세서에 기입된 값인 정'4850'에 근접한 '4854'가 나온 설정값으로 최종 수정
    - 모든 결과값을 map으로 return
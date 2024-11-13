
# 1. 환경 세팅
## 1. DB
### 1. mysql db 실행
docker run --name test-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=testdb -p 3306:3306 -d mysql:latest
### 2. 컨테이너 중지
docker stop test-mysql
### 3. 컨테이너 삭제
docker rm test-mysql
### 4. db 접속
#### url: jdbc:mysql://localhost:3306/testdb
#### username: root
#### password: root

---

## 2. Swagger
http://localhost:8080/swagger-ui/index.html

---

# 3. ERD

--- 

# 4. API Documentation
## 1. Auth API
### Register User
- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Description**: 사용자 회원가입
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**: `String` (회원가입 결과 메시지)

---

### Login User
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Description**: 사용자 로그인 및 JWT 토큰 발급
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "token": "string"
  }
  ```

---

### Logout User
- **URL**: `/api/auth/logout`
- **Method**: `POST`
- **Description**: 사용자 로그아웃
- **Request Body**:
  ```json
  {
    "username": "string"
  }
  ```
- **Response**: `String` (로그아웃 결과 메시지)

---

## 2. Advice API

### Request Advice
- **URL**: `/api/advice/request`
- **Method**: `POST`
- **Description**: 사용자가 포트폴리오 자문을 요청
- **Parameters**:
    - `userId`: `Long` (사용자 ID)
    - `riskType`: `RiskType` (위험 유형)
- **Response**:
  ```json
  {
    "id": "Long",
    "riskType": "String",
    "portfolioStocks": [
      {
        "stockCode": "string",
        "stockName": "string",
        "quantity": "int"
      }
    ],
    "totalInvestment": "BigDecimal"
  }
  ```

---

## 3. Stock API

### Get All Stocks
- **URL**: `/api/stocks`
- **Method**: `GET`
- **Description**: 등록된 모든 증권 목록 조회
- **Response**: `List<Stock>`
  ```json
  [
    {
      "id": "Long",
      "stockCode": "string",
      "stockName": "string",
      "price": "BigDecimal"
    }
  ]
  ```

---

### Register Stock
- **URL**: `/api/stocks/register`
- **Method**: `POST`
- **Description**: 새로운 증권 등록
- **Parameters**:
    - `stockCode`: `String` (증권 코드)
    - `stockName`: `String` (증권 이름)
    - `price`: `BigDecimal` (증권 가격)
- **Response**: `Stock`
  ```json
  {
    "id": "Long",
    "stockCode": "string",
    "stockName": "string",
    "price": "BigDecimal"
  }
  ```

---

### Update Stock Price
- **URL**: `/api/stocks/{stockId}/price`
- **Method**: `PUT`
- **Description**: 특정 증권의 가격 업데이트
- **Path Variable**:
    - `stockId`: `Long` (증권 ID)
- **Parameters**:
    - `newPrice`: `BigDecimal` (새로운 가격)
- **Response**: `Stock`
  ```json
  {
    "id": "Long",
    "stockCode": "string",
    "stockName": "string",
    "price": "BigDecimal"
  }
  ```

---

### Delete Stock
- **URL**: `/api/stocks/{stockId}`
- **Method**: `DELETE`
- **Description**: 특정 증권 삭제
- **Path Variable**:
    - `stockId`: `Long` (증권 ID)
- **Response**: `String` ("Stock deleted successfully.")

---

## 4. Transaction API

### Deposit
- **URL**: `/api/transactions/deposit`
- **Method**: `POST`
- **Description**: 사용자 원화 입금
- **Parameters**:
    - `userId`: `Long` (사용자 ID)
    - `amount`: `BigDecimal` (입금 금액)
- **Response**:
  ```json
  {
    "id": "Long",
    "amount": "BigDecimal",
    "balanceAfter": "BigDecimal",
    "timestamp": "LocalDateTime",
    "transactionType": "String"
  }
  ```

---

### Withdraw
- **URL**: `/api/transactions/withdraw`
- **Method**: `POST`
- **Description**: 사용자 원화 출금
- **Parameters**:
    - `userId`: `Long` (사용자 ID)
    - `amount`: `BigDecimal` (출금 금액)
- **Response**:
  ```json
  {
    "id": "Long",
    "amount": "BigDecimal",
    "balanceAfter": "BigDecimal",
    "timestamp": "LocalDateTime",
    "transactionType": "String"
  }
  ```
---
# 5. 주요 요구 사항 구현

## 1. 비밀번호 암호화
회원가입 시 BCryptPasswordEncoder를 사용하여 비밀번호를 암호화하고 데이터베이스에 저장합니다.

---

## 2. 사용자 인증
### 1. 인증 흐름
- 로그인 요청: 클라이언트가 username과 password를 보내면 서버는 이를 검증하고 JWT 토큰을 생성하여 반환합니다.
- JWT 토큰 검증 필터: 이후 클라이언트는 요청마다 이 JWT 토큰을 포함하여 서버에 접근하며, 서버는 JWT 인증 필터를 통해 각 요청의 JWT를 검증합니다.
- SecurityContext 설정: 인증 필터에서 JWT가 유효하면 SecurityContextHolder.getContext().setAuthentication(authentication);를 사용하여 요청의 SecurityContext에 인증 정보를 설정합니다.
### 2. SecurityConfig 설정:
- requestMatchers를 사용하여 로그인과 회원가입 경로를 허용하고, 다른 모든 경로는 인증이 필요하도록 설정합니다.
- sessionManagement에서 SessionCreationPolicy.STATELESS를 설정하여 JWT 인증 방식에서 세션을 사용하지 않도록 합니다.
- addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)를 통해 JWT 필터가 기본 인증 필터 전에 실행되도록 추가합니다.
### 3. authenticateUser 메서드의 동작(로그인 시 호출)
- authenticationManager.authenticate 호출을 통해 Spring Security의 사용자 인증을 처리합니다.
- 성공 시 JWT 토큰을 발급
### 4. authenticationManager.authenticate 의 동작 방식
- UsernamePasswordAuthenticationToken 생성: new UsernamePasswordAuthenticationToken(username, password)를 통해 사용자 이름과 암호를 포함하는 인증 객체를 생성합니다. 
- CustomUserDetailsService를 통해 사용자를 찾고, CustomUserDetails 객체에 사용자 정보를 리턴합니다.
- PasswordEncoder(예: BCryptPasswordEncoder)를 사용하여 입력된 암호와 저장된 암호를 비교하여 일치 여부를 확인합니다.
입력된 암호가 암호화된 비밀번호와 일치하면 인증에 성공하고, 그렇지 않으면 BadCredentialsException이 발생합니다.
- 인증에 성공하면 Authentication 객체가 반환됩니다. 
### 5. JWT 인증 필터
- JwtAuthenticationFilter는 각 요청마다 JWT를 확인하고, 유효한 경우 SecurityContextHolder에 인증 정보를 설정합니다.

---

## 3. 입출금 데이터 무결성 보장
@Transactional을 통해 입출금 작업이 원자적으로 수행되도록 하고, 낙관적 락을 통해 데이터 무결성을 보장합니다.

낙관적 락은 데이터베이스 레벨이 아닌 애플리케이션 레벨에서 데이터 무결성을 보장하는 방식으로, 여러 사용자가 동시에 데이터에 접근해도 데이터의 일관성을 유지하도록 합니다. 낙관적 락은 변경이 자주 발생하지 않는 데이터를 여러 사용자가 읽고 쓸 때 효과적입니다.

낙관적 락을 구현할 때는 보통 버전 관리를 통해 이루어지며, JPA에서는 @Version 어노테이션을 사용하여 이를 관리합니다.

### 1. 동작 방식
#### 1. 버전 관리 필드 추가: 
엔티티에 @Version 어노테이션이 붙은 필드를 추가합니다. 이 필드는 기본적으로 숫자 또는 타임스탬프 형태로 설정됩니다. 예를 들어, Long 타입 필드를 사용하면 엔티티가 생성될 때 기본값 0으로 초기화됩니다.

#### 2. 데이터 읽기 및 업데이트:
엔티티를 읽을 때, JPA는 현재 버전 정보를 함께 가져옵니다.
엔티티를 수정한 후 저장할 때 JPA는 데이터베이스에 저장된 버전과 엔티티의 버전을 비교합니다.
#### 3. 변경 감지 및 충돌 처리:
만약 저장 시점에 엔티티의 버전이 데이터베이스의 버전과 다르면, 즉 다른 트랜잭션이 먼저 해당 데이터를 변경했다면 OptimisticLockException이 발생합니다.
예외가 발생하면 트랜잭션을 롤백하고, 필요에 따라 재시도를 하거나 사용자에게 충돌 메시지를 전달합니다.

### 2. 낙관적 락과 무결성 보장의 원리

#### 1. 트랜잭션 시작 및 데이터 읽기:
트랜잭션이 시작되면, 엔티티의 데이터를 읽고 해당 엔티티의 version 값을 함께 읽어옵니다.
#### 2. 데이터 수정:
트랜잭션 내에서 엔티티의 데이터를 수정합니다. 이 단계에서는 데이터베이스의 실제 version 값이 변하지 않습니다.
#### 3. 트랜잭션 커밋 시점에 version 값 확인 및 원자적 업데이트:
트랜잭션 커밋 시점에 JPA는 데이터베이스의 version 값을 다시 확인하여 읽어온 version 값과 일치하는지 검사합니다.
일치할 경우에만 데이터 업데이트를 진행하고, 업데이트 후 version 값을 증가시켜 무결성을 보장합니다.
일치하지 않으면 OptimisticLockException 예외를 발생시키고, 트랜잭션을 롤백합니다.

### 3. 낙관적 락의 무결성 보장 한계
- 낙관적 락은 트랜잭션 커밋 시점에서만 충돌을 감지하므로, 트랜잭션 도중에 다른 트랜잭션이 데이터를 수정할 수 있습니다. 
- 현재 트랜잭션이 데이터를 수정하는 도중에 다른 트랜잭션이 같은 데이터를 변경할 수 있습니다. 이 경우 두 번째 트랜잭션이 먼저 커밋하면, 첫 번째 트랜잭션이 커밋하려고 할 때 version 값이 일치하지 않아 OptimisticLockException이 발생하게 됩니다. 즉, 충돌을 커밋 시점에만 확인할 수 있습니다.
- 낙관적 락은 트랜잭션 도중 발생할 수 있는 충돌을 실시간으로 방지하지는 않습니다. 다만, 트랜잭션 종료 시점에서만 이를 감지하여 무결성을 보장합니다.

### 4. 분산환경에서의 동시성과 데이터 무결성 보장

동일한 데이터를 수정하려는 트랜잭션들 간에는 커밋이 실질적으로 순차적으로 이루어집니다.
낙관적 락을 사용하면 트랜잭션 간의 동시성은 보장되지만, 커밋 시점에 버전 불일치가 감지될 경우 충돌이 발생한 트랜잭션은 롤백됩니다.
따라서 동일 데이터에 대해 동시에 커밋이 발생할 수는 없으며, 하나의 트랜잭션이 성공적으로 커밋된 후에야 다른 트랜잭션이 커밋을 시도할 수 있습니다.
이 방식으로 낙관적 락은 동시성 작업 중에도 데이터 무결성을 보장합니다.
 
낙관적 락의 작동 방식과 커밋 타이밍
- 데이터 읽기와 버전 확인:
트랜잭션 A와 B가 동일한 데이터를 읽어 version 값을 가져옵니다. 예를 들어, version 값이 1이라면, 트랜잭션 A와 B는 모두 version = 1이라는 값을 가지고 작업을 시작합니다.
- 업데이트 시도:
트랜잭션 A가 먼저 커밋하려고 하면, UPDATE ... WHERE version = 1과 같이 현재 version 값이 일치하는지 확인하는 조건과 함께 쿼리가 실행됩니다.
트랜잭션 A가 성공적으로 커밋되면, 데이터베이스의 version 값이 2로 증가합니다.
- 버전 불일치로 인한 충돌 감지:
이후 트랜잭션 B가 커밋을 시도할 때, 트랜잭션 B는 여전히 version = 1인 상태에서 업데이트를 시도합니다.
하지만 데이터베이스의 version 값이 이미 2로 증가했기 때문에 version 불일치가 발생하여 트랜잭션 B는 커밋에 실패하고 OptimisticLockException이 발생합니다.
- 충돌 발생 시 트랜잭션 롤백:
OptimisticLockException이 발생한 트랜잭션 B는 롤백 처리됩니다.
트랜잭션 B는 롤백된 후 필요에 따라 다시 시도하거나, 사용자가 직접 다시 시도할 수 있도록 알림을 받을 수 있습니다.
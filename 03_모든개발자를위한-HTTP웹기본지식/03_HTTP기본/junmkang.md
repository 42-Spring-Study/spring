# 섹션 3. HTTP 기본

# HTTP (HyperText Transfer Protocol)

### http 메시지에 모든 것을 전송

- 클라이언트 서버 구조
- 무상태 프로토콜(스테이스리스), 비연결성
- HTTP 메시지
- 단순함, 확장가능

- `TCP`: `HTTP/1.1`, `HTTP/2`
- `UDP`: `HTTP3`

## 클라이언트 서버 구조

클라이언트는 서버에 요청을 보내고, 응답을 대기

서버가 요청에 대한 결과를 만들어서 응답

서버 : 비즈니스 로직, 데이터

클라이언트 : UI, 사용성

영역을 서로 분리하여 구성, 양쪽을 독립적으로 진행하는 것이 가능

## 무상태 프로토콜(스테이스리스)

- 서버가 클라이언트의 상태를 보존X
- 장점: 서버 확장성 높음
- 단점: 클라이언트가 추가 데이터 전송

### stateful?, stateless?

서버가 클라이언트의 상태를 보존하냐 안하냐로 분리가 되는 방식

- `stateful`: 상태유지
클라이언트와 서버가 서로 통신하는 도중에 서버는 클라이언트의 상태를 보존해야하기때문에
해당 작업을 도중에 다른 서버가 대체하는 것이 불가능하다.
- `stateless`: 무상태
무상태에 경우에는 클라이언트의 상태를 보존할 필요가 없기때문에 응답 서버를 쉽게 바꿀 수 있고,
클라이언트와 작업도중에 다른 서버로 교체가 되어도 문제가 없다.

보통은 `stateful`과 `stateless`를 혼합해서 같이 사용한다.

- `stateful`: 상태를 유지해야하는 로그인 등
- `stateless`: 상태를 유지할 필요가 없는 단순한 소개화면 등

**상태 유지는 최소한으로 사용하고 기본은 무상태를 기반으로 생각**

## 비연결성

클라이언트와 서버는 서로 데이터를 통신할때만 연결을 하고 데이터의 통신이 끝나면 서버는 연결을 유지하지않는다.

- http는 기본이 연결을 유지하지 않는 모델
- 일반적으로 초 단위의 이하의 빠른 속도로 응답
- 서버의 자원을 매우 효율적으로 사용할 수 있다.

### 한계와 극복

- 비연결성 방식이다보니 TCP/IP 연결을 할때마다 새로 맺어야한다.
그로인해 3 way handshake 시간이 추가된다.
- HTTP 지속 연결 ( Persistent Connections )로 문제 해결, keepAlive랑 같은 개념
`해당 문제가 어떻게 해결이 되었는지 다시 한번 공부해볼 필요가있다.`
- HTTP/2, HTTP/3에서 더 많은 최적화

## HTTP 메시지

![Untitled](../img/junmkang_304.png)

- start-line
- header
- empty line
- body

### `start-line`

### 요청

- 요청 메시지 - http 메서드
`GET`, `POST`, `DELETE` …
- 요청 메시지 - 요청대상
`absolute-path[?query] (절대경로[?쿼리])`
- http version
`HTTP/1.1`

### 응답

- request-line / status-line
`HTTP/1.1` `200 OK`

### `header`

### 요청

- host 정보
`HOST`: `URL`

### 응답

http 전송에 필요한 모든 부가정보

`field-name` ":" OWS `field-value` OWS ( OWS: 띄어쓰기 허용 )

`field-name` 에 경우에는 대소문자 구별 X

### `empty line`

header와 body를 구별하는 line

### `body`

### 요청 응답

실제로 전송할 데이터

byte로 표현할 수 있는 모든 데이터 전송 가능 ex) HTML 문서, JSON 등등
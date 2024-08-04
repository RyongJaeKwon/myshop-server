# MyShop-server
<br/>

## 🖥 프로젝트 소개
**SpringBoot** + **React**로 만든 쇼핑몰 개인 프로젝트 입니다
<br/>
<br/>

## 🕰 개발 기간
* **24.06.19 ~ 24.08.02**
<br/>

## ⚙ 기술 스택
* ### 백엔드 
  * **Java 17**
  * **Spring Boot**
  * **Spring Security**
  * **Spring Data JPA**
  * **Querydsl**
  * **MariaDB**
<br/>

## 📌 구현 기능
* ### 로그인
  * 로그인, 로그아웃
  * 회원가입
  * OAuth2 로그인(카카오)
  * JWT를 이용한 인증
    * 로그인 시 accessToken, refreshToken 발급
    * accessToken을 통한 인증 및 권한 부여
    * refreshToken을 이용하여 토큰 갱신
* ### 상품
  * 상품 등록, 수정, 삭제
  * 카테고리별 상품 조회
  * 상품 목록 페이징 처리
  * 상품 리뷰 CRUD
  * 상품 검색
* ### 장바구니
  * 장바구니 추가, 수량 변경, 삭제
* ### 주문
  * 개별상품 주문
  * 장바구니 상품주문
  * MyPage(주문 목록)
  * 주문 상세보기
  * 주문 취소
<br/>

## 📄 API 명세
| Feature | Method | URI | Description | 요청 파라미터 | 토큰 필요 |
| ------- | ------ | --- | ------------------ | ------------ | --- |
| 로그인 | POST | /member/login | 로그인 | userId, password | |
| 로그인 | POST | /member/refresh | 토큰갱신 | Authorization, X-Refresh-Token | ✅ |
| 로그인 | POST | /member/ | 회원가입 | MemberDto | |
| 로그인 | PUT | /member/ | 회원수정 | MemberDto | ✅ |
| 로그인 | DELETE | /member/{id} | 회원탈퇴 | id | ✅ |
| 로그인 | GET | /member/user-id/{userId}/check | ID 중복체크 | userId | |
| 로그인 | GET | /member/user-email/{email}/check | Email 중복체크 | email | |
| 카카오 로그인 | POST | /member/kakao | 카카오 로그인 사용자의 회원가입 | MemberUpdate | |
| 카카오 로그인 | GET | /member/getkakao | 카카오 사용자 정보 가져오기 | accessToken | | ✅ |
| 상품 | POST | /items/ | 상품등록 | ItemDto | ✅ |
| 상품 | PUT | /items/{id} | 상품수정 | id, itemDto | ✅ |
| 상품 | DELETE | /items/{id} | 상품삭제 | id | ✅ |
| 상품 | GET | /items/{id} | 상품조회 | id | ✅ |
| 상품 | GET | /items/ | 상품목록 | | |
| 상품 | GET | /items/{category}/list | 카테고리별 상품목록 | PageRequestDto, category | |
| 상품 | GET | /items/view/{fileName} | 상품 이미지 조회 | fileName | |
| 상품 | GET | /items/search | 상품검색 | PageRequestDto | |
| 상품리뷰 | POST | /replies/ | 리뷰추가 | ReplyDto | ✅ |
| 상품리뷰 | PUT | /replies/ | 리뷰수정 | ReplyDto | ✅ |
| 상품리뷰 | DELETE | /replies/{id} | 리뷰삭제 | id, userId | ✅ |
| 상품리뷰 | GET | /replies/{id} | 리뷰조회 | id | ✅ |
| 상품리뷰 | GET | /replies/item/{itemId} | 상품별 리뷰목록조회 | itemId | ✅ |
| 장바구니 | POST | /cart/ | 장바구니 상품추가 | CartItemDto | ✅ |
| 장바구니 | DELETE | /cart/{cartItemId} | 장바구니 상품삭제 | cartItemId | ✅ |
| 장바구니 | GET | /cart/list | 장바구니 목록 | | ✅ |
| 주문 | POST | /orders/ | 개별상품 주문등록 | OrderCreateDto | ✅ |
| 주문 | POST | /orders/cart | 장바구니 주문등록 | OrderCreateDto | ✅ |
| 주문 | POST | /orders/{userId}/{orderId} | 주문취소 | userId, orderId | ✅ |
| 주문 | GET | /orders/{userId} | 회원별 주문목록 | userId | ✅ |
| 주문 | GET | /orders/{userId}/{orderId} | 주문 상세조회 | userId, orderId | ✅ |

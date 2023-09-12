# [서울시립대학교 학식 봇](https://pf.kakao.com/_YVxdzG)

![bot-profile.png](docs%2Fimages%2Fbot-profile.png)
* [채널 링크](https://pf.kakao.com/_YVxdzG)

## 목차
* 개발 동기
* 제공 기능
* 이슈 및 업데이트 사항
* 시스템 구조
* RDBMS 구조
* 테스트 항목
* 사용 기술

## 개발 동기
* 👨‍🎓 졸업 전 학교와 관련된 프로젝트를 하나 만들면 재미있을 것 같아 진행
* 🤖 학식 봇이 존재하면 학생들이 이용할지 궁금하여 진행
* 😁 프론트 엔드에 대한 부담을 덜 수 있어 개발 확정!

## 제공 기능
* **실시간 인기 메뉴**
  * 시간에 따라서 아침, 점심, 저녁 인기 메뉴
    * e.g) 현재 8:00 ~ 10:00 이면 아침 메뉴중 인기 메뉴 제공
* **실시간 추천 메뉴**
  * 시간에 따라서 아침, 점심, 저녁 추천 메뉴
    *  e.g) 현재 8:00 ~ 10:00 이면 아침 메뉴중 추천 메뉴 제공
* **식당별 식사종류에 맞는 메뉴 조회**
  * xx식당 아침, 점심, 저녁
    * e.g) 학생회관 점심 메뉴 조회
* **식사종류별 전체 식당 메뉴 전체 조회**
  * e.g) 학생회관, 아느칸, 자연과학관, 본관 점심 메뉴 조회
* **메뉴 추천하기**
  * 동일한 메뉴 중복 추천 불가

## 이슈 및 업데이트 사항
* [이슈 및 업데이트 사항 | 베타](docs/BETA-UPDATE-REPORT.md)
* [이슈 및 업데이트 사항 | 프로덕션](docs/UPDATE-REPORT.md)

## 시스템 구조
* 학교측에서 학식 메뉴에 대한 `API`를 제공하지 않기 때문에 `크롤링`하는 방법을 채택

![system-architecture.png](docs%2Fimages%2Fsystem-architecture.png)


## RDBMS 구조
![erd.png](docs%2Fimages%2Ferd.png)

## 테스트 항목
### 총 65개의 테스트 항목 작성
* **TextCardUosRestaurantControllerTest**
  * 학교식당이름과 식사종류로 메뉴를 조회할 때 진짜 메뉴를 제공받지 못했으면(e.g] 금일 학교사정상 운영안함) simpleText 형식의 예외응답을 준다.
  * 인기메뉴 조회시 운영시간이 아닌 경우 simpleText 형식으로 예외응답을 준다.
  * 조회수가 가장 많은 메뉴를 1개 조회한다.(조회수가 같으면 추천수가 많은 순으로 조회)
  * 추천메뉴 조회시 운영시간이 아닌 경우 simpleText 형식으로 예외응답을 준다.
  * 학교 식당이름과 식사종류로 메뉴를 조회하고 textCard 형식의 응답을 준다.
  * 추천수가 가장 많은 메뉴를 1개 조회한다.(추천수가 같으면 조회수가 많은 순으로 조회)
  * 학식 메뉴를 추천한다.
* **CommonControllerTest**
  * 일요일에는 학식을 제공하지 않는 안내문구를 simpleText로 응답한다.
  * botApikey 헤더가 알맞지 않은 경우 예외가 발생한다.
  * 토요일에는 학식을 제공하지 않는 안내문구를 simpleText로 응답한다.
* **UosRestaurantNameTest**
  * 지원하지 않는 한글로 UosRestaurantName enum 타입을 찾으면 예외가 발생한다.
  * 영어로 UosRestaurantName enum 타입을 찾는다.
  * 지원하지 않는 영어로 UosRestaurantName enum 타입을 찾으면 예외가 발생한다.
  * 한글로 UosRestaurantName enum 타입을 찾는다.
* **CrawlingStudentHallServiceTest**
  * 이미 크롤링한 데이터가 있으면 데이터베이스에 저장하지 않는다.
  * 크롤링 데이터를 저장한다.
* **MealTypeTest**
  * 지원하지 않는 한글로 MealType enum 타입을 찾으면 예외가 발생한다.
  * 영어로 MealType enum 타입을 찾는다.
  * 지원하지 않는 영어로 MealType enum 타입을 찾으면 예외가 발생한다.
  * 한글로 MealType enum 타입을 찾는다.
* **UosRestaurantServiceTest**
  * 저녁메뉴 중 가장 조회수가 많은 메뉴 1개를 조회한다.(조회수가 같으면 추천수가 많은 것을 조회)
  * 저녁메뉴 중 가장 추천수가 많은 메뉴 1개를 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)
  * 금일 조식 메뉴들을 조회하고 조회수를 1증가한다.
  * 운영시간이 아니면 조회수가 많은 메뉴 1개를 조회할때 예외가 발생한다.
  * 점심메뉴 중 가장 조회수가 많은 메뉴 1개를 조회한다.(조회수가 같으면 추천수가 많은 것을 조회)
  * 아침메뉴 중 가장 추천수가 많은 메뉴 1개를 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)
  * 사용자가 학식 메뉴를 추천할 때 사용자의 추천이력이 있으면 예외가 발생한다.
  * 학교식당의 금일 조식 메뉴를 조회하고 조회수를 1증가한다.
  * 아침메뉴 중 가장 추천수가 많은 메뉴 1개를 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)
  * 아침메뉴 중 가장 조회수가 많은 메뉴 1개를 조회한다.(조회수가 같으면 추천수가 많은 것을 조회)
  * 학교식당의 메뉴가 없을 경우 예외가 발생한다.
  * 운영시간이 아니면 추천수가 많은 메뉴 1개를 조회할때 예외가 발생한다.
  * 사용자가 학식 메뉴를 추천할 때 회원이 없으면 회원이 생성되고 추천수가 1증가하고 추천이력이 생성된다.
* **TimeProviderTest**
  * 평일이면 false를 반환한다.
  * 주말이면 true를 반환한다.
* **CacheSkillBlockServiceTest**
  * 캐시에 SkillBlock이 없으면, 캐시에 저장된다.
  * 캐시에 SkillBlock 배열이 없으면, 캐시에 저장된다.
* **CrawlingDateUtilsTest**
  * 메뉴의 글자수가 25보다 작으면 false를 반환한다.
  * 14시 ~ 18시30분 사이에는 저녁타입을 반환한다.
  * 메뉴가 빈문자열이면 false를 반환한다.
  * LocalDateTime을 '월/일 (요일)'로 변환한다.
  * 오전 8시 30분 ~ 11시 사이에는 아침타입을 반환한다.
  * 오전 11시 ~ 14시 사이에는 점심타입을 반환한다.
* **CacheMemberServiceTest**
  * 캐시에 회원을 저장하고, 방문횟수를 1증가 한다.
  * 회원이 없으면 데이터베이스에 회원을 생성하고 캐시에 저장한다.
* **StudentHallCrawlerTest**
  * 서울시립대학교 학생회관 1층 식당 주간식단표를 크롤링 한다.
* **SimpleTextUosRestaurantControllerTest**
  * 학교식당이름과 식사종류 알맞은 메뉴가 없으면 예외(200)가 발생하고 simpleText 형식으로 응답을 준다.
  * 식사종류로 메뉴들을 조회하고 simpleText 형식으로 응답을 준다.
  * 학교식당이름과 식사종류로 메뉴를 조회하고 simpleText 형식으로 응답을 준다.
* **OperatingTimeTest**
  * 현재 시간이 8:00 ~ 18:29 이 아니면 운영시간이 아니다.
  * 현재 시간이 8:00 ~ 18:29 이 아니면 운영시간이 아니다.
  * 현재 시간이 8:00 ~ 18:29 이면 운영시간 이다.
  * 현재 시간이 8:00 ~ 18:29 이면 운영시간 이다.
* **UosRestaurantRepositoryTest**
  * 날짜, 식사종류를 조건으로 식당 메뉴들을 조회한다.
  * 식당이름, 식사종류를 조건으로 식당 메뉴를 조회한다.
  * 날짜, 식사종류를 조건으로 조회수가 가장 많은 식사메뉴를 조회(조회수가 같을 경우 추천수 많은 순으로)
  * 날짜, 식당이름, 식사종류를 조건으로 식당 메뉴를 조회한다.
  * 날짜, 식사종류를 조건으로 추천수가 가장 많은 식사메뉴를 조회(추천수가 같을 경우 조회수 많은 순으로)
* **MemberBulkInsertRepositoryTest**
  * 1,000개의 데이터 벌크 insert
  * JdbcTemplate를 사용하여 1,000개의 데이터 벌크 insert
* **UosRestaurantServiceResponseConverterTest**
  * 학식 메뉴 조회 결과를 TextCard에 추천버튼을 포함하고 QuickReplies를 포함하는 응답으로 변경할 때 추천 스킬블록이 없으면 예외가 발생한다.
  * String을 받아 SimpleText형태로 변환한다.
  * 학식 메뉴 조회 결과를 TextCard에 추천버튼을 포함하고 QuickReplies를 포함하는 응답으로 변경한다.
* **SkillBlockRepositoryTest**
  * blockName을 포함하는 SkillBlock을 조회한다.
* **MemberServiceTest**
  * 캐시에 있는 회원의 방문 횟수를 데이터베이스와 동기화 한다.


## 사용 기술

<div align="center">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
<img src="https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<br /><br />

<img src="https://img.shields.io/badge/maridaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white">
<img src="https://img.shields.io/badge/H2_Database-1F305F?style=for-the-badge&logo=H2DB&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<br /><br />

<img src="https://img.shields.io/badge/amazon_ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/amazon_rds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
<img src="https://img.shields.io/badge/amazon_Elastic_Cache-4053D6?style=for-the-badge&logo=awselasticcache&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</div>
<br />
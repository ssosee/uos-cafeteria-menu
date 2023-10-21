# <a href="https://pf.kakao.com/_YVxdzG" target="_blank">서울시립대학교 학식 봇</a>

![bot-profile.png](docs%2Fimages%2Fbot-profile.png)
* <a href="https://pf.kakao.com/_YVxdzG" target="_blank">채널 링크</a>

## 목차
* 개발 동기
* 제공 기능
* 이슈 및 업데이트 사항
* 시스템 구조
* RDBMS 구조
* <a href="http://uos-restaurant-bot.shop/test-docs" target="_blank">테스트 항목</a>
* 사용 기술

## 개발 동기
* 👨‍🎓 졸업 전 학교와 관련된 프로젝트를 하나 만들면 재미있을 것 같아 진행
* 🤖 학식 봇이 존재하면 학생들이 이용할지 궁금하여 진행
* 😁 프론트엔드에 대한 부담을 덜 수 있어 개발 확정!

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
* 서버에 처리율 제한 장치(`RateLimiter`) 함께 구현

![system-architecture.png](docs%2Fimages%2Fsystem-architecture.png)

## RDBMS 구조
![erd.png](docs%2Fimages%2Ferd.png)

## <a href="http://uos-restaurant-bot.shop/test-docs" target="_blank">테스트 항목</a>
*  총 85개의 테스트 항목 작성


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
# 🐸MOMSWAY 
![logo2](https://github.com/skd9712/MomsWay/assets/59557044/307edca9-176f-4fb2-a201-09e618b87297)

## 🐸프로젝트 소개
### 모든 길은 맘스웨이로 통합니다. 

맘스웨이는 '교육 카페'와 '지역 카페'가 결합된 학부모 커뮤니티입니다. <br>
맘스웨이에서 학부모님들은 학원 정보는 물론, 학습 및 학교 관련 정보를 활발하게 교류할 수 있습니다.

###  프로젝트 목표

#### 효과적인 학원 홍보 / 학부모 간의 소통 / 이용자 특전
- 주요 고객층에게 효과적으로 학원을 홍보할 수 있는 플랫폼의 역할을 합니다.
- 같은 고민을 가진 학부모들과 대화하고 정보를 공유하며 유대감을 형성합니다
- 커뮤니티 이용자에게만 제공되는 이벤트 및 무료 특강 기회를 제공합니다.

## 🐸 팀원

| 이름 | 역할 | 연락처 | GitHub |
|------|-----------------------|---------------|---------------|
| 황현준 | 마이페이지, 입시게시판 | skd97122@gmail.com | [황현준](https://github.com/skd9712) |
| 김혜연 | 학원홍보, 공지게시판 | loveyrooney@gmail.com | [김혜연](https://github.com/loveyrooney) |
| 은 별 | 메인페이지, 회원기능 | ebyeol628@gmail.com | [은 별](https://github.com/Agstarr) |
| 홍유나 | 신고기능 | yunahong815@gmail.com | [홍유나](https://github.com/yuyuyu1123) |


## 🐸 기술스택
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Secyrity-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-4E7E5A?style=for-the-badge&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white) 
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white) <br>
![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black) 
![AJAX](https://img.shields.io/badge/AJAX-0769AD?style=for-the-badge&logo=ajax&logoColor=white) <br>
![AWS EC2](https://img.shields.io/badge/AWSEC2-FF6F00?style=for-the-badge&logo=amazon-aws&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWSRDS-FF6F00?style=for-the-badge&logo=amazon-aws&logoColor=white)
![API](https://img.shields.io/badge/API-000000?style=for-the-badge&logo=api&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)

## 🐸 WBS 
#### 프로젝트 기간 : 2024.06.19 ~ 2024.06.30
![wbs](https://github.com/skd9712/MomsWay/assets/59557044/e28e1ed0-0e43-4c0a-b43d-cbe130d7cb80)

## 🐸프로젝트 주요이슈
#### CASCADE 남용 - DB connection pool overflow 문제 발생

- 회원 탈퇴 시, 테이블에서 회원 삭제 후 연관된 모든 테이블 cascade 했던 기존 구현
- 💡 cascade 속성 사용하지 않고 탈퇴회원의 비밀번호만 삭제하여 탈퇴처리
- 신고 처리 시, 테이블에서 해당 글 삭제 후 연관된 모든 테이블 cascade 했던 기존 구현
- 💡 cascade 속성 사용하지 않고 orphanRemoval 조건만 설정

#### Page Template 공유하여 사용할 시 협업의 어려움

- 입시 게시판과 학원/공지 게시판의 이미지 수정 부분의 로직 충돌 발생
- 💡 서로가 구현한 내용 모두 이해한 뒤, 최소한의 변경으로 해결 
- 📌 공유할 템플릿의 최초 설계를 어떤 방식으로 해야할 지 알게됨

#### Exception 관련 이슈 

- 글 수정/삭제 접근 url에 글번호만 알면 같은 권한의 다른 유저가 수정/삭제 url에 접근이 가능한 문제 발생
- 💡 해당 메서드마다 authentication 체크하여 작성자 혹은 관리자만 접근 가능하도록 함
- 💡 그 외의 접근 시도들은 에러 페이지 리턴하는 CustomException 처리
- 💡 CustomException 은 DB 리턴이 null이거나, parameter 가 null 이거나 type 오류인 경우, 405 상황 등에 적용함

## 🐸 유스케이스 다이어그램
![스크린샷 2024-06-18 203359](https://github.com/skd9712/MomsWay/assets/59557044/5ceed0bd-14d3-445d-bc39-f35002b9ebf5)

## 🐸 ERD
![momsway_erd](https://github.com/skd9712/MomsWay/assets/59557044/d9d76dfe-6103-45d0-b957-f8e82db7f6a2)

## 🐸 엔티티 설계
![entities](https://github.com/skd9712/MomsWay/assets/59557044/b2ffbf88-e542-4ae9-a374-2afc382a7df2)

## 🐸 클래스 다이어그램
![dtos](https://github.com/skd9712/MomsWay/assets/59557044/2ad99b49-c342-4d13-9c45-0d68fd596878)
![entity3개](https://github.com/skd9712/MomsWay/assets/59557044/39803509-2179-4b14-98df-d20f3b336886)
![likeClassDiagram](https://github.com/skd9712/MomsWay/assets/59557044/54dcaea6-4c37-431c-9053-00408c5b6620)
![reportClassDiagram](https://github.com/skd9712/MomsWay/assets/59557044/53d29016-7ed0-49ac-b2af-596c0068f9c7)

## 🐸 시퀀스 다이어그램
![LikeServiceImpl_insertLike](https://github.com/skd9712/MomsWay/assets/59557044/ca01ea36-fcd1-4569-826b-424733fa58a0)
![reportseq](https://github.com/skd9712/MomsWay/assets/59557044/95e4a276-9864-457c-8926-19bc51997bb3)

## 🐸 시연영상
https://youtu.be/nCiYZTFm5Nk


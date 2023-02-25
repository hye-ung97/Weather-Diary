# Weather-Diary

### 1️⃣ Info
하루 기록 앱 '하루콩' 을 오마주하여 날씨정보를 가져와 지정 날짜에 일기를 작성 할수 있는는 백엔드 api 입니다.

### 2️⃣ Development Environment
1. IntelliJ IDEA
2. Spring boot v3.0.2
3. DB : mysql (JDBC)
4. 날씨 api : OpenWeatherMap API
5. Api doc : springdoc-openapi v2.0.2 (localhost:8080/swagger-ui/index.html)
6. Test : postman

### 3️⃣ Function
1. 주기적으로 매일 새벽 1시에 그날 날씨 정보를 가져와 database 에 저장
2. 날짜를 지정하여 일기 작성, 일기 저장시에 날씨 정보도 함께 저장됨
    1. Database 에 해당 날짜의 날씨 정보가 있다면 가져옴
    2. Daabase 에 해당 날짜의 날씨 정보가 없다면 api 를 호출하여 날씨 정보를 가져옴
3. 기간 설정하여 해당 날짜의 일기를 볼 수 있음
4. 날짜를 지정하여 해당 일기를 삭제하거나 수정할 수 있음

### 4️⃣ Review
1. swagger 적용 부분에서 오류가 발생하여 원인 분석을 해보니 spring 3.0 이상 버전 부터는 호환이 안된다는 점을 알게 되었다.   
spring 사이트에서 spingdoc-openapi 를 적용하면 된다는 것을 알게 되어 사이트 문서를 참고하여 문제를 해결 할 수 있었다.
2. OpenWeatherMap API 처음 사용해보았다.
3. 기존에는 mariadb 를 사용하여 프로젝트를 진행하여 pc 에는 mariadb 만 설치 되어 있었다.     
이번에는 mysql 를 설치하여 사용하였는데 mariadb 와 mysql이 충돌이 심하여 db 가 작동이 안되었다.     
db 충돌 없이 사용하기 위해서는 port 를 다르게 설정 해주면 된다는 것을 알게 되었다.

![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

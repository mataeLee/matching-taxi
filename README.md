# Matching-Taxi
### 실시간 택시승차를 위한 승객-기사 매칭 서비스 프로젝트  
Documentation : [Notion](https://parallel-cornucopia-5d2.notion.site/Matching-Taxi-fd95f9208c6c4b1b9d6f2bc0e013552d)
   
# Collaboration
- Git-flow 브랜치 전략 적용
- pull-request 요청을 코드리뷰 후에 merge

### git branch naming
- 브랜치 타입 이슈 번호를 브랜치 이름의 prefix로 적용하고 뒤에 브랜치 이름을 연결한다.
ex) feature/{number}-{name}

### git commit
- 메시지는 추가 및 변경된 내용을 기반으로 알기 쉽게 정리하여 작성한다
- 제목과 본문은 행을 나눠 분리하여 작성한다
- commit Type을 제목 서두에 적어 분별하기 쉽도록 작성한다

        feat : 기능 개발 추가
        fix : 버그 수정
        style : 코드 스타일 수정
        refactor : 코드 리팩토링, 디자인 패턴 적용 등
        comment : 주석 처리
        test : 누락된 테스트 코드 추가

### Issue Register
- 해당되는 비즈니스 요구사항을 작성한다
- 기술적 목표의 어떤 부분인지 명시한다
- 개발 Task들을 리스트로 정리하여 작성한다

### Pull Reqeust
- 등록된 issue와 연결되도록 PR 작성
    
    `#{issueNumer} {issueTitleName}`

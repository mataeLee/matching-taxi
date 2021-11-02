# 1. 프로젝트 개요
   
   
# 2. 프로젝트 목표
   
   
# 3. 서버 구조도
   
   
# 4. 기술적 문제 해결점
   
   
# 5. 협업
* Git-flow 브랜치 전략 적용
* pull-request 방식으로 코드리뷰

### git branch naming
- 브랜치 타입 이슈 번호를 브랜치 이름의 prefix로 적용하고 뒤에 브랜치 이름을 연결한다. 
ex) feature/{number}-{name}

### git commit
- 메시지는 추가 및 변경된 내용을 기반으로 알기 쉽게 정리하여 작성한다.
  `테스트11번째 -> X`, `git commit 연습을 위한 테스트용 커밋 작성 -> O`
- 제목과 본문은 행을 나눠 분리하여 작성한다.
- commit Type을 제목 서두에 적어 분별하기 쉽도록 작성한다.

        feat : 기능
        fix : 버그 수정
        docs : 문서 수정
        style : 서식 지정, 세미콜론 누락 등 코드 변경이 없는 경우
        refactor : 코드 리팩터링
        test : 누락된 테스트 코드를 추가할 때
        chore : 잡일(?), 빌드 업무나 패키지 매니저 수정할 때

### Issue Register
- 해당되는 비즈니스 요구사항을 작성한다.
- 기술적 목표의 어떤 부분인지 명시한다.
- 개발내용들을 리스트로 정리하여 작성한다.

### Pull Reqeust
- 등록된 issue와 연결되도록 PR 작성
    
    `#{issueNumer} {issueTitleName}`

# Reference
   
git
- [Bitbucket git-flow tutorials](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)
- [좋은 커밋 작성법](https://meetup.toast.com/posts/106)


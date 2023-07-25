# StudyJPAWithSpringBoot

## 엔티티 설계시 주의점
* 엔티티에는 가급적 Setter를 사용하지말자
* 모든 연관관계는 지연로딩으로 설정
* 컬렉션은 필드에서 초기화 하자

## 테이블, 컬럼명 생성 전략
* 하이버네이트 기존 구현: 엔티티의 필드명을 그대로 테이블 명으로 사용
* 스프링 부트 신규 설정(엔티티(필드)->테이블(컬럼))
  * 카멜케이스 -> 언더스코어(memberPoint => member_point)
  * .(점) -> _(언더스코어)
  * 대문자 -> 소문자
  
## 도메인 모델패턴 vs 트랜잭션 스크립트 패턴
 * 도메인 모델패턴(Entity에 비즈니스 로직 존재)
 * 트랜잭션 스크립트 패턴(Service에 비즈니스 로직 존재)
 
## 준영속 엔티티
 * 영속성 엔티티가 더 이상 관리하지 않는 엔티티
 * 준영속성 엔티티를 수정 하는 2가지 방법
   * 변경 감지 기능 사용 -선택해서 변경가능
   * 병합('merge')사용 - 모든 속성을 다 변경시킴(null도 null로 업데이트)
   
## API와 JPA 설계시
 * Entity를 RequestParam으로 사용하면 추후에 문제가 발생한다 => requestParam은 DTO를 만들어서 사용
 * 혹시나 Entity를 외부에 노출할 경우(지양해야함) 양방향 참조시 한 쪽에는 @JsonIgnore를 붙여서 조회시 순환참조를 막는다
 * 지연로딩(Lazy)을 피하기 위해 즉시로딩(Eager)으로 설정하면 안된다
 * 엔티티를 DTO로 변환하거나 DTO로 바로조회하는 두가지 방법이 있음 장단점 있음
 * 엔티티 쿼리 방식 선택 권장 순서
   -우선 엔티티를 DTO로 변환하는 방법을 선택
   -필요하면 페치조인으로 성능 최적화 => 대부분 성능 이슈 해결
   -그래도 안되면 DTO로 직접 조회하는 방법을 사용
   -최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
 * 컬렉션 (1대다) 페치조인시 페이징 처리 불가
 * 컬렉션 (1대다) 페치조인은 1개만 사용할 수 있다.
 
## 컬렉션 페이징 한계 돌파방법
 * xxxToOne 관계는 fetch 조인 
 * 컬렉션은 지연 로딩으로 조회
 * 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size 적용
 - 장점
 	* 쿼리 호출 수가 '1+N' => '1+1'로 최적화
 	* 조인보다 DB 데이터 전송량이 최적화
 	
## API 개발 고급 정리
* 엔티티조회
    * 엔티티를 조회해서 그대로 반환 v1
    * 엔티티 조회 후 DTO로 변환 v2
    * 페치 조인으로 쿼리수 최적화 v3
    * 컬렉션 페이징과 한계돌파 v3.1
        * 컬렉션은 페치 조인시 페이징이 불가능
        * ToOne 관계는 페치 조인으로 쿼리 수 최적화
        * 컬렉션은 페치 조인 대신에 지연 로딩을 유지하고, hibernate.default_batch_fetch_size, @BatchSize로 최적화
* DTO 직접 조회
    * JPA에서 DTO를 직접 조회 v4
    * 컬렉션 조회 최적화 - 일대다 관계인 컬렉션은 IN 절을 황용해서 메모리에 미리 조회해서 최적화 v5
    * 플랫 데이터 최적화 - JOIN 결과를 그대로 조회 후 어플리케이션에서 원하는 모양으로 직접 변환 v6
* 권장순서<br>
 1.엔티티 조회 방식으로 우선 접근
    1. 페치조인으로 쿼리 수를 최적화
    2. 컬렉션 최적화
       1. 페이징 필요 hibernate.default_batch_fetch_size, @BatchSize
       2. 페이징 필요x -> 페치 조인 사용
 2. 엔티티 조회 방식으로 해결이 안되면 DTO 조회 방식 사용
 3. DTO 조회 방식으로 해결이 안되면 NativeSQL or 스프링 JdbcTemplate
 
## OSIV와 성능 최적화
 * Open Session In View: 하이버네이트
 * Open EntityManager In View:JPA
 * spring.jpa.open-in-view : true(default)
     * true면 API 요청이 끝날때까지/화면단에 다 부려질때까지 data 영속성이 살아 있음 
 
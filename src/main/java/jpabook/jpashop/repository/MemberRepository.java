package jpabook.jpashop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByName(String name);

}

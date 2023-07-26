package jpabook.jpashop.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;

@RunWith(SpringRunner.class) //Junit실행시 Spring과 함께 실행
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"}) // 테스트 설정용 yml 사용하고싶을때
@Transactional //Test에서 사용시 Rollback
public class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepositoryOld memberRepository;

	@Test
	@Rollback(false)
	public void 회원가입() throws Exception {
		// given
		Member member = new Member();
		member.setName("kim");

		// when
		Long saveId = memberService.join(member);

		// then
		assertEquals(member, memberRepository.findOne(saveId));
	}

	@Test(expected = IllegalStateException.class)
	public void 중복_회원_예외() throws Exception {
		// given
		Member member1 = new Member();
		member1.setName("kim");

		Member member2 = new Member();
		member2.setName("kim");

		// when
		memberService.join(member1);
		memberService.join(member2);

		// then
		fail("예외가 발생해야 한다.");
	}

}

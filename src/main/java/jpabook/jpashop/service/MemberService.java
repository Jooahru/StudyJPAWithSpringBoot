package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepositoryOld memberRepositoryOld;

	private final MemberRepository memberRepository;

	// 회원가입
	@Transactional
	public Long join(Member member) {
		validateDuplidateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplidateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());
		if (!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	// 회원 전체 조회
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member findOne(Long memberId) {
		return memberRepository.findById(memberId).get();
	}

	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findById(id).get();
		member.setName(name);
	}

}

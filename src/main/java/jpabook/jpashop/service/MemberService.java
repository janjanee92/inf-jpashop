package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     *  회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMemberName(member.getName());    //  중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMemberName(String name) {
        List<Member> findMembers = memberRepository.findByName(name);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     *  회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 한명 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    }

    /**
     * 회원 정보 수정
     * @param id 회원 아이디
     * @param name 회원이름
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).orElseThrow();
        validateDuplicateMemberName(name);
        member.setName(name);
    }
}

package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    void 회원가입() {
        //  given
        Member member = new Member();
        member.setName("lee");

        //  when
        Long savedId = memberService.join(member);

        //  then
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }
    
    @Test
    void 중복_회원_예외() {
        //  given
        Member member1 = new Member();
        member1.setName("lee");

        Member member2 = new Member();
        member2.setName("lee");

        //  when
        memberService.join(member1);

        //  then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}
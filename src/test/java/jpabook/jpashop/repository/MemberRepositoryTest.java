package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    void member() {
        //  given
        Member member = new Member();
        member.setName("hello");
        Member member2 = new Member();
        member2.setName("hi");

        //  when
        memberRepository.save(member);
        memberRepository.save(member2);

        //  then

        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);

    }

}
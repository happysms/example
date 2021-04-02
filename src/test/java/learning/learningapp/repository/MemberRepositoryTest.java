package learning.learningapp.repository;

import learning.learningapp.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired // 인젝션을 받
    MemberRepository memberRepository;

//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testMember() throw Exception {
//
//
//    }
}
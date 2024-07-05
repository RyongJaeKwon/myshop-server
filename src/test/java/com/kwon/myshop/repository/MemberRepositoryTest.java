package com.kwon.myshop.repository;

import com.kwon.myshop.domain.Address;
import com.kwon.myshop.domain.Member;
import com.kwon.myshop.domain.Role;
import com.kwon.myshop.exception.MemberNotFoundException;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
//@Transactional
@Slf4j
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("테스트를 위한 회원데이터 등록")
    public void saveMembersTest() throws Exception {
        for (int i=0; i<10; i++){
            Address address = Address.builder()
                    .postcode("1111" + i)
                    .basic_address("서울시" + i)
                    .detail_address("강남구" + i)
                    .build();

            Member member = Member.builder()
                    .email("kwon" + i + "@naver.com")
                    .userId("kyj53"+ i)
                    .password(passwordEncoder.encode("@Rnjsdydwo1"))
                    .name("kwon" + i)
                    .phone("010-0000-000" + i)
                    .address(address)
                    .build();

            if (i < 7) {
                member.addRole(Role.MEMBER);
            }else {
                member.addRole(Role.ADMIN);
            }

            memberRepository.save(member);
        }
    }

    @Test
    public void deleteByMemberEmailTest() {
        String email = "kwon5@naver.com";
        memberRepository.deleteByEmail(email);

        Assertions.assertNull(memberRepository.findByEmail(email));

    }

    @Test
    public void saveAddressTest() {
        Address address = Address.builder().postcode("12345").basic_address("서울시").detail_address("강남구").build();
        Member member = Member.builder().name("kwon").email("gyj343@naver.com").password(passwordEncoder.encode("1234")).phone("010-1111-2222").address(address).build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원데이터 등록")
    public void saveMemberTest() throws Exception {
        //given
        Member member = Member.builder().name("kwon").email("gyj343@naver.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-1111-2222").build();

        //when
        memberRepository.save(member);

        //then
        Member savedMember = memberRepository.findByEmail("gyj343@naver.com").orElseThrow(() -> new MemberNotFoundException());

        Assertions.assertNotNull(savedMember);
        Assertions.assertEquals(member.getId(), savedMember.getId());
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
        Assertions.assertEquals(member.getName(), savedMember.getName());
        Assertions.assertEquals(member.getPassword(), savedMember.getPassword());
        Assertions.assertEquals(member.getPhone(), savedMember.getPhone());
    }

    @Test
    @DisplayName("회원데이터 조회")
    public void findByMemberTest() throws Exception {
        //given
        Member member = Member.builder().name("kwon").email("gyj343@naver.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-1111-2222").build();

        memberRepository.save(member);

        //when
        Member savedMember = memberRepository.findById(member.getId()).get();

        //then
        Assertions.assertNotNull(savedMember);
        Assertions.assertEquals(member.getId(), savedMember.getId());
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
        Assertions.assertEquals(member.getName(), savedMember.getName());
        Assertions.assertEquals(member.getPassword(), savedMember.getPassword());
        Assertions.assertEquals(member.getPhone(), savedMember.getPhone());
    }

    @Test
    @DisplayName("회원데이터 모두조회")
    public void findAllByMemberTest() throws Exception {
        //given
        Member member1 = Member.builder().name("kwon").email("gyj343@naver.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-1111-2222").build();
        Member member2 = Member.builder().name("kim").email("kim@gmail.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-2222-3333").build();
        Member member3 = Member.builder().name("lee").email("lee@naver.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-3333-4444").build();
        Member member4 = Member.builder().name("park").email("park@naver.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-4444-5555").build();
        Member member5 = Member.builder().name("choi").email("choi@naver.com").password(passwordEncoder.encode("@%QWdiasdfnxj1")).phone("010-5555-6666").build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Assertions.assertNotNull(member1);
        Assertions.assertNotNull(member2);
        Assertions.assertNotNull(member3);
        Assertions.assertNotNull(member4);
        Assertions.assertNotNull(member5);
        Assertions.assertEquals(member1.getId(), memberList.get(0).getId());
        Assertions.assertEquals(member1.getEmail(), memberList.get(0).getEmail());
        Assertions.assertEquals(member2.getId(), memberList.get(1).getId());
        Assertions.assertEquals(member2.getEmail(), memberList.get(1).getEmail());
        Assertions.assertEquals(member3.getId(), memberList.get(2).getId());
        Assertions.assertEquals(member3.getEmail(), memberList.get(2).getEmail());
        Assertions.assertEquals(member4.getId(), memberList.get(3).getId());
        Assertions.assertEquals(member4.getEmail(), memberList.get(3).getEmail());
        Assertions.assertEquals(member5.getId(), memberList.get(4).getId());
        Assertions.assertEquals(member5.getEmail(), memberList.get(4).getEmail());
    }

}

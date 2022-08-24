package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.MyInfo;
import project.academyshow.entity.Member;
import project.academyshow.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByUsername(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        member.orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        return member.get();
    }

    /** 마이페이지 - 내 정보 수정 */
    public void updateMember(String username, MyInfo myInfo) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        optionalMember.orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        optionalMember.get().updateMember(myInfo);
    }
}

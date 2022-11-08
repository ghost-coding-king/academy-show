package project.academyshow.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.academyshow.entity.Member;
import project.academyshow.entity.ProviderType;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.entity.CustomUserDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /** Database 에서 Member 조회 (AuthenticationManager 가 authenticate() 에 사용) */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byUsername = memberRepository.findByUsernameAndProviderType(username, ProviderType.LOCAL);
        byUsername.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Member member = byUsername.get();
        return CustomUserDetails.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .providerType(member.getProviderType())
                .build();
    }
}

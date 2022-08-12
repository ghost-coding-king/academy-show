package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.LoginRequest;
import project.academyshow.controller.request.SignUpRequest;
import project.academyshow.entity.Member;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.token.AuthToken;
import project.academyshow.security.token.AuthTokenProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    /** 회원가입 */
    public void signUp(SignUpRequest signUpRequest) {
        Member newMember = Member.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        memberRepository.save(newMember);
    }

    /** 로그인
     * @return AuthToken */
    public AuthToken login(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));
            return authTokenProvider.createToken(authenticate);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}

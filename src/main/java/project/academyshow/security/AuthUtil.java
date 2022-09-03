package project.academyshow.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import project.academyshow.entity.Member;
import project.academyshow.security.entity.CustomUserDetails;

public class AuthUtil {
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    public static Member getLoggedInMember() { return getUserDetails().getMember(); }

    public static String getLoggedInUsername() {
        return getAuthentication().getName();
    }

    public static void checkIsOwner(Member resourceOwner) {
        if(!getLoggedInMember().getId().equals(resourceOwner.getId()))
            throw new RuntimeException("해당 리소스에 대한 권한이 없습니다.");
    }
}

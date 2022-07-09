package cl.zeruscode.treip.domain.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface DentalUserDetails extends UserDetails {
    public String getEmail();
}

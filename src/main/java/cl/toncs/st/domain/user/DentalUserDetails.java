package cl.toncs.st.domain.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface DentalUserDetails extends UserDetails {
    String getEmail();
}

package cl.zeruscode.treip.service.user;

import cl.zeruscode.treip.domain.SecurityUserDetails;
import cl.zeruscode.treip.domain.user.DentalUserDetails;
import cl.zeruscode.treip.entities.user.RoleEntity;
import cl.zeruscode.treip.entities.user.UserEntity;
import cl.zeruscode.treip.mapper.UserMapper;
import cl.zeruscode.treip.repository.RoleRepository;
import cl.zeruscode.treip.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(@Nonnull String username) throws UsernameNotFoundException {
        var user = userRepository.findByName(username)
                                 .orElseThrow(() -> new UsernameNotFoundException(username));
        return new SecurityUserDetails(user);

    }

    public DentalUserDetails save(@Nonnull DentalUserDetails userDetails) {
        UserEntity user = toUserEntity(userDetails);
        user = userRepository.save(user);
        return toUserDetails(user);
    }

    public DentalUserDetails findById(@Nonnull Long id) {
        return userRepository.findById(id)
                                 .map(this::toUserDetails)
                                 .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserEntity toUserEntity(DentalUserDetails userDetails) {
        java.util.List<cl.zeruscode.treip.entities.user.RoleEntity> rolesEntities = toRoleEntities(userDetails.getAuthorities());
        return new UserEntity(userDetails.getUsername(), userDetails.getEmail(), userDetails.getPassword(), rolesEntities);
    }

    private List<RoleEntity> toRoleEntities(Collection<? extends GrantedAuthority> authorities) {
        var roles = authorities.stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toList());
        return roleRepository.findAllByNames(roles);
    }

    private DentalUserDetails toUserDetails(UserEntity userEntity) {
        return UserMapper.INSTANCE.toUserDetails(userEntity);
    }
}

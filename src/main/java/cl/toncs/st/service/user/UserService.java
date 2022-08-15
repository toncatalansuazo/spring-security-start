package cl.toncs.st.service.user;

import cl.toncs.st.entities.user.RoleEntity;
import cl.toncs.st.entities.user.UserEntity;
import cl.toncs.st.exception.UserEmailNotFoundException;
import cl.toncs.st.repository.RoleRepository;
import cl.toncs.st.repository.UserRepository;
import cl.toncs.st.domain.user.DentalUser;
import cl.toncs.st.domain.user.PatchDentalUser;
import cl.toncs.st.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    /** we are going to use just email */
    @Override
    public DentalUser loadUserByUsername(@Nonnull String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
                                 .orElseThrow(() -> new UserEmailNotFoundException(username));
        return userMapper.toDentalUser(user);

    }

    public DentalUser save(@Nonnull DentalUser userDetails) {
        UserEntity user = toUserEntity(userDetails);
        userRepository.save(user);
        return toDentalUser(user);
    }

    public DentalUser findById(@Nonnull Long id) {
        return userRepository.findById(id)
                                 .map(this::toDentalUser)
                                 .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public PageImpl<DentalUser> findAll(Pageable pageable) {
        var users = userRepository.findAll(pageable).stream()
                                  .map(this::toDentalUser)
                                  .toList();
        return new PageImpl<>(users);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public DentalUser update(@Nonnull Long userId, @Nonnull PatchDentalUser patchDentalUser) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(""));
        if (patchDentalUser.getUsername() != null && patchDentalUser.getUsername().length() > 0) {
            user.setUsername(patchDentalUser.getUsername());
        }
        if (patchDentalUser.getEmail() != null) {
            user.setEmail(patchDentalUser.getEmail());
        }
        if (patchDentalUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(patchDentalUser.getPassword()));
        }

        return toDentalUser(userRepository.save(user));
    }

    private UserEntity toUserEntity(DentalUser userDetails) {
        java.util.List<RoleEntity> rolesEntities = toRoleEntities(userDetails.getAuthorities());
        return new UserEntity(userDetails.getUsername(), userDetails.getEmail(), userDetails.getPassword(), rolesEntities);
    }

    private List<RoleEntity> toRoleEntities(Set<SimpleGrantedAuthority> authorities) {
        Set<String> roles = authorities.stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.toSet());
        return roleRepository.findAllByNames(roles);
    }

    private DentalUser toDentalUser(UserEntity userEntity) {
        return userMapper.toDentalUser(userEntity);
    }
}

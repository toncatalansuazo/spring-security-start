package cl.toncs.st;

import static org.assertj.core.api.Assertions.assertThat;

import cl.toncs.st.entities.user.RoleType;
import cl.toncs.st.entities.user.UserEntity;
import cl.toncs.st.repository.RoleRepository;
import cl.toncs.st.repository.UserRepository;
import cl.toncs.st.rest.AuthenticationController;
import cl.toncs.st.rest.BaseTestContainer;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringSecurityStart.class)
@ExtendWith(SpringExtension.class)
class SpringSecurityStartTests extends BaseTestContainer {

	@Autowired
	private AuthenticationController authenticationController;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@BeforeEach
	void cleanDb() {
	}
	@Test
	void contextLoads() {
		assertThat(authenticationController).isNotNull();
	}

	@Test
	void roles_and_users_are_created() {
		Assertions.assertThat(userRepository.findByName("superadmin").get()).isNotNull();
		Assertions.assertThat(userRepository.findByName("user").get()).isNotNull();
		Assertions.assertThat(roleRepository.findAll()).hasSize(2);
	}

	@Test
	@Transactional
	void given_deleted_user_role_is_not_deleted() {
		// given user with role admin
		java.util.List<UserEntity> users = changeAdminToUserRole();
		Assertions.assertThat(roleRepository.findAll()).hasSize(2);
	}

	@NotNull
	public List<UserEntity> changeAdminToUserRole() {
		// given user with role admin
		var adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN);
		var userRole = roleRepository.findByName(RoleType.ROLE_USER);
		var users = adminRole.getUsers().stream()
							 .map(user -> {
								 var userRoles = user.getRoles();
								 userRoles.remove(adminRole);
								 userRoles.add(userRole);
								 return user;
							 }).collect(Collectors.toList());

		userRepository.saveAllAndFlush(users);
		return users;
	}

}

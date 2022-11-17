package cl.toncs.st.rest.v1;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cl.toncs.st.domain.user.DentalUser;
import cl.toncs.st.domain.user.PatchDentalUser;
import cl.toncs.st.mapper.UserMapper;
import cl.toncs.st.service.user.UserService;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/{user_id}")
    public ResponseEntity<DentalUser> findById(@PathVariable("user_id") @Nonnull Long userId) {
        var user = userService.findById(userId);
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping
    public ResponseEntity<DentalUser> save(@RequestBody @Valid DentalUser userDto) {
        var user = userMapper.userToUserDetail(userDto);
        user = userService.save(user);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping
    public ResponseEntity<Page<DentalUser>> findAll(@Nonnull Pageable pageable) {
        var users = userService.findAll(pageable);
        return new ResponseEntity<>(users, OK);
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<DentalUser> patch(@PathVariable("user_id") @Nonnull Long userId, @RequestBody @Valid PatchDentalUser patchDentalUser) {
        DentalUser user = userService.update(userId, patchDentalUser);
        return new ResponseEntity<>(user, OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> delete(@PathVariable("user_id") @Nonnull Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(OK);
    }
}

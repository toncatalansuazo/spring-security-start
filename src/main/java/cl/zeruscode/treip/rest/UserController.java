package cl.zeruscode.treip.rest;

import cl.zeruscode.treip.domain.user.UserDto;
import cl.zeruscode.treip.mapper.UserMapper;
import cl.zeruscode.treip.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController("api/v1/users")
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDetails> save(@RequestBody @Validated UserDto userDto) {
        var user = UserMapper.INSTANCE.userToUserDetail(userDto);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDetails> findById(@RequestParam Long userId) {
        var user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

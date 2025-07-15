package com.arfat.orchastrator.user;

import com.arfat.user.UserService;
import com.arfat.user.model.UpdateUser;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        var user =  userService.findUserByUsername(username);

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Validated @RequestBody UserRequest userRequest) {
        var user = userRequest.toUser();
        var created = userService.createUser(user);

        return ResponseEntity.ok(UserResponse.from(created));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponse> updateUser(
            @NotNull @PathVariable String username,
            @Validated @RequestBody UserRequest userRequest) {

        UpdateUser updateUser = userRequest.toUpdateUser();
        var updatedUser = userService.updateUser(username, updateUser);

        return ResponseEntity.ok(UserResponse.from(updatedUser));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable String username) {
        userService.deleteUser(username);

        return ResponseEntity.noContent().build();
    }
}

package security.app.api.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import security.app.api.security.JWTUtil;
import security.app.api.service.UserService;

@Data
@NoArgsConstructor
@AllArgsConstructor
class SignInRequest {
    private String username;
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SignInResponse {
    private String token;
}

@RestController
@RequestMapping(value = "/api")
public class AuthController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManage;

    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        final Authentication authentication = authenticationManage.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userService.loadUserByUsername(signInRequest.getUsername());

        String token = jwtUtil.generatedToken(userDetails);

        SignInResponse signInResponse = new SignInResponse(token);

        return ResponseEntity.ok(signInResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("TEST");
    }
}

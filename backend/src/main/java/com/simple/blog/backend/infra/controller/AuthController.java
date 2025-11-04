package com.simple.blog.backend.infra.controller;

import com.simple.blog.backend.core.exception.DomainException;
import com.simple.blog.backend.core.usecases.auth.activation.IActivateAccountUseCase;
import com.simple.blog.backend.core.usecases.auth.login.LoginInput;
import com.simple.blog.backend.core.usecases.auth.login.LoginOutput;
import com.simple.blog.backend.core.usecases.auth.login.ILoginUseCase;
import com.simple.blog.backend.core.usecases.auth.logout.ILogoutUseCase;
import com.simple.blog.backend.core.usecases.auth.refresh.IRefreshTokenUseCase;
import com.simple.blog.backend.core.usecases.auth.register.RegisterInput;
import com.simple.blog.backend.core.usecases.auth.register.IRegisterUseCase;
import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.config.swagger.docs.AuthControllerDoc;
import com.simple.blog.backend.infra.dto.input.LoginRequestDTO;
import com.simple.blog.backend.infra.dto.input.RefreshTokenDTO;
import com.simple.blog.backend.infra.dto.input.RegisterRequestDTO;
import com.simple.blog.backend.infra.dto.output.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDoc {

    private final IRegisterUseCase registerUseCase;
    private final IActivateAccountUseCase activateAccountUseCase;
    private final ILoginUseCase loginUseCase;
    private final IRefreshTokenUseCase refreshTokenUseCase;
    private final ILogoutUseCase logoutUseCase;

    public AuthController(IRegisterUseCase registerUseCase, IActivateAccountUseCase activateAccountUseCase, ILoginUseCase loginUseCase, IRefreshTokenUseCase refreshTokenUseCase, ILogoutUseCase logoutUseCase) {
        this.registerUseCase = registerUseCase;
        this.activateAccountUseCase = activateAccountUseCase;
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO dto){
        this.registerUseCase.execute(new RegisterInput(dto.name(), dto.username(), dto.email(), dto.password()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Check your e-mail to activate your account"));
    }

    @GetMapping("/activate")
    public ResponseEntity<Map<String, String>> activateAccount(@RequestParam String code){
        this.activateAccountUseCase.execute(code);
        return ResponseEntity.ok(Map.of("message", "Account activated successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        LoginOutput loginOutput = this.loginUseCase.execute(new LoginInput(dto.email(), dto.password()));
        return ResponseEntity.ok(new LoginDTO(loginOutput.accessToken(), loginOutput.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginDTO> refresh(@RequestBody @Valid RefreshTokenDTO dto){
        LoginOutput loginOutput = this.refreshTokenUseCase.execute(dto.refreshToken());
        return ResponseEntity.ok(new LoginDTO(loginOutput.accessToken(), loginOutput.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String refreshToken, @AuthenticationPrincipal SimpleBlogUserDetails user){
        if(user == null) throw new DomainException("User not found");
        this.logoutUseCase.execute(user.getId(), refreshToken);
        return ResponseEntity.noContent().build();
    }

}

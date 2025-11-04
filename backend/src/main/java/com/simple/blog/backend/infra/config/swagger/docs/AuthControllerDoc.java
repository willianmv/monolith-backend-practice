package com.simple.blog.backend.infra.config.swagger.docs;

import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.dto.input.LoginRequestDTO;
import com.simple.blog.backend.infra.dto.input.RefreshTokenDTO;
import com.simple.blog.backend.infra.dto.input.RegisterRequestDTO;
import com.simple.blog.backend.infra.dto.output.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Auth", description = "Endpoints para registro, ativação e login de users")
public interface AuthControllerDoc {

    @Operation(
            summary = "Registrar novo usuário",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário registrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Usuário Registrado",
                                            value = """
                                                    {
                                                     "message": "Check your e-mail to activate your account"
                                                    }
                                                    """))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro de Validação",
                                            value = """
                                                     {
                                                      "timestamp": "2025-08-07T12:34:56-03:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Dados inválidos",
                                                      "path": "/api/auth/register",
                                                      "fieldErrors": [
                                                        {
                                                          "field": "email",
                                                          "message": "Formato de e-mail inválido"
                                                        },
                                                        {
                                                          "field": "password",
                                                          "message": "Senha deve conter pelo menos 6 caracteres"
                                                        }
                                                      ]
                                                     }
                                                    """))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro Interno",
                                            value = """
                                                       {
                                                         "timestamp": "2025-08-07T12:35:00-03:00",
                                                         "status": 500,
                                                         "error": "Unexpected error occurred",
                                                         "message": "java.lang.NullPointerException",
                                                         "path": "/api/auth/register"
                                                       }
                                                    """
                                    )))
            }
    )
    ResponseEntity<Map<String, String>> register(RegisterRequestDTO dto);


    @Operation(
            summary = "Ativar conta",
            description = "Ativa a conta do usuário usando o código enviado por e-mail.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conta ativada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conta Ativada",
                                            value = """
                                                    {
                                                     "message": "Account activated successfully!"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Código inválido ou expirado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Código Inválido ou Expirado",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:36:00-03:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Code expired",
                                                      "path": "/api/auth/activate"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro Interno",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:36:30-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.RuntimeException",
                                                      "path": "/api/auth/activate"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<Map<String, String>> activateAccount(String code);

    @Operation(
            summary = "Login do usuário",
            description = "Faz login e retorna o token de autenticação JWT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Login Bem-sucedido",
                                            value = """
                                                    {
                                                      "token": "eyJhbGciOiJIUzI1NiIsInR...",
                                                      "refreshToken": "bf86d857-342e-4e34-a651-662a..."
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de autenticação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Credenciais Inválidas",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:37:00-03:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Invalid password",
                                                      "path": "/api/auth/login"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno no servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro Interno",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:37:30-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.NullPointerException",
                                                      "path": "/api/auth/login"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<LoginDTO> login(LoginRequestDTO dto);

    @Operation(
            summary = "Renovar token",
            description = "Renova o access token utilizando um refresh token válido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens renovados com sucesso", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Tokens Renovados", value = """
                {
                  "token": "eyJhbGciOiJIUzI1NiIsInR...",
                  "refreshToken": "29e6f172-a2bc-4320-aab0-b5bcd70f5f1f"
                }
            """)
                    )),
                    @ApiResponse(responseCode = "400", description = "Refresh token inválido ou expirado", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Token Inválido", value = """
                {
                  "timestamp": "2025-08-07T12:38:00-03:00",
                  "status": 400,
                  "error": "Bad Request",
                  "message": "Refresh token inválido ou expirado",
                  "path": "/api/auth/refresh"
                }
            """)
                    )),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Erro Interno", value = """
                {
                  "timestamp": "2025-08-07T12:38:30-03:00",
                  "status": 500,
                  "error": "Unexpected error occurred",
                  "message": "java.lang.Exception",
                  "path": "/api/auth/refresh"
                }
            """)
                    ))
            }
    )
    ResponseEntity<LoginDTO> refresh(RefreshTokenDTO dto);

    @Operation(
            summary = "Logout do usuário",
            description = "Revoga o refresh token do usuário, encerrando a sessão atual.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Refresh token inválido", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Token Inválido", value = """
                {
                  "timestamp": "2025-08-07T12:39:00-03:00",
                  "status": 400,
                  "error": "Bad Request",
                  "message": "Failed to revoke refresh token or token not found",
                  "path": "/api/auth/logout"
                }
            """)
                    )),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Erro Interno", value = """
                {
                  "timestamp": "2025-08-07T12:39:30-03:00",
                  "status": 500,
                  "error": "Unexpected error occurred",
                  "message": "java.lang.Exception",
                  "path": "/api/auth/logout"
                }
            """)
                    ))
            }
    )
    ResponseEntity<Void> logout(String refreshToken,  SimpleBlogUserDetails user);

}

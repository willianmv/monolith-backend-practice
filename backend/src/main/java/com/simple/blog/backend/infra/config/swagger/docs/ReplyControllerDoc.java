package com.simple.blog.backend.infra.config.swagger.docs;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.usecases.reply.get.ReplySummaryOutput;
import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.dto.input.CreateReplyDTO;
import com.simple.blog.backend.infra.dto.output.CreatedReplyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Replies", description = "Endpoints para criação, listagem e exclusão de respostas (replies) em posts")
public interface ReplyControllerDoc {

    @Operation(
            summary = "Criar uma nova resposta",
            description = "Cria uma nova resposta (reply) associada a um post existente. É necessário estar autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Resposta criada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Resposta Criada",
                                            value = """
                                                    {
                                                      "id": 501,
                                                      "postId": 101,
                                                      "authorId": 1,
                                                      "authorName": "João Silva",
                                                      "content": "Concordo totalmente com o post!",
                                                      "createdAt": "2025-08-07T14:10:00-03:00"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação nos dados de entrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro de Validação",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T14:11:00-03:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "O campo 'content' não pode estar vazio",
                                                      "path": "/api/replies"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post associado não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Post Não Encontrado",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T14:12:00-03:00",
                                                      "status": 404,
                                                      "error": "Not Found",
                                                      "message": "Post com ID 101 não encontrado",
                                                      "path": "/api/replies"
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
                                                      "timestamp": "2025-08-07T14:13:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.RuntimeException",
                                                      "path": "/api/replies"
                                                    }
                                                    """
                                    )))
            }
    )
    CreatedReplyDTO createReply(SimpleBlogUserDetails user, CreateReplyDTO input);


    @Operation(
            summary = "Listar respostas de um post",
            description = "Retorna uma lista paginada de respostas (replies) associadas a um post específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de respostas retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Lista de Respostas",
                                            value = """
                                                    {
                                                      "content": [
                                                        {
                                                          "id": 501,
                                                          "postId": 101,
                                                          "authorName": "João Silva",
                                                          "content": "Excelente post!",
                                                          "createdAt": "2025-08-07T14:14:00-03:00"
                                                        },
                                                        {
                                                          "id": 502,
                                                          "postId": 101,
                                                          "authorName": "Maria Souza",
                                                          "content": "Muito útil, obrigada por compartilhar!",
                                                          "createdAt": "2025-08-07T14:16:00-03:00"
                                                        }
                                                      ],
                                                      "page": 0,
                                                      "size": 10,
                                                      "totalElements": 2,
                                                      "totalPages": 1
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Post Não Encontrado",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T14:17:00-03:00",
                                                      "status": 404,
                                                      "error": "Not Found",
                                                      "message": "Post com ID 101 não encontrado",
                                                      "path": "/api/replies/101/replies"
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
                                                      "timestamp": "2025-08-07T14:18:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.Exception",
                                                      "path": "/api/replies/101/replies"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<DomainPage<ReplySummaryOutput>> getRepliesByPost(Long postId, int page, int size);


    @Operation(
            summary = "Excluir uma resposta",
            description = "Exclui uma resposta existente. Somente o autor da resposta pode removê-la.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resposta excluída com sucesso"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não autorizado a excluir esta resposta",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Acesso Negado",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T14:19:00-03:00",
                                                      "status": 403,
                                                      "error": "Forbidden",
                                                      "message": "Você não tem permissão para excluir esta resposta",
                                                      "path": "/api/replies/501"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Resposta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Resposta Não Encontrada",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T14:20:00-03:00",
                                                      "status": 404,
                                                      "error": "Not Found",
                                                      "message": "Resposta com ID 501 não encontrada",
                                                      "path": "/api/replies/501"
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
                                                      "timestamp": "2025-08-07T14:21:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.Exception",
                                                      "path": "/api/replies/501"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<Void> deleteReply(Long replyId, SimpleBlogUserDetails user);


}

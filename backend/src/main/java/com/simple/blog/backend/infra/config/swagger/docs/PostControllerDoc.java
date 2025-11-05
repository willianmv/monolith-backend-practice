package com.simple.blog.backend.infra.config.swagger.docs;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.usecases.post.detailed.DetailedPostOutput;
import com.simple.blog.backend.core.usecases.post.summary.PostQueryFilter;
import com.simple.blog.backend.core.usecases.post.summary.SummaryPostOutput;
import com.simple.blog.backend.infra.config.security.SimpleBlogUserDetails;
import com.simple.blog.backend.infra.dto.output.CreatedPostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Tag(name = "Posts", description = "Endpoints para criação, listagem, detalhamento e exclusão de posts")
public interface PostControllerDoc {

    @Operation(
            summary = "Criar um novo post",
            description = "Cria um novo post com título, conteúdo, tags e imagem opcional.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Post Criado",
                                            value = """
                                                    {
                                                      "id": 101,
                                                      "title": "Meu primeiro post",
                                                      "content": "Este é o conteúdo do post.",
                                                      "imageUrl": "https://cdn.simpleblog.com/images/post101.jpg",
                                                      "authorId": 1,
                                                      "tags": ["Spring", "Backend", "Java"],
                                                      "createdAt": "2025-08-07T12:45:00-03:00"
                                                    }
                                                    """
                                    ))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação dos campos",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro de Validação",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:46:00-03:00",
                                                      "status": 400,
                                                      "error": "Bad Request",
                                                      "message": "Título e conteúdo são obrigatórios",
                                                      "path": "/api/posts"
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
                                                      "timestamp": "2025-08-07T12:47:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.RuntimeException",
                                                      "path": "/api/posts"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<CreatedPostDTO> createPost(
            String title,
            String content,
            Set<String> tagNames,
            MultipartFile image,
            SimpleBlogUserDetails user
    );


    @Operation(
            summary = "Listar posts",
            description = "Retorna uma lista paginada de posts, podendo filtrar por título, autor, tags e ordem de criação.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de posts retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Lista de Posts",
                                            value = """
                                                    {
                                                      "content": [
                                                        {
                                                          "id": 101,
                                                          "title": "Como usar Spring Boot",
                                                          "summary": "Um guia prático sobre Spring Boot",
                                                          "author": "João Silva",
                                                          "tags": ["Spring", "Java"],
                                                          "createdAt": "2025-08-07T12:48:00-03:00"
                                                        },
                                                        {
                                                          "id": 102,
                                                          "title": "Integração com Swagger",
                                                          "summary": "Documente sua API facilmente",
                                                          "author": "Maria Souza",
                                                          "tags": ["Swagger", "API"],
                                                          "createdAt": "2025-08-06T10:22:00-03:00"
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
                            responseCode = "500",
                            description = "Erro interno no servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Erro Interno",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:49:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.Exception",
                                                      "path": "/api/posts"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<DomainPage<SummaryPostOutput>> getPosts(
            int page,
            int size,
            String title,
            Set<String> tags,
            Long authorId,
            PostQueryFilter.SortOrder sortOrder
    );

    @Operation(
            summary = "Obter detalhes de um post",
            description = "Retorna os detalhes completos de um post específico pelo seu ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post encontrado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Post Detalhado",
                                            value = """
                                                    {
                                                      "id": 101,
                                                      "title": "Como usar Spring Boot",
                                                      "content": "Conteúdo completo do post...",
                                                      "author": {
                                                        "id": 1,
                                                        "name": "João Silva"
                                                      },
                                                      "tags": ["Spring", "Backend"],
                                                      "imageUrl": "https://cdn.simpleblog.com/images/post101.jpg",
                                                      "createdAt": "2025-08-07T12:50:00-03:00",
                                                      "updatedAt": "2025-08-07T13:00:00-03:00"
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
                                                      "timestamp": "2025-08-07T12:51:00-03:00",
                                                      "status": 404,
                                                      "error": "Not Found",
                                                      "message": "Post com ID 101 não encontrado",
                                                      "path": "/api/posts/101"
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
                                                      "timestamp": "2025-08-07T12:52:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.Exception",
                                                      "path": "/api/posts/101"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<DetailedPostOutput> getPost(Long postId);


    @Operation(
            summary = "Excluir um post",
            description = "Exclui um post existente. Apenas o autor do post pode realizar a exclusão.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post excluído com sucesso"),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não autorizado a excluir o post",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Acesso Negado",
                                            value = """
                                                    {
                                                      "timestamp": "2025-08-07T12:53:00-03:00",
                                                      "status": 403,
                                                      "error": "Forbidden",
                                                      "message": "Você não tem permissão para excluir este post",
                                                      "path": "/api/posts/101"
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
                                                      "timestamp": "2025-08-07T12:54:00-03:00",
                                                      "status": 404,
                                                      "error": "Not Found",
                                                      "message": "Post com ID 101 não encontrado",
                                                      "path": "/api/posts/101"
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
                                                      "timestamp": "2025-08-07T12:55:00-03:00",
                                                      "status": 500,
                                                      "error": "Unexpected error occurred",
                                                      "message": "java.lang.Exception",
                                                      "path": "/api/posts/101"
                                                    }
                                                    """
                                    )))
            }
    )
    ResponseEntity<Void> deletePost(Long postId, SimpleBlogUserDetails user);

}

package com.simple.blog.backend.core.usecases.auth.logout;

public interface ILogoutUseCase {

    void execute(long userId, String refreshToken);

}

package com.simple.blog.backend.core.usecases.auth.login;

public interface ILoginUseCase {

    LoginOutput execute(LoginInput loginInput);

}

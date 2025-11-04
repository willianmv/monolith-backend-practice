package com.simple.blog.backend.core.usecases.auth.refresh;

import com.simple.blog.backend.core.usecases.auth.login.LoginOutput;

public interface IRefreshTokenUseCase {

    LoginOutput execute(String token);

}

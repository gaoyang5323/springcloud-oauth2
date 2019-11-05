package com.example.auth.excepthandler;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: gaoyang
 * @Description:
 */
@RestControllerAdvice
public class RestControllerExceptionAdvice {

    //判断oauth异常,自定义返回数据
    @ExceptionHandler
    public Object exception(OAuth2Exception e){
        //if ("invalid_client".equals(errorCode)) {
        //            return new InvalidClientException(errorMessage);
        //        } else if ("unauthorized_client".equals(errorCode)) {
        //            return new UnauthorizedClientException(errorMessage);
        //        } else if ("invalid_grant".equals(errorCode)) {
        //            return new InvalidGrantException(errorMessage);
        //        } else if ("invalid_scope".equals(errorCode)) {
        //            return new InvalidScopeException(errorMessage);
        //        } else if ("invalid_token".equals(errorCode)) {
        //            return new InvalidTokenException(errorMessage);
        //        } else if ("invalid_request".equals(errorCode)) {
        //            return new InvalidRequestException(errorMessage);
        //        } else if ("redirect_uri_mismatch".equals(errorCode)) {
        //            return new RedirectMismatchException(errorMessage);
        //        } else if ("unsupported_grant_type".equals(errorCode)) {
        //            return new UnsupportedGrantTypeException(errorMessage);
        //        } else if ("unsupported_response_type".equals(errorCode)) {
        //            return new UnsupportedResponseTypeException(errorMessage);
        //        } else {
        //            return (OAuth2Exception)("access_denied".equals(errorCode) ? new UserDeniedAuthorizationException(errorMessage) : new OAuth2Exception(errorMessage));
        //        }
        return "获取token错误";
    }
}

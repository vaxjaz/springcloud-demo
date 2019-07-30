package com.jay.li.springcloudclient.error;

import com.jay.li.springcloudclient.error.exception.BadRequestException;
import com.jay.li.springcloudclient.error.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

/**
 * feign 异常全局处理
 * feign　调用时候异常处理的逻辑是通过http status状态码确定的,
 * 所以一般每个服务最好做global exception handler　返回各种对应的异常状态码，不然内部服务所有抛出的异常都是500,
 * 对外返回全是feign异常或者熔断异常
 */
@Component
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                return new BadRequestException();
            case 404:
                return new NotFoundException();
            default:
                return new Exception("Generic error");
        }
    }
}

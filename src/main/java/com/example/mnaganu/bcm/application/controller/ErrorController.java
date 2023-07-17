package com.example.mnaganu.bcm.application.controller;

import com.example.mnaganu.bcm.application.model.ApiErrorCode;
import com.example.mnaganu.bcm.application.model.ApiStatus;
import com.example.mnaganu.bcm.application.model.response.ErrorResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  @RequestMapping(produces = "application/json")
  ErrorResponse error(HttpServletRequest request) {
    //ErrorHandlerController に到達しない処理をこちらに記載
    //主に 404 エラーはここでしか検知できないようです。

    Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (statusCode != null && statusCode.toString().equals("404")) {
      return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1006, "URL not found [" + urlDecode(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString()) + "]");
    }

    StringBuffer buf = new StringBuffer();
    buf.append("エラーが発生しました。 ");
    buf.append("status:");
    buf.append(statusCode.toString());
    buf.append(" path:");
    buf.append(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

    return new ErrorResponse(ApiStatus.NG, ApiErrorCode.E1007, buf.toString());
  }

  private String urlDecode(String target) {

    String decodedResult = "";
    try {
      decodedResult = URLDecoder.decode(target, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
    return decodedResult;

  }

}
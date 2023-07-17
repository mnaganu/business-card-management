package com.example.mnaganu.bcm.application.model;

public enum ApiErrorCode {
  NONE(0),
  E1001(1001), //offset format エラー
  E1002(1002), //limit format エラー
  E1003(1003), //Business Card ID format エラー
  E1004(1004), //Business Card Name not found エラー
  E1005(1005), // 500 エラー
  E1006(1006), // 404 エラー
  E1007(1007);

  private final int code;

  ApiErrorCode(int code) {
    this.code = code;
  }

  public String toString() {
    return Integer.valueOf(code).toString();
  }

}
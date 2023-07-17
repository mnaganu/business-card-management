package com.example.mnaganu.bcm.application.model;

public enum ApiStatus {
  OK("OK"),
  NG("NG");

  private final String name;

  ApiStatus(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

}

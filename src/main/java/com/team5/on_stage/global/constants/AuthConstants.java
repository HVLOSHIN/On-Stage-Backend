package com.team5.on_stage.global.constants;

public class AuthConstants {

    // Domain - Develop
    static public String DEVELOP_DOMAIN = "localhost";
    static public String DEVELOP_FRONT_DOMAIN = "http://localhost:3000";
    static public String DEVELOP_BACK_DOMAIN = "http://localhost:8080";

    // Domain - Deploy
    static public String DEPLOY_DOMAIN = "59.8.139.239";
    static public String DEPLOY_FRONT_DOMAIN = "http://59.8.139.239:3000";
    static public String DEPLOY_BACK_DOMAIN = "http://59.8.139.239:5000";

    // Domain - Deploy
    static public String DEPLOY_DOMAIN2 = "localhost";
    static public String DEPLOY_FRONT_DOMAIN2 = "http://localhost:3000";
    static public String DEPLOY_BACK_DOMAIN2 = "http://localhost:5000";

    // Token Type
    static public String TYPE_REFRESH = "refresh";
    static public String TYPE_ACCESS = "access";

    // Access Token을 전달할 헤더
    static public String AUTH_HEADER = "Authorization";

    // Access Token의 타입
    static public String AUTH_TYPE = "Bearer ";

    // 각 Token의 유효 시간
    static public Long REFRESH_TOKEN_EXPIRED_MS = 86400000L;
    static public Long ACCESS_TOKEN_EXPIRED_MS = 86400000L;
}

package com.example.yblog.allstatic;

public class IpHostName {
    public static int statusNum=0; // 현재 상태값을 입력하기위한 절대적은 번호 , 1이 끝

    public static  String adminUser = "Y";
    public static  String StaticURL = "";

    public static  String client_id = "872113e237ff5335153b82fd5861def5";

    public static  String LoginRequestURI = "";
    // 주의 : 노트북과 데스크탑,등등 작업환경을 변경할때마다 포트번호를알맞게 변경해주세요
    public static  String grant_type ="authorization_code";
    public static  String redirect_uri ="";
    public static  String Loginredirect_uri ="";
    public static  String OauthToken_request = "https://kauth.kakao.com/oauth/token"; // 인증토큰주소
    public static  String UserInfoSelect = "https://kapi.kakao.com/v2/user/me"; // 사용자 정보조회에 필요함

    public static  String ContentType = "application/x-www-form-urlencoded;charset=utf-8";



}

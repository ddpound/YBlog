package com.example.yblog.kakaoLogin.service;

import com.example.yblog.kakaoLogin.dto.KakaoProfile;
import com.example.yblog.kakaoLogin.dto.OAuthToken;
import org.springframework.http.ResponseEntity;

public interface KaKaoLoginService {
    // 로그인 요청전송 요청후 코드를 받아 낸다
    // 인증을 위해 body데이터를 담아 전송한다 요청후 response데이터를 받아내는 메소드
    public ResponseEntity<String> responeKEntity(String code, String redirect_uri) ;

    // 요청받아낸 body데이터 값을 자바 오브젝트로 담아내는 메소드
    public OAuthToken ObjectOauth(ResponseEntity<String> body);

    // 인증 코드를 받았으니 그걸 이용해서 정보 요청
    // 그결과값을 body데이터 -> json -> 오브젝트로 변환, 그리고 반환
    public ResponseEntity<String> responseUserInfo(OAuthToken outhToken);

    // KaKaoProfile에 담아내는 과정
    public KakaoProfile kakaoProfile(ResponseEntity<String> response);

    // 전부다 위의 메소드를 전부 통합해서 결과만 배출
    public KakaoProfile intergration(String code, String redirect_uri);
}

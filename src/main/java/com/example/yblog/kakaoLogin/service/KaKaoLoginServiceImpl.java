package com.example.yblog.kakaoLogin.service;

import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.kakaoLogin.dto.KakaoProfile;
import com.example.yblog.kakaoLogin.dto.OAuthToken;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class KaKaoLoginServiceImpl implements KaKaoLoginService{


    @Override
    public ResponseEntity<String> responeKEntity(String code, String redirect_uri) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", AllStaticElement.ContentType);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", AllStaticElement.grant_type);
        params.add("client_id", AllStaticElement.client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

        ResponseEntity<String> response = rt.exchange(AllStaticElement.OauthToken_request,
                HttpMethod.POST, kakaoTokenRequest,String.class);

        return response;
    }

    @Override
    public OAuthToken ObjectOauth(ResponseEntity<String> body) {
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            // getter,setter가 없거나, 변수이름을 정확하게 기입하지않으면 틀린다
            oAuthToken = objectMapper.readValue(body.getBody(), OAuthToken.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return oAuthToken;
    }

    @Override
    public ResponseEntity<String> responseUserInfo(OAuthToken outhToken) {
        RestTemplate rt2 = new RestTemplate();
        // httpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + outhToken.getAccess_token());
        headers2.add("Content-type", AllStaticElement.ContentType);
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response = rt2.exchange(AllStaticElement.UserInfoSelect,HttpMethod.POST,
                kakaoProfileRequest2,String.class);

        return response;
    }

    @Override
    public KakaoProfile kakaoProfile(ResponseEntity<String> response) {
        ObjectMapper objectMapper2 = new ObjectMapper();
        // 여기다가 json 으로 담아낼 예정
        KakaoProfile kakaoProfile = null;

        try {
            // getter,setter가 없거나, 변수이름을 정확하게 기입하지않으면 틀린다
            kakaoProfile = objectMapper2.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    @Override
    public KakaoProfile intergration(String code, String redirect_uri) {
        // responeKEntity : 인증코드를 받아내어 카카오측에 전송
        // 카카오 측에서 보내준 OAUTHTOKEN을 클래스 오브젝트화 시켜서 받아내는 부분이다
        // Oauth액세스 토큰
        OAuthToken oauthToken = ObjectOauth(responeKEntity(code, redirect_uri));

        // 인증받은 후 oauthResponse란 해당 회원정보를 모두 body데이터에 json형태로 담아낸 값이다
        ResponseEntity<String> oauthResponse = responseUserInfo(oauthToken);

        KakaoProfile kakaoProfile = kakaoProfile(oauthResponse);

        return kakaoProfile;
    }
}

package de.ralph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;

//Before running this live test make sure both authorization server and resource server in this module are running   

public class AuthorizationCodeLiveTest {
    
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationCodeLiveTest.class);
	
	public final static String AUTH_SERVER = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect";
    public final static String RESOURCE_SERVER = "http://localhost:8081/sso-resource-server";
    private final static String REDIRECT_URL = "http://localhost:8082/ui-one/login/oauth2/code/custom";
    private final static String CLIENT_ID = "ssoClient-1";
    private final static String CLIENT_SECRET = "ssoClientSecret-1";

    @Test
    public void givenUser_whenUseFooClient_thenOkForFooResourceOnly() {
    	logger.info("givenUser_whenUseFooClient_thenOkForFooResourceOnly()");
    	
        final String accessToken = obtainAccessTokenWithAuthorizationCode("john@test.com", "123");
        String path = RESOURCE_SERVER + "/api/foos/2";
        logger.info("path: " + path);
        logger.info("accessToken: " + accessToken);
        final Response fooResponse = RestAssured.given()
            .header("Authorization", "Bearer " + accessToken)
            .get(path);
        assertEquals(200, fooResponse.getStatusCode());
        String name = fooResponse.jsonPath().get("name").toString();
        logger.info("name: " + name);
        assertNotNull(name);
    }
    
    @Test
    public void testWithZipcode() {
    	logger.info("testWithZipcode()");
    	
        final String accessToken = obtainAccessTokenWithAuthorizationCode("john@test.com", "123");
        String path = RESOURCE_SERVER + "/api/foos/zipcode/D-37000";
        logger.info("path: " + path);
        logger.info("accessToken: " + accessToken);
        final Response fooResponse = RestAssured.given()
            .header("Authorization", "Bearer " + accessToken)
            .get(path);
        assertEquals(200, fooResponse.getStatusCode());
        String name = fooResponse.jsonPath().get("name").toString();
        logger.info("name: " + name);
        assertEquals("[Ccccc]", name);
    }
    
    @Test
    public void testWithName() {
    	logger.info("testWithName()");
    	
        final String accessToken = obtainAccessTokenWithAuthorizationCode("john@test.com", "123");
        String path = RESOURCE_SERVER + "/api/foos/name/Bbbbbbb";
        logger.info("path: " + path);
        logger.info("accessToken: " + accessToken);
        final Response fooResponse = RestAssured.given()
            .header("Authorization", "Bearer " + accessToken)
            .get(path);
        assertEquals(200, fooResponse.getStatusCode());
        String name = fooResponse.jsonPath().get("name").toString();
        logger.info("name: " + name);
        assertEquals("[Bbbbbbb]", name);
    }
    
    @Test
    public void getThemAll() {
    	logger.info("getThemAll()");
    	final String accessToken = obtainAccessTokenWithAuthorizationCode("john@test.com", "123");
        String path = RESOURCE_SERVER + "/api/foos";
        logger.info("path: " + path);
        final Response fooResponse = RestAssured.given()
            .header("Authorization", "Bearer " + accessToken)
            .get(path);
        assertEquals(200, fooResponse.getStatusCode());
        String res = fooResponse.asString();
        logger.info("res: " + res);
    }

    private String obtainAccessTokenWithAuthorizationCode(String username, String password) {

        String authorizeUrl = AUTH_SERVER + "/auth";
        String tokenUrl = AUTH_SERVER + "/token";

        Map<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("client_id", CLIENT_ID);
        loginParams.put("response_type", "code");
        loginParams.put("redirect_uri", REDIRECT_URL);
        loginParams.put("scope", "read write");

        // user login
        Response response = RestAssured.given()
            .formParams(loginParams)
            .get(authorizeUrl);
        String cookieValue = response.getCookie("AUTH_SESSION_ID");

        String authUrlWithCode = response.htmlPath()
            .getString("'**'.find{node -> node.name()=='form'}*.@action");

        // get code
        Map<String, String> codeParams = new HashMap<String, String>();
        codeParams.put("username", username);
        codeParams.put("password", password);
        response = RestAssured.given()
            .cookie("AUTH_SESSION_ID", cookieValue)
            .formParams(codeParams)
            .post(authUrlWithCode);

        final String location = response.getHeader(HttpHeaders.LOCATION);

        assertEquals(HttpStatus.FOUND.value(), response.getStatusCode());
        final String code = location.split("#|=|&")[3];

        // get access token
        Map<String, String> tokenParams = new HashMap<String, String>();
        tokenParams.put("grant_type", "authorization_code");
        tokenParams.put("client_id", CLIENT_ID);
        tokenParams.put("client_secret", CLIENT_SECRET);
        tokenParams.put("redirect_uri", REDIRECT_URL);
        tokenParams.put("code", code);

        response = RestAssured.given()
            .formParams(tokenParams)
            .post(tokenUrl);

        return response.jsonPath()
            .getString("access_token");
    }
}

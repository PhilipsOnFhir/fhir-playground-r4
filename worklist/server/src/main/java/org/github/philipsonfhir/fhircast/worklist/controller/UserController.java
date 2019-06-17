package org.github.philipsonfhir.fhircast.worklist.controller;

import org.apache.catalina.User;
import org.github.philipsonfhir.fhircast.worklist.controller.domain.UserLogin;
import org.github.philipsonfhir.fhircast.worklist.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserSessionService userSessionService;

    @Autowired
    public UserController( UserSessionService userSessionService ){
        this.userSessionService = userSessionService;
    }


//    private AuthenticationEntryPoint authenticationEntryPoint() {
//        return new AuthenticationEntryPoint() {
//            // You can use a lambda here
//            @Override
//            public void commence(HttpServletRequest aRequest, HttpServletResponse aResponse,
//                                 AuthenticationException aAuthException) throws IOException, ServletException {
//                String redirectUri = "http://localhost:9010/user";
//                String clientId    = "worklist";
//                String oauthServer = "http://localhost:9100";
////                String oauthServer = "http://localhost:8081/test.html";
//
//                String url = oauthServer + "/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri+"&launch=1234&scope=read&state=5678";
////                aResponse.sendRedirect("http://localhost:9100" + "/oauth/authorize?redirect_uri=localhost:9010/user");
//                aResponse.sendRedirect(url);
////                ?response_type=code&client_id=fhircasthub&redirect_uri="+redirectUri+"&launch=xyz123&scope=read%20write&state="+state+"&aud=https://ehr/fhir");
//            }
//        };
//    }
    @GetMapping( "/launch")
    public ModelAndView launch( ModelMap modelMap ) {
        modelMap.addAttribute("attribute", "redirectWithRedirectPrefix");
        String redirectUri = "http://localhost:9010/token";
        String clientId    = "worklist";
        String oauthServer = "http://localhost:9100";

        String url = oauthServer + "/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri+"&launch=1234&scope=read&state=5678";

        return new ModelAndView( "redirect:"+url, modelMap );
    }

    @GetMapping( "/token")
    public String token( @RequestParam Map<String, String> queryParams ) {

        String code = queryParams.get("code");
        String clientId    = "worklist";
        String redirectUri = "http://localhost:9010/token";

        System.out.println("Get token");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
        httpHeaders.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString("worklist:worklist-secret".getBytes()));

        MultiValueMap<String,String> map =new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("code",code );
        map.add("redirect_uri", redirectUri );

        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);


        String tokenUrl="http://localhost:"+9100+"/oauth/token";

        ResponseEntity<String> response =
                restTemplate.exchange(tokenUrl,
                        HttpMethod.POST,
                        entity,
                        String.class);

//        assertEquals( HttpStatus.OK, response.getStatusCode() );
        return  code;
    }

    @GetMapping( "/user")
//    @PreAuthorize("#oauth2.hasAnyScope('read')")
    public String demo(Principal principal) {
        return "Hello "+principal.getName()+", Auth 2.0 Resource Server, Access Granted by authentication server..";
    }

//    @GetMapping( "/user")
//    public String readLogin(){
//        return "current user";
//    }

    @GetMapping( "/users/{userId}")
    public ResponseEntity<UserLogin> getLogin(@PathVariable String userId){
        ResponseEntity<UserLogin> responseEntity;
        UserLogin userLogin = userSessionService.getUserLogin( userId );
        if ( userLogin==null ){
            responseEntity = new ResponseEntity<UserLogin>( HttpStatus.NOT_FOUND );
        }
        else {
            responseEntity = new ResponseEntity<UserLogin>( userLogin, HttpStatus.OK );

        }
        return responseEntity;
    }
}

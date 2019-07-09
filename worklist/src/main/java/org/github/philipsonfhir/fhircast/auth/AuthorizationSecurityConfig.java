package org.github.philipsonfhir.fhircast.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zakaria on 16/07/17.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationSecurityConfig extends AuthorizationServerConfigurerAdapter {

//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(new InMemoryTokenStore())
                .tokenEnhancer( new CustomTokenEnhancer())
                ;
    }



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .inMemory()
                .withClient("my-trusted-client")
                    .authorizedGrantTypes("password",
                            "refresh_token", "implicit", "client_credentials", "authorization_code")
                    .authorities("CLIENT")
                    .scopes("read", "write", "trust")
                    .accessTokenValiditySeconds(3600)
                    .redirectUris("http://localhost:8081/test.html")
//                    .resourceIds("resource")
                    .secret("mysecret")
            .and()
                .withClient("fhircasthub")
                        .authorizedGrantTypes("password",
                                "refresh_token", "implicit", "client_credentials", "authorization_code")
                        .authorities("CLIENT")
                        .scopes("read", "write", "trust")
                        .accessTokenValiditySeconds(3600)
                        .redirectUris("http://localhost:8080/test")
                        .secret("{noop}fhircast-secret")
            .and()
                .withClient("worklist")
                .authorizedGrantTypes("password",
                        "refresh_token", "implicit", "client_credentials", "authorization_code")
                .authorities("EMR")
                .scopes("read","topic","user/*.*")
                .accessTokenValiditySeconds(3600)
                .redirectUris("http://localhost:8080/test","http://localhost:4200")
                .secret("{noop}worklist-secret");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()");
    }

    private class CustomTokenEnhancer implements TokenEnhancer {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
            System.out.println("enhance");
            Map<String, Object> additionalInfo = new HashMap<>();
            additionalInfo.put("patient","patientId");
            additionalInfo.put("encounter","encounterId");
            additionalInfo.put("need_patient_banner","false");

            OAuth2AccessToken newToken = new MyOAuth2AccessToken(oAuth2AccessToken,additionalInfo);

            return newToken;
        }

        private class MyOAuth2AccessToken implements OAuth2AccessToken {
            private final Map<String, Object> additionalInfo;
            private final OAuth2AccessToken token;

            public MyOAuth2AccessToken(OAuth2AccessToken oAuth2AccessToken, Map<String, Object> additionalInfo) {
                this.additionalInfo = additionalInfo;
                this.token = oAuth2AccessToken;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return additionalInfo;
            }

            @Override
            public Set<String> getScope() {
                return token.getScope();
            }

            @Override
            public OAuth2RefreshToken getRefreshToken() {
                return token.getRefreshToken();
            }

            @Override
            public String getTokenType() {
                return token.getTokenType();
            }

            @Override
            public boolean isExpired() {
                return token.isExpired();
            }

            @Override
            public Date getExpiration() {
                return token.getExpiration();
            }

            @Override
            public int getExpiresIn() {
                return token.getExpiresIn();
            }

            @Override
            public String getValue() {
                return token.getValue();
            }
        }
    }
}
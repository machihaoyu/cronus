package com.fjs.cronus.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


/**
 * Created by bianxj on 2017/4/28.
 *
 */
@OAuth2ContextConfiguration
public class FeignClientConfig {


    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client.access-token-uri}")
    private String tokenUrl;
    @Value("${security.oauth2.client.user-authorization-uri}")
    private String authorizeUrl;


    @Bean
    protected OAuth2ProtectedResourceDetails resource() {
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setId(clientId);
        resource.setAccessTokenUri(tokenUrl);
        resource.setUserAuthorizationUri(authorizeUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setGrantType("password");
        resource.setScope(Arrays.asList("app"));
        resource.setTokenName("access_token");
        resource.setAuthenticationScheme(AuthenticationScheme.query);
        resource.setClientAuthenticationScheme(AuthenticationScheme.form);
        resource.setUseCurrentUri(false);
        return resource;
    }

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Map<String, Collection<String>> headers = requestTemplate.headers();

                //TODO 要删除
                String localToken = "";//"6f0f93fb-2531-486d-bf95-018a7234a337";
                if (headers.containsKey("Authorization")) {
                    Collection<String> requestToken = headers.get("Authorization");
                    Object[] star = (Object[])requestToken.toArray();
                    localToken = star[0].toString();
                }
                if (StringUtils.isNotEmpty(localToken)) {
                    requestTemplate.header("Authorization",
                            "bearer " + localToken);
                }
            }
        };
    }

    /*@Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext context) {
        return new OAuth2FeignRequestInterceptor(context, resource());
    }*/

}

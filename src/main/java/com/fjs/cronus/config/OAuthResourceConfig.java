package com.fjs.cronus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * Created by bianxj on 2017/4/27.
 */
@Configuration
public class OAuthResourceConfig extends ResourceServerConfigurerAdapter {
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.resource.id}")
    private String resourceId;
    @Value("${security.oauth2.resource.token-info-uri}")
    private String checkTokenUri;

    @Bean
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId(clientId);
        remoteTokenServices.setClientSecret(clientSecret);
        remoteTokenServices.setCheckTokenEndpointUrl(checkTokenUri);
        return remoteTokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/uc/api/**", "/saas/api/**", "/php/api/**").authenticated();
        //http.addFilterBefore( new PreAuthFilter(), BasicAuthenticationFilter )
       /* http
                .authorizeRequests()
                .antMatchers("/user/blueprints").access("#oauth2.hasScope('cloudbreak.blueprints')")
                .antMatchers("/user/templates").access("#oauth2.hasScope('cloudbreak.templates')");*/
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId);
        resources.tokenServices(remoteTokenServices());
    }
}
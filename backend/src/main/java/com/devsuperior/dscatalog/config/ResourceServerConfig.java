package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;

	private static final String[] PUBLIC = { "/oauth/token" };

	private static final String[] OPERATOR_OR_ADMIN = { "/api/v1/products/**", "/api/v1/categories/**" };

	private static final String[] ADMIN = { "/api/v1/users/**" };

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(this.tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(PUBLIC).permitAll().antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN)
				.permitAll().antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN").antMatchers(ADMIN).hasRole("ADMIN").anyRequest().authenticated();
	}

}

package com.commvault.cloudplatform.config;

import com.commvault.cloudplatform.security.CVAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
/*
 * Spring Security默认是禁用注解的，要想开启注解(如 @PreAuthorize等注解)
 * 需要继承WebSecurityConfigurerAdapter,并在类上加@EnableGlobalMethodSecurity注解，并设置prePostEnabled 为true
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CVAuthenticationProvider authProvider;

    /*
     * 进行HTTP配置拦截，过滤url，这里主要配置服务端的请求拦截
     * permitAll表示该请求任何人都可以访问，authenticated()表示其他的请求都必须要有权限认证
     * 纯认证服务器实现中，以下配置其实用不到，也可以省略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.formLogin()
                .loginPage("/")
                .successHandler(new AjaxAuthSuccessHandler())
                .failureHandler(new AjaxAuthFailHandler())
                .loginProcessingUrl("/login")
            .and()
                .logout()
                .logoutSuccessHandler(new AjaxLogoutSuccessHandler())
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
            .and()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
            .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/js/**", "/fonts/**", "/file/**", "/css/**", "/favicon.ico", "/index.html", "/content.html").permitAll()
                .anyRequest().authenticated();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    //设置是否保留用户密码在上下文中
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(true);
    }

    //定义登陆成功返回信息
    private class AjaxAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            //User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("[" + SecurityContextHolder.getContext().getAuthentication().getPrincipal() +"]登陆成功！");

            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"message\":\"登录成功\"}");
            out.flush();
            out.close();
        }
    }

    //定义登陆失败返回信息
    private class AjaxAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = response.getWriter();
            out.write("{\"message\":\"" + exception.getMessage() + "\"}");
            out.flush();
            out.close();
        }
    }

    //定义异常返回信息
    public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpStatus.UNAUTHORIZED.value(),authException.getMessage());
        }

    }

    //定义登出成功返回信息
    private class AjaxLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"message\":\"登出成功\"}");
            out.flush();
            out.close();
        }
    }
}

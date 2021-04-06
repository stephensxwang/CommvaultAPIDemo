package com.commvault.cloudplatform.security;

import com.commvault.client.service.CvCommonService;
import com.commvault.client.service.impl.CvCommonServiceImpl;
import com.commvault.client.util.CvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CVAuthenticationProvider implements AuthenticationProvider {

    private final static Logger logger = LoggerFactory.getLogger(CVAuthenticationProvider.class);
    private final static CvCommonService cvCommonService = new CvCommonServiceImpl();
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(authentication instanceof PreAuthenticatedAuthenticationToken){
            password = (String) ((UsernamePasswordAuthenticationToken) authentication.getPrincipal()).getCredentials();
        }

        String cvToken = null;
        try {
            cvToken = getCVToken(name, password);
            if (StringUtils.hasText(cvToken)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(cvToken));
                // use the credentials
                // and authenticate against the third-party system
                return new UsernamePasswordAuthenticationToken(name, password, authorities);
            } else {
                throw new BadCredentialsException("User Name or Password is incorrect");
                //如果AuthenticationProvider返回了null，AuthenticationManager会交给下一个支持authentication类型的AuthenticationProvider处理
                //return null;
            }
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    //support方法检查authentication的类型是不是这个AuthenticationProvider支持的，这里我简单地返回true，就是所有都支持
    @Override
    public boolean supports(Class<?> authentication) {
        //return true;
        //只支持UsernamePasswordAuthenticationToken 和 PreAuthenticatedAuthenticationToken 分别在 createAccessToken 和 refreshAccessToken 时调用
        return authentication.equals(UsernamePasswordAuthenticationToken.class) || authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }

    private String getCVToken(String username, String password) throws Exception {

        if(StringUtils.hasText(username) && StringUtils.hasText(password)){
            return CvUtils.doLogin(cvCommonService, username, password);
        }

        return null;
    }
}

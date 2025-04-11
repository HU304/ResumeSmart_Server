package com.rs.service.impl;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.rs.constants.CacheConstants;
import com.rs.domain.dto.LoginDto;
import com.rs.domain.entity.LoginUser;
import com.rs.domain.vo.UserInfoVo;
import com.rs.domain.vo.UserLoginVo;
import com.rs.response.Result;
import com.rs.service.LoginService;
import com.rs.uitils.BeanCopyUtils;
import com.rs.uitils.JwtUtil;
import com.rs.uitils.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {


    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;


    @Override
    public Result login(LoginDto user) {
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate =
                authenticationManager.authenticate(authenticationToken);

        if (ObjectUtils.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getSysUser().getUserId().toString();

        //jwt生成token
        String token = JwtUtil.createJWT(userId);

        //用户信息存入redis
        redisCache.setCacheObject(CacheConstants.BLOG_LOGIN_USER_KEY + userId, loginUser);

        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getSysUser(), UserInfoVo.class);
        //转换为vo对象返回
        return Result.success(new UserLoginVo(token, userInfoVo));
    }
}

package com.rs.constants;

public interface CacheConstants {
    // 认证后的用户key前缀
    public static final String LOGIN_USER_KEY = "login_key:";
    public static final String TOKEN_SET_KEY = "allTokens";
    /**
     * blog 令牌前缀
     */
    public static final String BLOG_LOGIN_USER_KEY = "blog_login_user_key:";


    //用户新增默认密码
    public static final String LOGIN_USER_PASSWORD = "$2a$10$pRwnzWnbVnhAhR8DBzvfrujFzguinBp13siEApFgasTbvqJxi3nrq";
    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    public static final String BLOG_ARTICLE_CATEGORY = "articleCategory"; //博客文章分类
    public static final String BLOG_ARTICLE_DETAILS = "blog_article_details:"; //博客文章详情
    public static final Integer BLOG_ARTICLE_TIMEOUT = 10; ///博客缓存时间

}

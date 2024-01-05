package com.utils.common.encryption.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("jwt.config")
@Component
@Data
public class JwtUtil {
    //盐
    private String key;
    //过期时间(在配置文件中配置的)
    private long access;
    private long refresh;

    public  enum JwtEnun {
        access, refresh
    }


    /**
     * 生成JWT
     *
     * @param id       用户id
     * @param username 用户名
     * @param password 用户密码
     * @param claims   自定义key-value 信息
     * @param jwtEnun    access 或者refresh
     * @return
     */
    public String createJWT(String id, String username, String password, Map<String, Object> claims, JwtEnun jwtEnun) throws Exception {
        //signWith(SignatureAlgorithm.HS256, key) 设置加密方式，和盐 。盐自己规定
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .claim("pass", password)
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key);
        if (id != null) {
            builder.setId(id);
        }

        if (claims != null) {
            builder.setClaims(claims);
        }

        //设置过期时间
        switch (jwtEnun) {
            case access:
                builder.setExpiration(new Date(nowMillis + access));
                break;
            case refresh:
                builder.setExpiration(new Date(nowMillis + refresh));
                break;
            default:
                throw new Exception("必须选择access或者refresh");
        }

        return builder.compact();
    }

    /**
     * 生成JWT
     *
     * @param username 用户名
     * @param password 用户密码
     * @return
     */
    public String createJWT(String username, String password, JwtEnun jwtEnun) throws Exception {
        return createJWT(null, username, password, null,jwtEnun);
    }


    /**
     * 解析JWT    如果验证token错误 那么将抛出SignatureException异常  , 如果Token过期那么会抛出 ExpiredJwtException 异常  ,我们可以捕获信息然后抛给前端
     *
     * @param jwtStr
     * @return
     */
    public Claims parseJWT(String jwtStr) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }


    //parseJWT.getSubject()+parseJWT.get("pass")


    //从token中获得账户和密码
    public Map<String, String> getUserNameAndPassFromToken(String token) {
        Map<String, String> map = new HashMap<>();
        final Claims claims = parseJWT(token);
        map.put("user", claims.getSubject());
        map.put("pass", String.valueOf(claims.get("pass")));
        return map;
    }


    //从token中获得过期时间
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = parseJWT(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        return parseJWT(token).getSubject();
    }

    /**
     * 从token中获取用户id
     *
     * @param token
     * @return
     */
    public String getUserIdFromToken(String token) {
        return parseJWT(token).getId();
    }


}

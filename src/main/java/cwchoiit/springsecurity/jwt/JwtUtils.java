package cwchoiit.springsecurity.jwt;

import cwchoiit.springsecurity.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.util.Date;

public abstract class JwtUtils {

    public static final int EXPIRATION_TIME = 600000; // 10분
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";

    private JwtUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Pair<String, Key> key = JwtKey.getRandomKey();
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                .signWith(key.getSecond())
                .compact();
    }
}

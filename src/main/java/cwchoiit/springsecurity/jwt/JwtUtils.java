package cwchoiit.springsecurity.jwt;

import cwchoiit.springsecurity.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.util.Date;

import static cwchoiit.springsecurity.jwt.JwtProperties.EXPIRATION_TIME;

public abstract class JwtUtils {

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
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME)) // JwtProperties 파일에 있는 상수값 (gitignore)
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                .signWith(key.getSecond())
                .compact();
    }
}

package cwchoiit.springsecurity.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.security.Key;

public class SigningKeyResolver extends SigningKeyResolverAdapter {

    private SigningKeyResolver() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static SigningKeyResolver instance = new SigningKeyResolver();

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        String kid = header.getKeyId();
        if (kid == null) {
            return null;
        }
        return JwtKey.getKey(kid);
    }
}

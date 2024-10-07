package cwchoiit.springsecurity.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

public abstract class JwtKey {

    /**
     * Key-Rolling 을 위한 Key set, 외부로 절대 유출되어서는 안된다.
     * 같은 패키지 내 JwtProperties 인터페이스를 만들고 gitignore 설정한다.
     */
    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", JwtProperties.SECRET_KEY_1,
            "key2", JwtProperties.SECRET_KEY_2,
            "key3", JwtProperties.SECRET_KEY_3
    );
    /**
     * KID_SET = ["key1", "key2", "key3"] 이 된다.
     * toArray()는 타입을 명시하지 않으면, Object[]로 반환한다.
     * toArray(new String[0])은 원하는 타입을 명시하기 위해 new String[0]으로 넣은것이고 크기를 0으로 할당해도 상관없이
     * 자바가 알아서 내부적으로 적절한 크기의 배열(여기서는 3)을 자동으로 할당하여 반환하기 때문에 성능이나 메모리 관점에서 문제가 되지 않는다.
     */
    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);

    private JwtKey() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[new Random().nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if (key == null) {
            return null;
        }
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}

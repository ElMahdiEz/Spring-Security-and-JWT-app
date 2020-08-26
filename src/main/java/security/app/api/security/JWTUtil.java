package security.app.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${auth.expiration}")
    private Long TOKEN_EXPIRATION;
    @Value("${auth.secret}")
    private String TOKEN_SECRET;

    public String generatedToken(UserDetails userDetails) {
        // 1. claims
        // 2. expiration
        // 3. sign
        // 4. compact

        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());

        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + TOKEN_EXPIRATION * 1000);
    }

    private Claims getClaims(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValid(String token, UserDetails userDetails) {
        String username = getUsernameFromJWT(token);

        return (username.equals(userDetails.getUsername()) && !isExpired(token));
    }

    private boolean isExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }
}

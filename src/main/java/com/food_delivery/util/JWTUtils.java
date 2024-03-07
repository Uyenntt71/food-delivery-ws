package com.food_delivery.util;

import static com.food_delivery.constant.CommonParams.AUTHORIZATION_HEADER;
import static com.food_delivery.constant.CommonParams.EMPTY_STRING;
import static com.food_delivery.constant.CommonParams.TOKEN_VALID_TIME;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;

public class JWTUtils {
    private static final String TENANT_CLAIM   = "tenantId";
    private static final String USERNAME_CLAIM = "sub";
    private static final String CUSTOMER_CLAIM = "customerId";
    private static final String SCOPES         = "scopes";
    private static final String ENABLED        = "enabled";
    private static final String CERTIFICATE    = "certificate";

    public static String claimAccount(String token) {
        try {
            return JWT.decode(token).getClaim(USERNAME_CLAIM).asString();
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    public static String claimCustomerId(String token) {
        try {
            return JWT.decode(token).getClaim(CUSTOMER_CLAIM).asString();
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    public static String claimAccountWithRequest(HttpServletRequest request) {
        String Authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (Authorization == null) {
            return EMPTY_STRING;
        }
        String token = request.getHeader(AUTHORIZATION_HEADER).replace("Bearer ", "");
        return claimAccount(token);
    }

    public static String claimToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER).replace("Bearer ", "");
    }

    public static String claimCustomerIdWithRequest(HttpServletRequest request) {
        String xAuthorization = request.getHeader(AUTHORIZATION_HEADER);
        if (xAuthorization == null) {return EMPTY_STRING;}
        String token = request.getHeader(AUTHORIZATION_HEADER).replace("Bearer ", "");
        return claimCustomerId(token);
    }

    public static long getTokenExp(String token) {
        return JWT.decode(token).getClaim("exp").asLong();
    }

    public static String rebuildToken(String token, String signingKey, String certificate) throws CommonException {
        Jws<Claims> jwsClaims = parseClaims(token, signingKey);
        Claims claims = jwsClaims.getBody();
        claims.put(CERTIFICATE, certificate);

        return Jwts.builder()
                   .setClaims(claims)
                   .signWith(SignatureAlgorithm.HS512, signingKey)
                   .compact();
    }

    private static Jws<Claims> parseClaims(String token, String signingKey) throws CommonException {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            throw new CommonException("Invalid token", ErrorCode.AUTHENTICATION);
        } catch (ExpiredJwtException expiredEx) {
            throw new CommonException("Token expired", ErrorCode.AUTHENTICATION);
        }
    }

    public static String buildToken(String username, String customerId,  String signingKey) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_VALID_TIME);
        Claims claims = new DefaultClaims();
        claims.setExpiration(expiryDate);
        claims.setSubject(username);
        claims.setIssuedAt(now);
        claims.put(CUSTOMER_CLAIM, customerId);

        return Jwts.builder()
                   .setClaims(claims)
                   .signWith(SignatureAlgorithm.HS512, signingKey)
                   .compact();
    }


    public static void verifyToken(String token, String signingKey) throws CommonException {
        Jws<Claims> jwsClaims = parseClaims(token, signingKey);
        Claims claims = jwsClaims.getBody();
        String subject = claims.getSubject();
        List<String> scopes = claims.get(SCOPES, List.class);
    }
}

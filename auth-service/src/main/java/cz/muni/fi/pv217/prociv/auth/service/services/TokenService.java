package cz.muni.fi.pv217.prociv.auth.service.services;

import io.smallrye.jwt.build.Jwt;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class TokenService {
    public String getToken(String username, boolean isAdmin) {
        Set<String> groups = new HashSet<>(Arrays.asList("User"));
        if (isAdmin) {
            groups.add("Admin");
        }
        return Jwt.issuer("https://example.com/issuer")
                .upn(username + "@quarkus.io")
                .groups(groups)
                .sign();
    }
}
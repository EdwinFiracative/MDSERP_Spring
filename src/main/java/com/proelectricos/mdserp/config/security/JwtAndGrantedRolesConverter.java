package com.proelectricos.mdserp.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtAndGrantedRolesConverter implements
        Converter<Jwt, Collection<GrantedAuthority>> {

    public static final String ROLES_TAG = "roles";
    private static final String AUTHORITY_PREFIX = "ROLE_";
    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";


    @Override
    public Collection<GrantedAuthority> convert(final @NonNull Jwt jwt) {
        Collection<String> roles = new HashSet<>(extractRolesFromRealmAccess(jwt));
        roles.addAll(extractRolesFromResourceAccess(jwt));

        return roles.stream()
                .map(role -> AUTHORITY_PREFIX + role.toUpperCase()) // Convert to uppercase for consistency
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(HashSet::new));
    }


    @SuppressWarnings("unchecked")
    private Collection<String> extractRolesFromRealmAccess(final Jwt jwt) {
        Collection<String> mappedAuthorities = new ArrayList<>();

        if (jwt.hasClaim(REALM_ACCESS)) {
            Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS);
            if (realmAccess.containsKey(ROLES_TAG)) {
                mappedAuthorities.addAll((Collection<String>) realmAccess.get(ROLES_TAG));
            }
        }

        return mappedAuthorities;
    }


    @SuppressWarnings("unchecked")
    private Collection<String> extractRolesFromResourceAccess(final Jwt jwt) {
        Collection<String> mappedAuthorities = new ArrayList<>();

        if (jwt.hasClaim(RESOURCE_ACCESS)) {
            Map<String, Object> resourceAccess = jwt.getClaimAsMap(RESOURCE_ACCESS);

            resourceAccess.values().forEach(resource -> {
                Map<String, Object> resourceMap = (Map<String, Object>) resource;
                if (resourceMap.containsKey(ROLES_TAG)) {
                    mappedAuthorities.addAll((Collection<String>) resourceMap.get(ROLES_TAG));
                }
            });
        }

        return mappedAuthorities;
    }


}
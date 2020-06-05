package lt.Renaldas.tasks.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum ApplicationUserRole {

    ADMIN(Sets.newHashSet(ApplicationUserPermission.READ, ApplicationUserPermission.WRITE)),
    ADMINTRAINEE(Sets.newHashSet(ApplicationUserPermission.READ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
	this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
	return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
	Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
		.map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
	permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
	return permissions;
    }
}

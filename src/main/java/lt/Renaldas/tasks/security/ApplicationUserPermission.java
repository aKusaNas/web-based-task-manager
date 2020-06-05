package lt.Renaldas.tasks.security;

public enum ApplicationUserPermission {
    READ("admin:read"), 
    WRITE("admin:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
	this.permission = permission;
    }

    public String getPermission() {
	return permission;
    }
}

package kusitms.gallae.global;

public enum Role {
    MANAGER("MANAGER"),
    USER("USER"),
    ADMIN("ADMIN");

    public static final String AUTHORITY_PREFIX = "ROLE_";

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toAuthority() {
        return AUTHORITY_PREFIX + this.value;
    }
}

package entity.records;

public record PasswordRecord(
        String hash,
        String salt
) {
}

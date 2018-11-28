package uk.gov.digital.ho.hocs.info.security;
import java.util.Arrays;
import lombok.Getter;


@Getter
public enum AccessLevel {
  SUMMARY(1), READ(2), WRITE(3), OWNER(5);
    private int level;

    AccessLevel(int level) {
        this.level = level;
    }

    public static AccessLevel from(int value) {
        return Arrays.stream(values())
                .filter(level -> level.level == value)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}

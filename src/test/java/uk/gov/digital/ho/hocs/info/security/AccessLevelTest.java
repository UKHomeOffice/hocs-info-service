package uk.gov.digital.ho.hocs.info.security;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static uk.gov.digital.ho.hocs.info.security.AccessLevel.*;

public class AccessLevelTest {

    @Test
    public void getDisplayValue() {
        assertThat(UNSET.getLevel()).isEqualTo(0);
        assertThat(SUMMARY.getLevel()).isEqualTo(1);
        assertThat(READ.getLevel()).isEqualTo(2);
        assertThat(RESTRICTED_READ.getLevel()).isEqualTo(3);
        assertThat(WRITE.getLevel()).isEqualTo(4);
        assertThat(OWNER.getLevel()).isEqualTo(5);
        assertThat(CASE_ADMIN.getLevel()).isEqualTo(6);

    }

    @Test
    public void shouldNotAccidentallyChangeTheOrder() {
        assertOrderValue(UNSET, 0);
        assertOrderValue(SUMMARY, 1);
        assertOrderValue(READ, 2);
        assertOrderValue(RESTRICTED_READ, 3);
        assertOrderValue(WRITE, 4);
        assertOrderValue(OWNER, 5);
        assertOrderValue(CASE_ADMIN, 6);
    }

    @Test
    public void shouldNotAccidentallyAddValues() {
        for (AccessLevel accessLevel : AccessLevel.values()) {
            switch (accessLevel) {
                case UNSET:
                case SUMMARY:
                case READ:
                case RESTRICTED_READ:
                case WRITE:
                case OWNER:
                case CASE_ADMIN:
                    break;
                default:
                    fail("You've added a AccessLevel, make sure you've written all the tests!");
            }
        }
    }

    private void assertOrderValue(AccessLevel accessLevel, int value) {
        assertThat(accessLevel.ordinal()).isEqualTo(value);
    }

}

package tobyspring.splearn.adapter.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurePasswordEncoderTest {

    @Test
    void securePasswordEncoder() {
        // given
        SecurePasswordEncoder securePasswordEncoder = new SecurePasswordEncoder();

        // when
        String passwordHash = securePasswordEncoder.encode("password");

        // then
        assertThat(securePasswordEncoder.matches("password", passwordHash)).isTrue();
        assertThat(securePasswordEncoder.matches("secret", passwordHash)).isFalse();
    }
}

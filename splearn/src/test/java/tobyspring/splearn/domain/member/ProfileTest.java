package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    void profile() {
        new Profile("tobyspring");
        new Profile("toby123");
        new Profile("12345");
        new Profile("");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("xxxxxooxxoxoxoxoxoxox"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("A"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("프로필"))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void url() {
        var profile = new Profile("tobyspring");

        assertThat(profile.url()).isEqualTo("@tobyspring");
    }
}

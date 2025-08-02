package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    void createMember() {
        // given
        var member = new Member("email", "nickname", "password");

        // when & then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void constructorNullCheck() {
        // when & then
        assertThatThrownBy(() -> new Member(null, "nickname", "password"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        // given
        var member = new Member("email", "nickname", "password");

        // when
        member.activate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        // given
        var member = new Member("email", "nickname", "password");

        // when
        member.activate();

        // then
        assertThatThrownBy(member::activate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        // given
        var member = new Member("email", "nickname", "password");
        member.activate();

        // when
        member.deactivate();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        // given
        var member = new Member("email", "nickname", "password");

        // when & then
        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
    }
}

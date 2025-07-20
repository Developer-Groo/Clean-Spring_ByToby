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

}

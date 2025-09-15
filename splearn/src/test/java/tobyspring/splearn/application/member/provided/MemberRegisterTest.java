package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        // when
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        // then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        // given
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        // when & then
        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate() {
        // given
        Member member = registerMember();

        // when
        member = memberRegister.activate(member.getId());
        entityManager.flush();

        // then
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Toby123", "toby100", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
    }

    @Test
    void updateInfoFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Toby123", "toby100", "자기소개"));

        Member member2 = registerMember("splearn2@gmail.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2 는 기존의 member 와 같은 프로필 주소를 사용할 수 없다
        assertThatThrownBy(() ->
                memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "Introduction"))
        ).isInstanceOf(DuplicateProfileException.class);

        // 다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("Peter", "toby101", "Introduction"));

        // 기존 프로필 주소 변경 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "Introduction"));

        // 프로필 주소 제거 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "", "Introduction"));

        // 프로필 주소 중복은 허용하지 않음
        assertThatThrownBy(() ->
                memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby101", "Introduction"))
        ).isInstanceOf(DuplicateProfileException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        // when & then
        checkValidation(new MemberRegisterRequest("splearn@gmail.com", "name", "password"));
        checkValidation(new MemberRegisterRequest("splearn@gmail.com", "nicknames__________________________", "password"));
        checkValidation(new MemberRegisterRequest("splearngmail.com", "nickname", "password"));
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}

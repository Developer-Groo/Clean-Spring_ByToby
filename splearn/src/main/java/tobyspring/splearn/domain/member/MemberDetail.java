package tobyspring.splearn.domain.member;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    void activate() {
        isTrue(activatedAt == null, "이미 activatedAt 은 설정되었습니다.");
        this.activatedAt = LocalDateTime.now();
    }

    void deactivate() {
        isTrue(deactivatedAt == null, "이미 deactivatedAt 은 설정되었습니다.");
        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = requireNonNull(updateRequest.introduction());
    }
}

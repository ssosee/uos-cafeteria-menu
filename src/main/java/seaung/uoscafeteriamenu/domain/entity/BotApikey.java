package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BotApikey extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String botApikey;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_use_member_id")
    private ApiUseMember apiUseMember;

    public static BotApikey create(String key, ApiUseMember apiUseMember) {
        BotApikey botApikey = new BotApikey();
        botApikey.botApikey = key;
        botApikey.apiUseMember = apiUseMember;
        apiUseMember.mappingApiKey(botApikey);

        return botApikey;
    }
}

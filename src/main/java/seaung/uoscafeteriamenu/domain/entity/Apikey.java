package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apikey extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String botApikey;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_use_member_id")
    private ApiUseMember apiUseMember;

    public static Apikey create(String key, ApiUseMember apiUseMember) {
        Apikey apikey = new Apikey();
        apikey.botApikey = key;
        apikey.apiUseMember = apiUseMember;
        apiUseMember.mappingApiKey(apikey);

        return apikey;
    }
}

package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiUseMember extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @OneToOne(mappedBy = "apiUseMember")
    private BotApikey apikey;

    public static ApiUseMember create(String name, String email) {
        ApiUseMember apiUseMember = new ApiUseMember();
        apiUseMember.name = name;
        apiUseMember.email = email;

        return apiUseMember;
    }

    public void mappingApiKey(BotApikey apikey) {
        this.apikey = apikey;
    }
}

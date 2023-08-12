package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.repository.ActionRepository;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;
import seaung.uoscafeteriamenu.domain.repository.UtteranceRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UtteranceService {

    private final MemberRepository memberRepository;
    private final ActionRepository actionRepository;
    private final UtteranceRepository utteranceRepository;

    // 사용자의 발화 정보를 저장한다.
    public void saveMemberUtterance() {

    }
}

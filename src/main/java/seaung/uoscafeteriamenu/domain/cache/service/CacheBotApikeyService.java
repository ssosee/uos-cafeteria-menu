package seaung.uoscafeteriamenu.domain.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheBotApikey;
import seaung.uoscafeteriamenu.domain.entity.BotApikey;
import seaung.uoscafeteriamenu.domain.repository.ApikeyRepository;
import seaung.uoscafeteriamenu.web.exception.ApikeyException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheBotApikeyService {

    private final ApikeyRepository apiKeyRepository;

    @Cacheable(value = "cacheBotApikey", key = "#botApikeyHeader")
    public CacheBotApikey findBotApiKeyHeader(String botApikeyHeader) {
        BotApikey botApikey = apiKeyRepository.findByBotApikey(botApikeyHeader)
                .orElseThrow(() -> new ApikeyException(ApikeyException.VALID_API_KEY_CODE));

        return new CacheBotApikey(botApikey.getBotApikey());
    }
}

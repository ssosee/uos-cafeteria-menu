package seaung.uoscafeteriamenu.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @PostMapping("/hello")
    public String hello(@RequestBody Object o) {
        log.info("o={}", o.toString());
        return "안녕";
    }
}

/**
 * {
 *   "intent": {
 *     "id": "fn6jdqxdtl3832kpqzpe9mgq",
 *     "name": "블록 이름"
 *   },
 *   "userRequest": {
 *     "timezone": "Asia/Seoul",
 *     "params": {
 *       "ignoreMe": "true"
 *     },
 *     "block": {
 *       "id": "fn6jdqxdtl3832kpqzpe9mgq",
 *       "name": "블록 이름"
 *     },
 *     "utterance": "발화 내용",
 *     "lang": null,
 *     "user": {
 *       "id": "427298",
 *       "type": "accountId",
 *       "properties": {}
 *     }
 *   },
 *   "bot": {
 *     "id": "64ad4e834bc96323949bfb11",
 *     "name": "봇 이름"
 *   },
 *   "action": {
 *     "name": "m6gnizck38",
 *     "clientExtra": null,
 *     "params": {},
 *     "id": "6a171kzftj5feysekd1eqcvi",
 *     "detailParams": {}
 *   }
 * }
 */
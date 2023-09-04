package seaung.uoscafeteriamenu.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <a href="https://imksh.com/113#article-5-2--jdbc-%EB%B2%8C%ED%81%AC-%EC%97%B0%EC%82%B0-%ED%85%8C%EC%8A%A4%ED%8A%B8">참고</a>
 * IDENTITY 전략을 사용 중이고, JPA를 통해 save를 호출하는 경우 Bulk 쿼리가 수행되지 않는다.
 */
@Repository
@RequiredArgsConstructor
public class MemberBulkInsertRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TimeProvider timeProvider;

    public void saveAll(Set<Member> memberSet) {
        String sql = "insert into member(create_at, last_modified_at, bot_user_id, visit_count) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                List<Member> members = new ArrayList<>(memberSet);
                ps.setTimestamp(1, Timestamp.valueOf(timeProvider.getCurrentLocalDateTime()));
                ps.setTimestamp(2, Timestamp.valueOf(timeProvider.getCurrentLocalDateTime()));
                ps.setString(3, members.get(i).getBotUserId());
                ps.setLong(4, members.get(i).getVisitCount());
            }

            @Override
            public int getBatchSize() {
                return memberSet.size();
            }
        });

    }
}

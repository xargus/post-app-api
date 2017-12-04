package center.xargus.postapp.memo.dao;

import center.xargus.postapp.memo.model.MemoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemoDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("memo.sql"));
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }

    public int insert(String userId, String content, String updateDate) {
        String query = "insert into MEMO (user_id, content, updateDate) VALUES(?, ?, ?)";
        return jdbcTemplate.update(query, userId, content, updateDate);
    }

    public int update(int memoId, String content, String updateDate) {
        String query = "update MEMO set content = ?, updateDate = ? where _id = ?";
        return jdbcTemplate.update(query, content, updateDate, memoId);
    }

    public int delete(int memoId) {
        String query = "delete from MEMO where _id = ?";
        return jdbcTemplate.update(query, memoId);
    }

    public List<MemoModel> select(int start, int limit) {
        String query = "select * from MEMO ORDER BY updateDate DESC limit " + start + ", " + limit;
        return jdbcTemplate.query(query, new BeanPropertyRowMapper(MemoModel.class));
    }

    public List<MemoModel> select(String userId, int start, int limit) {
        String query = "select * from MEMO where user_id = '" + userId + "'" +" ORDER BY updateDate DESC limit " + start + ", " + limit;
        return jdbcTemplate.query(query, new BeanPropertyRowMapper(MemoModel.class));
    }

    public String getUserId(int memoId) {
        String query = "select user_id from MEMO where _id = " + memoId;
        return jdbcTemplate.queryForObject(query, String.class);
    }
}

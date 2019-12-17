package weed3test;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheService;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

public class DbUtil {

    private final static HikariDataSource dataSource(){
        Map<String, String> map = new HashMap<>();

        map.put("schema", "rock");
        map.put("url", "jdbc:mysql://localdb:3306/rock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true");
        map.put("driverClassName", "com.mysql.cj.jdbc.Driver");
        map.put("username", "demo");
        map.put("password", "UL0hHlg0Ybq60xyb");

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(map.get("url"));
        dataSource.setUsername(map.get("username"));
        dataSource.setPassword(map.get("password"));
        dataSource.setDriverClassName(map.get("driverClassName"));

        return dataSource;
    }

    public static DbContext getDb() {
        DbContext db = new DbContext("rock", dataSource());

        return db;
    }

    public static DbContext db = getDb();
    public static ICacheServiceEx cache = new LocalCache().nameSet("test");
}
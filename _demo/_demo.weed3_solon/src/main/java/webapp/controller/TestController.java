package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.annotation.XSingleton;
import org.noear.weed.DbContext;
import webapp.dso.DbConfig;

import static org.noear.weed.OrderBy.ASC;
import static org.noear.weed.OrderBy.DESC;

@XMapping("/test")
@XSingleton(true)
@XController
public class TestController {
    DbContext db2 = DbConfig.db2;

    @XMapping("demo1")
    public Object test(String sql) throws Exception {
        //
        // mysql 8.0 才支持
        //
        Object tmp = db2.table("ag").innerJoin("ax").on("ag.agroup_id = ax.agroup_id")
                .limit(10)
                .with("ax", db2.table("appx").selectQ("*"))
                .with("ag", db2.table("appx_agroup").where("agroup_id < 10").selectQ("*"))
                .with("ah", "select * from appx_agroup where agroup_id<?",10)
                .select("ax.*")
                .getMapList();

        if (sql == null) {
            return tmp;
        } else {
            return db2.lastCommand.text;
        }
    }

    @XMapping("demo2")
    public Object test2(String sql) throws Exception {
        //
        // mysql 8.0 才支持
        //
        Object tmp = db2.table("ax")
                .orderBy("app_id", DESC)
                .limit(2)
                .with("ax", db2.table("appx").selectQ("*"))
                .select("ax.*")
                .getMapList();

        if (sql == null) {
            return tmp;
        } else {
            return db2.lastCommand.text;
        }
    }
}

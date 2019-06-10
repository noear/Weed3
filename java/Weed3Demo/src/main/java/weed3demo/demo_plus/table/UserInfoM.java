package weed3demo.demo_plus.table;

import noear.weed.DbTable;
import weed3demo.config.DbConfig;

/**
 * Created by noear on 2017/7/22.
 */
public class UserInfoM extends DbTable {
    public UserInfoM() {
        super(DbConfig.test);

        table("$.user_info u");
        set("userID", () -> userID);
        set("sex", () -> sex);
    }

    public Integer userID;
    public Integer sex;
}

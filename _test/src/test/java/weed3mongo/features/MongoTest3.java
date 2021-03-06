package weed3mongo.features;

import org.junit.Test;
import org.noear.weed.cache.ICacheServiceEx;
import org.noear.weed.cache.LocalCache;
import org.noear.weed.mongo.MgContext;
import weed3mongo.model.UserModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author noear 2021/2/6 created
 */
public class MongoTest3 {
    String url = "mongodb://172.168.0.162:27017";
    MgContext db = new MgContext(url, "demo");

    @Test
    public void test1(){
        List<UserModel> mapList =  db.table("user")
                .whereBtw("id", 10,20)
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() == 10;
        assert mapList.get(0).id == 10;
    }

    @Test
    public void test12(){
        ICacheServiceEx cache = new LocalCache();

        for(int i=0 ; i< 3; i++) {
            List<UserModel> mapList = db.table("user")
                    .whereBtw("id", 10, 20)
                    .orderByAsc("id")
                    .limit(10)
                    .caching(cache)
                    .selectList(UserModel.class);

            assert mapList.size() == 10;
            assert mapList.get(0).id == 10;
        }
    }

    @Test
    public void test2(){
        List<UserModel> mapList =  db.table("user")
                .whereIn("id", Arrays.asList(3,4))
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).id == 3;
    }

    @Test
    public void test22(){
        List<UserModel> mapList =  db.table("user")
                .whereNlk("name", "^no")
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

       System.out.println(mapList);
       assert mapList.size() == 0;
    }

    //@Test
    public void test3(){
        //需要服务器开启脚本能力
        List<UserModel> mapList =  db.table("user")
                .whereScript("this.id==3")
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).id == 3;
    }

    @Test
    public void test4(){
        List<UserModel> mapList =  db.table("user")
                .whereMod("id", 3,1)
                .orderByAsc("id")
                .limit(10)
                .selectList(UserModel.class);

        assert mapList.size() > 2;
        assert mapList.get(0).id == 1;
    }
}

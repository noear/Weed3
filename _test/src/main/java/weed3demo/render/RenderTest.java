package weed3demo.render;

import org.noear.weed.DbContext;
import weed3demo.DbUtil;

public class RenderTest {
    public static void main(String[] args){
        DbContext db = DbUtil.db;

        TestMapper mapper = db.mapper(TestMapper.class);



        System.out.println(mapper.appx_get_ftl(1));

        System.out.println(mapper.appx_get_beetl(1));

        System.out.println(mapper.appx_get_enjoy(1));

        System.out.println(mapper.appx_get_velocity(1));
    }
}

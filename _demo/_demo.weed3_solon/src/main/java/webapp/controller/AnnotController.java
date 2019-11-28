package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.annotation.XSingleton;
import org.noear.solon.core.ModelAndView;
import org.noear.weed.DbContext;
import webapp.dso.DbConfig;
import webapp.dso.SqlAnnotation;


@XMapping("/annot")
@XSingleton(true)
@XController
public class AnnotController {
    DbContext db2 = DbConfig.db2;

    @XMapping("demo0/test")
    public Object test(String sql) throws Exception {
        //
        // mysql 8.0 才支持
        //
         Object tmp = db2.table("ag").innerJoin("ax").on("ag.agroup_id = ax.agroup_id")
                .limit(10)
                .with("ax","select * from appx")
                .with("ag","select * from appx_agroup where agroup_id<10")
                .select("ax.*")
                .getMapList();

         if(sql == null){
             return tmp;
         }else{
             return db2.lastCommand.text;
         }
    }

    @XMapping("demo0/html")
    public ModelAndView demo0() throws Exception {
        ModelAndView mv = new ModelAndView("view.ftl");

        Object _map = demo3();
        mv.put("map", _map);

        return mv;
    }

    @XMapping("demo1/json")
    public Object demo1() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get();
    }

    @XMapping("demo2/json")
    public Object demo2() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get2(48);
    }

    @XMapping("demo3/json")
    public Object demo3() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get3("appx",48);
    }

    @XMapping("demo4/json")
    public Object demo4() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getlist(1);
    }

    @XMapping("demo5/json")
    public Object demo5() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getids();
    }

}

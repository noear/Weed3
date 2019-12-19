package webapp;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.XPlugin;
import org.noear.weed.DbContext;
import org.noear.weed.WeedConfig;
import org.noear.weed.annotation.Db;
import org.noear.weed.ext.Act0;
import org.noear.weed.xml.XmlSqlLoader;
import webapp.model.AppxModel;

import java.lang.annotation.Annotation;

public class App {
    public static void main(String[] args){
        Act0 tmp2 = AppxModel::new;

        XmlSqlLoader.tryLoad();

        //测试
        Aop.factory().beanLoaderAdd(Db.class, (clz, bw, anno) -> {
            if(clz.isInterface()){
                DbContext db = WeedConfig.libOfDb.get(anno.value());
                Object raw = db.mapper(clz);
                Aop.put(clz, raw);
            }
        });

        //测试
        XPlugin plugin = (app)->{
            Aop.factory().beanBuilderadd((clz, annoS)->{
                if(clz.isInterface()) {
                    Db dbAnno = clz.getAnnotation(Db.class);
                    if (dbAnno == null) {
                        if (annoS != null) {
                            for (Annotation a1 : annoS) {
                                if (a1.annotationType() == Db.class) {
                                    dbAnno = (Db) a1;
                                }
                            }
                        }

                        if(dbAnno!=null){
                            DbContext db = WeedConfig.libOfDb.get(dbAnno.value());
                            Object raw = db.mapper(clz);
                            return raw;
                        }
                    }else{
                        DbContext db = WeedConfig.libOfDb.get(dbAnno.value());
                        Object raw = db.mapper(clz);
                        Aop.put(clz, raw);
                        return raw;
                    }
                }
                return null;
            });
        };



        XApp app = XApp.start(App.class,args ,(x)->{
            x.plug(plugin);
        });

        app.get("/",(c)->{
            c.render("nav.htm", null);
        });



        /*
        *  测试用到的表结构
   CREATE TABLE `appx` (
  `app_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `app_key` varchar(40) DEFAULT NULL COMMENT '应用访问KEY',
  `akey` varchar(40) DEFAULT NULL COMMENT '（用于取代app id 形成的唯一key） //一般用于推广注册之类',
  `ugroup_id` int(11) DEFAULT '0' COMMENT '加入的用户组ID',
  `agroup_id` int(11) DEFAULT NULL COMMENT '加入的应用组ID',
  `name` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `note` varchar(50) DEFAULT NULL COMMENT '应用备注',
  `ar_is_setting` int(1) NOT NULL DEFAULT '0' COMMENT '是否开放设置',
  `ar_is_examine` int(1) NOT NULL DEFAULT '0' COMMENT '是否审核中(0: 没审核 ；1：审核中)',
  `ar_examine_ver` int(11) NOT NULL DEFAULT '0' COMMENT '审核 中的版本号',
  `log_fulltime` datetime DEFAULT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `IX_akey` (`akey`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用表';
        *
        * */
    }
}

package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import org.noear.weed.wrap.DbType;
import weed3test.DbUtil;
import weed3test.model.AppxCopy2Model;
import weed3test.model.AppxCopyModel;
import weed3test.util.Datetime;

import java.util.Map;

public class _TimeTest {
    DbContext db = DbUtil.db;

    private AppxCopy2Model _model;
    private AppxCopy2Model model() throws Exception{
        if(_model == null) {
            _model = db.table("appx_copy")
                    .whereEq("app_id", 22)
                    .select("*")
                    .getItem(AppxCopy2Model.class);

            System.out.println(db.lastCommand.text);
        }

        return _model;
    }
    @Test
    public void test0() throws Exception{
        model();
    }

    @Test
    public void test_insert() throws Exception {
        Map<String,Object> map = db.table("appx_copy")
                .whereEq("app_id", 22)
                .select("*")
                .getMap();

        map.put("app_id",1001);

        db.table("appx_copy").setMap(map).upsert("app_id");
        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test_insert2() throws Exception {
        AppxCopy2Model map = db.table("appx_copy")
                .whereEq("app_id", 22)
                .select("*")
                .getItem(AppxCopy2Model.class);

        map.app_id = 1000;

        if(db.dbType() == DbType.Oracle){
            map.time1 = null;
            map.date1 = null;
        }

        db.table("appx_copy")
                .setEntityIf(map, (k, v) -> v != null)
                .upsert("app_id");
        System.out.println(db.lastCommand.text);
    }

    @Test
    public void test_datetime1() throws Exception {
        AppxCopy2Model m = model();
        Datetime log_fulltime = new Datetime(m.log_fulltime);

        assert m.app_id == 22;
        assert log_fulltime.getSeconds() == m.datetime1.getSecond();
    }

    @Test
    public void test_date1() throws Exception {
        AppxCopy2Model m = model();
        Datetime log_fulltime = new Datetime(m.log_fulltime);

        assert m.app_id == 22;
        assert log_fulltime.getMonth() + 1 == m.date1.getMonth().getValue();
    }

    @Test
    public void test_time1() throws Exception {
        AppxCopy2Model m = model();
        Datetime log_fulltime = new Datetime(m.log_fulltime);

        assert m.app_id == 22;
        assert log_fulltime.getSeconds() == m.time1.getSecond();
    }
}

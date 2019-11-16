package webapp.dso;

import java.math.*;
import java.time.*;
import java.util.*;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.xml.Namespace;

import java.lang.Integer;

@Namespace("webapp.dso")
public interface SqlMapper{
    //随便取条数据
    Map<String,Object> appx_get() throws Exception;

    //根据id取条数据
    Map<String,Object> appx_get2(int app_id) throws Exception;

    //取一批ID
    List<Integer> appx_get3() throws Exception;
}

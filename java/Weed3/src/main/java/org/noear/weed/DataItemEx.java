package org.noear.weed;

import org.noear.weed.ext.Act2;
import org.noear.weed.ext.Fun0;

import java.util.*;

/**
 * Created by yuety on 15/9/2.
 */
public class DataItemEx implements IDataItem{
    HashMap<String, Fun0<Object>> _data = new LinkedHashMap<>();
    List<String> _keys = new ArrayList<>();
    boolean _isNotNull = false; //不需要null的数据

    public DataItemEx() { }
    public DataItemEx(boolean isUsingDbNull) { _isUsingDbNull = isUsingDbNull; }

    @Override
    public int count() {
        return _data.size();
    }
    @Override
    public void clear() {
        _data.clear();
        _keys.clear();
    }

    @Override
    public boolean exists(String name) {
        return _data.containsKey(name);
    }

    @Override
    public List<String> keys() {
        return _keys;
    }


    @Override
    public IDataItem set(String name, Object value) {
        _data.put(name,(()-> value));
        if(_keys.contains(name) == false) {
            _keys.add(name);
        }
        return this;
    }

    public DataItemEx set(String name, Fun0<Object> value) {
        _data.put(name,value);
        _keys.add(name);
        return this;
    }

    @Override
    public Object get(int index){
        return get(_keys.get(index));
    }
    @Override
    public Object get(String name) {
        return _data.get(name).run();
    }

    @Override
    public Variate getVariate(String name) {
        if (_data.containsKey(name)) {
            return new VariateEx(name, _data.get(name));
        }
        else {
            return new Variate(name, null);
        }
    }

    @Override
    public void remove(String name){
        _data.remove(name);
        _keys.remove(name);
    }

    @Override
    public <T extends IBinder> T toItem(T item) {
        item.bind((key) -> getVariate(key));

        return item;
    }


    @Override
    public short getShort(String name) {
        return (short)get(name);
    }

    @Override
    public int getInt(String name) {
        return (int)get(name);
    }

    @Override
    public long getLong(String name) {
        return (long)get(name);
    }

    @Override
    public double getDouble(String name) {
        return (double)get(name);
    }

    @Override
    public float getFloat(String name) {
        return (float)get(name);
    }

    @Override
    public String getString(String name) {
        return (String)get(name);
    }

    @Override
    public boolean getBoolean(String name) {
        return (boolean)get(name);
    }

    @Override
    public Date getDateTime(String name) {
        return (Date)get(name);
    }



    //
    //===========================
    //
    @Override
    public void forEach(Act2<String, Object> callback)
    {
        for(Map.Entry<String,Fun0<Object>> kv : _data.entrySet()) {
            Object val = kv.getValue().run();

            if (val == null && _isUsingDbNull) {
                callback.run(kv.getKey(), "$NULL");
            } else {
                callback.run(kv.getKey(), val);
            }
        }
    }

    private boolean _isUsingDbNull=false;
}

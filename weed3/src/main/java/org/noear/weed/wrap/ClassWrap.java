package org.noear.weed.wrap;

import org.noear.weed.DataItem;
import org.noear.weed.WeedConfig;
import org.noear.weed.annotation.Table;
import org.noear.weed.ext.Act2;
import org.noear.weed.utils.ThrowableUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Class 包装，方便缓存和列操作
 * */
public class ClassWrap {
    private static Map<Class<?>, ClassWrap> _cache = new ConcurrentHashMap<>();

    public static ClassWrap get(Class<?> clz) {
        ClassWrap clzWrap = _cache.get(clz);
        if (clzWrap == null) {
            clzWrap = new ClassWrap(clz);
            _cache.put(clz, clzWrap);
        }

        return clzWrap;
    }


    public final Class<?> clazz;
    public final List<FieldWrap> fieldWraps; //所有的字段（包括继承的）
    public final String tableName;
    private final Map<String,FieldWrap> _fieldWrapMap = new HashMap<>();

    protected ClassWrap(Class<?> clz) {
        clazz = clz;
        fieldWraps = new ArrayList<>();

        scanAllFields(clz, _fieldWrapMap::containsKey,(k,fw)->{
            fieldWraps.add(fw);
            _fieldWrapMap.put(k.toLowerCase(), fw);
        });

        Table ann = clz.getAnnotation(Table.class);
        if (ann != null) {
            tableName = ann.value();
        }else {
            tableName = WeedConfig.namingStrategy.classToTableName(clz);
        }
    }

    /** 扫描一个类的所有字段（不能与Snack3的复用；它需要排除非序列化字段） */
    private static void scanAllFields(Class<?> clz, Predicate<String> checker, BiConsumer<String,FieldWrap> consumer) {
        if (clz == null) {
            return;
        }

        for (Field f : clz.getDeclaredFields()) {
            int mod = f.getModifiers();

            if (!Modifier.isStatic(mod)) {
                if (checker.test(f.getName()) == false) {
                    f.setAccessible(true);
                    consumer.accept(f.getName(), new FieldWrap(clz, f));
                }
            }
        }

        Class<?> sup = clz.getSuperclass();
        if (sup != Object.class) {
            scanAllFields(sup, checker, consumer);
        }
    }

    public FieldWrap getFieldWrap(String name){
        return _fieldWrapMap.get(name.toLowerCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassWrap classWrap = (ClassWrap) o;
        return Objects.equals(clazz, classWrap.clazz);
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    public <T> T newInstance() {
        try {
            return (T)clazz.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //将 data 转为 entity
    public <T> T toEntity(DataItem data) {
        try {
            T item = (T) clazz.newInstance();

            for (FieldWrap fw : fieldWraps) {
                //转入时，不排除; 交dataItem检查
                if (data.exists(fw.name)) {
                    fw.setValue(item, data.get(fw.name));
                }
            }

            return item;
        } catch (Throwable ex) {
            ex = ThrowableUtils.throwableUnwrap(ex);
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    public void fromEntity(Object obj, Act2<String, Object> setter) {
        try {
            for (FieldWrap fw : fieldWraps) {
                if (!fw.exclude) {
                    //转出时，进行排除
                    setter.run(fw.name, fw.getValue(obj));
                }
            }
        } catch (Throwable ex) {
            ex = ThrowableUtils.throwableUnwrap(ex);
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }
}

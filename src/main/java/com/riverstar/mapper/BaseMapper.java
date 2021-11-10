package com.riverstar.mapper;

import com.riverstar.tool.StringTool;
import com.riverstar.entity.BaseEntity;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description: 数据库操作公共方法基类
 **/
public interface BaseMapper<T extends BaseEntity> {

    String MIN_MAX_COUNT_SQL = "select min(id) min, max(id) max, count(id) count from ";

    @InsertProvider(type = GenSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(T t);

    @UpdateProvider(type = GenSqlProvider.class, method = "update")
    void update(T t);

    @SelectProvider(type = GenSqlProvider.class, method = "findOne")
    T findOne(long id);

    @SelectProvider(type = GenSqlProvider.class, method = "findOneLock")
    T findOneLock(long id);

    @SelectProvider(type = GenSqlProvider.class, method = "findAll")
    List<T> findAll();

    @SelectProvider(type = GenSqlProvider.class, method = "findAllIdDesc")
    List<T> findAllIdDesc();

    @SelectProvider(type = GenSqlProvider.class, method = "findBySql")
    T findOneBySql(String condSql);

    @SelectProvider(type = GenSqlProvider.class, method = "findBySql")
    List<T> findListBySql(String condSql);

    @SelectProvider(type = GenSqlProvider.class, method = "findByCond")
    T findOneByCond(Map<String, Object> cond);

    @SelectProvider(type = GenSqlProvider.class, method = "findByCond")
    List<T> findListByCond(Map<String, Object> cond);

    class GenSqlProvider {

        public String insert(Object o) {
            StringJoiner fldSj = new StringJoiner(",", "(", ")");
            StringJoiner valSj = new StringJoiner(",", "(", ")");

            constructInsert(fldSj, valSj, o);

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ").append(StringTool.camelToUnderline(o.getClass().getSimpleName()));
            sb.append(fldSj.toString());
            sb.append(" values ");
            sb.append(valSj.toString());

            return sb.toString();
        }

        public String update(Object o) {
            StringJoiner updSj = new StringJoiner(",");

            constructUpdate(updSj, o);

            StringBuilder sb = new StringBuilder();
            sb.append("update ").append(StringTool.camelToUnderline(o.getClass().getSimpleName())).append(" set ");
            sb.append(updSj.toString());
            sb.append(" where id = #{id}");

            return sb.toString();
        }

        public String findOne(ProviderContext cxt) {
            String name = getInterfaceGeneric(cxt.getMapperType());
            return "select * from " +
                    StringTool.camelToUnderline(name) +
                    " where id = #{id}";
        }

        public String findOneLock(ProviderContext cxt) {
            String name = getInterfaceGeneric(cxt.getMapperType());
            return "select * from " +
                    StringTool.camelToUnderline(name) +
                    " where id = #{id} for update";
        }

        public String findAll(ProviderContext cxt) {
            String name = getInterfaceGeneric(cxt.getMapperType());
            return "select * from " + StringTool.camelToUnderline(name);
        }

        public String findAllIdDesc(ProviderContext cxt) {
            String name = getInterfaceGeneric(cxt.getMapperType());
            return StringTool.formatStr("select * from {} order by id desc", StringTool.camelToUnderline(name));
        }

        public String findBySql(ProviderContext cxt, String condSql) {
            String name = getInterfaceGeneric(cxt.getMapperType());
            return StringTool.formatStr("select * from {} where {}", StringTool.camelToUnderline(name), condSql);
        }

        public String findByCond(ProviderContext cxt, Map<String, Object> cond) {
            String name = getInterfaceGeneric(cxt.getMapperType());
            StringBuilder sb = new StringBuilder();

            if (cond != null && !cond.isEmpty()) {
                for (String fld : cond.keySet()) {
                    if (sb.length() != 0)
                        sb.append(" and ");

                    sb.append(StringTool.camelToUnderline(fld)).append("=#{").append(fld).append("}");
                }
            }
            return "select * from " + StringTool.camelToUnderline(name) + " where " + sb.toString();
        }

        private void constructInsert(StringJoiner fldSj, StringJoiner valSj, Object o) {
            List<Field[]> caches = getFields(o);

            for (int i = caches.size() - 1; i >= 0; i--) {
                append(fldSj, valSj, o, caches.get(i));
            }
        }

        private void constructUpdate(StringJoiner updSj, Object o) {
            List<Field[]> caches = getFields(o);

            for (int i = caches.size() - 1; i >= 0; i--) {
                append(updSj, o, caches.get(i));
            }
        }

        private void append(StringJoiner fldSj, StringJoiner valSj, Object o, Field[] fields) {
            for (Field field : fields) {
                if (checkNull(o, field)) continue;

                fldSj.add(StringTool.camelToUnderline(field.getName()));
                valSj.add("#{" + field.getName() + "}");
            }
        }

        private void append(StringJoiner updSj, Object o, Field[] fields) {
            for (Field field : fields) {
                if (checkNull(o, field) || "id".equals(field.getName())) continue;

                updSj.add(StringTool.camelToUnderline(field.getName()) + "=" + "#{" + field.getName() + "}");
            }
        }

        private String getInterfaceGeneric(Class cls) {
            if (cls == null)
                return null;

            // 获取继承的接口
            Type[] tyes = cls.getGenericInterfaces();
            if (tyes.length == 0)
                return null;

            ParameterizedType type = (ParameterizedType) tyes[0];

            // 获取继承的接口的泛型
            Type[] genTypes = type.getActualTypeArguments();
            if (genTypes.length - 1 < 0)
                return null;
            Class genCls = (Class) genTypes[0];

            return genCls.getSimpleName();
        }

        private boolean checkNull(Object o, Field field) {
            try {
                field.setAccessible(true);
                Object val = field.get(o);
                return val == null;
            } catch (Exception ignored) {
            }
            return true;
        }

        private List<Field[]> getFields(Object o) {
            List<Field[]> caches = new ArrayList<>();
            Class temp = o.getClass();

            while (temp != null && !temp.getSimpleName().equals("Object")) {
                caches.add(temp.getDeclaredFields());
                temp = temp.getSuperclass();
            }
            return caches;
        }
    }
}
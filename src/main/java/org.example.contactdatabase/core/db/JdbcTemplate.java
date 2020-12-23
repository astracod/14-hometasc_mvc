package org.example.contactdatabase.core.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JdbcTemplate {

    Connection connection;


    public JdbcTemplate() {
        try {
            connection = DatabaseHelper.getInstance().getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





    public interface RowMapper<T> {
        T map(ResultSet set) throws SQLException;
    }

    public <T> List<T> query(String query, List<Object> param, RowMapper<T> mapper) {

        LinkedList<T> result = new LinkedList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            for (int i = 0; i < param.size(); i++) {
                statement.setObject(i + 1, param.get(i));
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
            return result;

        } catch (SQLException throwables) {
            return result;
        }

    }


    public <T> List<T> query(String query, RowMapper<T> mapper) {

        LinkedList<T> result = new LinkedList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(mapper.map(resultSet));
            }
            return result;

        } catch (SQLException throwables) {
            return result;
        }
    }

    public void update(String query, List<Object> param) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < param.size(); i++) {
                statement.setObject(i+1,param.get(i));
            }

            statement.executeUpdate();

        } catch (SQLException throwables) { }

    }

    public static class PropertyBeanRowMapper<T> implements RowMapper {

        private Class<T> tClass;

        public PropertyBeanRowMapper(Class<T> clazz) {
            tClass = clazz;
        }

        @Override
        public Object map(ResultSet set) throws SQLException {
            try {
                T o = tClass.getConstructor().newInstance();
                Field[] fields = tClass.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    field.setAccessible(true);
                    field.set(o, set.getObject(name));
                }
                return o;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}

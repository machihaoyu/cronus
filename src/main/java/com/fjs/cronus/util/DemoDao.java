package com.fjs.cronus.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hong on 2017/4/25.
 */
@Repository
/** 开启事务 **/
@EnableTransactionManagement
public class DemoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * jdbcTemplate 和dataSource
     */
    @Autowired
    private DataSource dataSource;


    /**
     * 使用元数据接口的方式获取Demo列表.
     *
     * @return
     */
    public List<Map<String, Object>> getDemoList(String demo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return jdbcTemplate.execute(new StatementCallback<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> doInStatement(Statement statement) throws SQLException, DataAccessException {
                //查询所有的记录.
                ResultSet resultSet = statement.executeQuery("select * from " + demo);

                //获取元数据结果集.
                ResultSetMetaData metaData = resultSet.getMetaData();

                //获取到表结构有多少列.
                int columnCount = metaData.getColumnCount();

                List<Map<String, Object>> data = new LinkedList<>();

                List<String> columnNames = new ArrayList<>(columnCount);

                //获取到所有列字段名.
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                if ("agreement_copy".equals(demo)) {
                    while (resultSet.next()) {
                        Map<String, Object> map = new HashMap<>();
                        List<Object> list = new ArrayList<>();
                        for (String columnName : columnNames) {

                            Object columnValue = resultSet.getObject(columnName);
                            if ("create_time".equals(columnName)) {
                                long columnValueLong = (long) columnValue;
                                columnValue = new Date(columnValueLong);
                                columnValue = sdf.format(columnValue);
                            }
                            if ("pay_time".equals(columnName)) {
                                int columnValueInt = (int) columnValue;
                                long columnValueLong = (long) columnValueInt;
                                columnValue = new Date(columnValueLong);
                                columnValue = sdf.format(columnValue);
                            }
                            if ("update_time".equals(columnName)) {
                                columnValue = sdf.format(columnValue);
                            }
                            if ("create_user".equals(columnName)) {
                                list.add(columnValue);
                            }
                            list.add(columnValue);
                            map.put(columnName, list);

                        }
                        data.add(map);
                    }
                }

                return data;
            }
        });
    }


    public List<List<Object>> getDemoListT(String demo) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Connection connection = null;
        //获取到数据库连接
        connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + demo);
        //获取元数据结果集.
        ResultSetMetaData metaData = resultSet.getMetaData();
        //获取到表结构有多少列.
        int columnCount = metaData.getColumnCount();

        List<List<Object>> data = new LinkedList<>();

        List<String> columnNames = new ArrayList<>(columnCount);

        //获取到所有列字段名.
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        if ("agreement_copy".equals(demo)) {
            while (resultSet.next()) {
                List<Object> list = new ArrayList<>();
                for (String columnName : columnNames) {

                    Object columnValue = resultSet.getObject(columnName);
                    if ("create_time".equals(columnName)) {
                        long columnValueLong = (long) columnValue;
                        columnValue = new Date(columnValueLong);
                        columnValue = sdf.format(columnValue);
                    }
                    if ("pay_time".equals(columnName)) {
                        int columnValueInt = (int) columnValue;
                        long columnValueLong = (long) columnValueInt;
                        columnValue = new Date(columnValueLong);
                        columnValue = sdf.format(columnValue);
                    }
                    if ("update_time".equals(columnName)) {
                        columnValue = sdf.format(columnValue);
                    }
                    if ("create_user".equals(columnName)) {
                        list.add(columnValue);
                    }
                    list.add(columnValue);

                }
                data.add(list);
            }
        }

        return data;
    }

    /**
     * 在使用事务之前,我们得知道当前数据库是否支持事务.
     *
     * @return
     */
    public Boolean supportedTransaction() {
        boolean supported = false;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            //通过元数据接口获取当前数据库是否支持事务.
            supported = metaData.supportsTransactions();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return supported;
    }


/**
 * JdbcTemplate 插入记录
 * <p>
 * 加入事务处理.
 **/
    /**
     * 加入事务处理.
     **/
    @Transactional(noRollbackFor = Exception.class)
    public int save(List<List<Object>> lists) {
        int result = 0;
        String sql = "insert into agreement values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        for (List<Object> list : lists) {
             result= jdbcTemplate.update(sql, new Object[]{list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5),list.get(6),list.get(7),
                    list.get(8),list.get(9),list.get(10),list.get(11),list.get(12),list.get(13),list.get(14),list.get(15),list.get(16),
                    list.get(17),list.get(18),list.get(19),
                    list.get(20),list.get(21),list.get(6),0});
        }
        return result;
    }

    /**
     * 使用jdbcTemplate的方式,通过id获取demo对象.
     *
     * @param id
     * @return
    //     */
//    public Demo getById(Long id) {
//        String sql = "select * from demo where id=?";
//        RowMapper<Demo> rowMapper = new BeanPropertyRowMapper<>(Demo.class);
//        return jdbcTemplate.queryForObject(sql, rowMapper, id);
//    }


}

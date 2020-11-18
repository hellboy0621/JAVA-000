package com.transformers.jdbc;

import java.sql.*;

public class JdbcDemo {

    /**
     * CREATE TABLE `t_order`
     * (
     *     `order_id` INT,
     *     `user_id`  INT,
     *     `status`   VARCHAR(10),
     *     `column1`  VARCHAR(10),
     *     `column2`  VARCHAR(10),
     *     `column3`  VARCHAR(10)
     * );
     */

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(Constant.DRIVER);
        Connection conn = DriverManager.getConnection(Constant.URL, Constant.USER_NAME, Constant.PASSWORD);
        query(conn);
        System.out.println("*************");
        insert(conn);
        query(conn);
        System.out.println("*************");
        update(conn);
        query(conn);
        System.out.println("*************");
        delete(conn);
        query(conn);
        conn.close();
    }

    public static void update(Connection conn) {
        String sql = "update t_order set user_id = 4 where order_id = 3 ";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (pstmt.executeUpdate() == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void delete(Connection conn) {
        String sql = "delete from t_order where order_id = 3 ";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (pstmt.executeUpdate() == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void query(Connection conn) throws SQLException {
        String sql = "select * from t_order";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            System.out.print(rs.getInt("order_id") + "\t");
            System.out.print(rs.getInt("user_id") + "\t");
            System.out.print(rs.getString("status") + "\t");
            System.out.print(rs.getString("column1") + "\t");
            System.out.print(rs.getString("column2") + "\t");
            System.out.print(rs.getString("column3") + "\t");
            System.out.println();
        }
    }

    public static void insert(Connection conn) {
        String sql = "insert into t_order(order_id, user_id, status, column1, column2, column3) " +
                "values(?,?,?,?,?,?) ";
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 3);
            pstmt.setInt(2, 3);
            pstmt.setString(3, "c");
            pstmt.setString(4, "c");
            pstmt.setString(5, "c");
            pstmt.setString(6, "c");
            if (pstmt.executeUpdate() == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}

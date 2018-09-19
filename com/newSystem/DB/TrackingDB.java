package com.newSystem.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TrackingDB {
    private String dbURL;
    public TrackingDB() {
        dbURL = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            ///////////////////////////////////////////////////////////////////////////
            // 1. 시퀀스 확인.
            String query = "select count(*) from user_sequences where sequence_name = 'TRACKING_SEQUENCE'";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            int sCount = -1;
            while(rs.next())
                sCount = rs.getInt(1);
            if (sCount == 0) {
                query = "CREATE SEQUENCE TRACKING_SEQUENCE";
                pstmt = con.prepareStatement(query);
                pstmt.execute();
            }
            ///////////////////////////////////////////////////////////////////////////
            // 2. 테이블 확인.
            query = "select count(*) from user_tables where table_name = 'TRACKING_TABLE'";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            int tCount = -1;
            while(rs.next())
                tCount = rs.getInt(1);
            if (tCount == 0) {
                query = "CREATE TABLE TRACKING_TABLE (" +
                        "num NUMBER PRIMARY KEY," +
                        "pid VARCHAR(40)," +
                        "ip VARCHAR(20)," +
                        "request_time VARCHAR(30)" +
                        ")";
                pstmt = con.prepareStatement(query);
                pstmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String pid, String ip) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            String query = "INSERT INTO TRACKING_TABLE (num, pid, ip, REQUEST_TIME)" +
                    "VALUES (TRACKING_SEQUENCE.nextval, ?, ?, TO_CHAR(SYSDATE, 'yyyy/mm/dd HH24:MI:SS'))";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, pid);
            pstmt.setString(2, ip);
            int n = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

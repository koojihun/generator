package com.newSystem.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TrackingDB {
    public TrackingDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String pid, String ip) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String dbURL = "jdbc:oracle:thin:@localhost:1521:XE";
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            String query = "INSERT INTO track_request_table (seq, pid, ip, time)" +
                    "VALUES (track_num.nextval, ?, ?, TO_DATE(SYSDATE, 'yyyy/mm/dd HH24:MI:SS'))";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, pid);
            pstmt.setString(2, ip);
            int n = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.newSystem.DB;

import com.newSystem.Main;
import com.newSystem.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CompanyDB {
    private String dbURL;
    public CompanyDB() {
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
            String query = "select count(*) from user_sequences where sequence_name = 'COMPANY_SEQUENCE'";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            int sCount = -1;
            while(rs.next())
                sCount = rs.getInt(1);
            if (sCount == 0) {
                query = "CREATE SEQUENCE COMPANY_SEQUENCE";
                pstmt = con.prepareStatement(query);
                pstmt.execute();
            }
            ///////////////////////////////////////////////////////////////////////////
            // 2. 테이블 확인.
            query = "select count(*) from user_tables where table_name = 'COMPANY_TABLE'";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            int tCount = -1;
            while(rs.next())
                tCount = rs.getInt(1);
            if (tCount == 0) {
                query = "CREATE TABLE COMPANY_TABLE (" +
                        "num NUMBER PRIMARY KEY," +
                        "companyName VARCHAR(30)," +
                        "companyAddress VARCHAR(50)," +
                        "directorName VARCHAR(30)," +
                        "directorEmail VARCHAR(30)," +
                        "directorPhone VARCHAR(30)" +
                        ")";
                pstmt = con.prepareStatement(query);
                pstmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAll(String companyName, String companyAddress, String directorName, String directorEmail, String directorPhone) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            String query = "INSERT INTO COMPANY_TABLE (num, COMPANYNAME, COMPANYADDRESS, DIRECTORNAME, DIRECTOREMAIL, DIRECTORPHONE)" +
                    "VALUES (COMPANY_SEQUENCE.nextval, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, companyName);
            pstmt.setString(2, companyAddress);
            pstmt.setString(3, directorName);
            pstmt.setString(4, directorEmail);
            pstmt.setString(5, directorPhone);
            int n = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.companyAddresses.put(Settings.companyName, Settings.companyAddress);
    }

    public void insertExceptAddress(String companyName, String directorName, String directorEmail, String directorPhone) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            String query = "INSERT INTO COMPANY_TABLE (num, COMPANYNAME, DIRECTORNAME, DIRECTOREMAIL, DIRECTORPHONE)" +
                    "VALUES (COMPANY_SEQUENCE.nextval, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, companyName);
            pstmt.setString(2, directorName);
            pstmt.setString(3, directorEmail);
            pstmt.setString(4, directorPhone);
            int n = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAddress(String companyName, String companyAddress) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            String query = "UPDATE COMPANY_TABLE SET COMPANYADDRESS = ? WHERE COMPANYNAME = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, companyAddress);
            pstmt.setString(2, companyName);
            int n = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.companyAddresses.put(companyName, companyAddress);
    }

    public void getCompanyNamesAndAddresses() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(dbURL, "koo", "jin278");
            String query = "SELECT COMPANYNAME, COMPANYADDRESS FROM COMPANY_TABLE";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                String cn = rs.getString(1);
                String ca = rs.getString(2);
                Main.companyAddresses.put(cn, ca);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

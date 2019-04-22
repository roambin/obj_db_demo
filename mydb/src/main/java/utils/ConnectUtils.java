package utils;

import info.DBInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectUtils {


    public static Connection getConnection()  {
        //System.out.println("start> connect database:");
        try {
            Class.forName(DBInfo.driver);
        }catch (ClassNotFoundException e) {
            System.out.println("false> connect/forname:"+e);
            e.printStackTrace();
            return null;
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DBInfo.url,DBInfo.user,DBInfo.pass);
        }catch (SQLException e) {
            System.out.println("false> connect/getConnection:"+e);
            e.printStackTrace();
            return null;
        }
        if(conn == null){
            try {
                throw new Exception("can't get connection");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //System.out.println("success> connect database:"+conn+" ");
        return conn;
    }

    public static boolean closeConnection(Connection conn)  {
        try {
            if(conn!=null) {
                conn.close();
            }
        }catch (Exception e) {
            System.out.println("false> connect/getConnection:"+e);
            e.printStackTrace();
            return false;
        }
        //System.out.println("end> close connect:"+conn);
        return true;
    }
    public static boolean closeRs(ResultSet rs)  {
        try {
            if(rs!=null) {
                rs.close();
            }
        }catch (Exception e) {
            System.out.println("false> connect/closeRs:"+e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean closePstm(PreparedStatement pstm)  {
        try {
            if(pstm!=null) {
                pstm.close();
            }
        }catch (Exception e) {
            System.out.println("false> connect/closePstm:"+e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean closeAll(Connection conn, PreparedStatement pstm, ResultSet rs){
        return closeConnection(conn) && closePstm(pstm) && closeRs(rs);
    }
    public static boolean runCommand(String command){
        Connection conn = ConnectUtils.getConnection();
        PreparedStatement pstm = null;
        try{
            pstm = conn.prepareStatement(command);
            boolean result = pstm.execute();
            pstm.close();
            conn.close();
            return result;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public static ResultSet runCommandQuery(Connection conn, PreparedStatement pstm, String command){
        ResultSet rs = null;
        try{
            pstm = conn.prepareStatement(command);
            rs = pstm.executeQuery();
            return rs;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}

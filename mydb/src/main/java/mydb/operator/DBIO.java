package mydb.operator;

import mapping.operator.IO;
import mydb.utils.ConnectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBIO extends IO {
    public Connection conn;
    public PreparedStatement pstm;
    @Override
    public boolean open() {
        conn = ConnectUtils.getConnection();
        return true;
    }

    @Override
    public boolean close() {
        ConnectUtils.closePstm(pstm);
        ConnectUtils.closeConnection(conn);
        return true;
    }
}

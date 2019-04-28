package compute.entity;

import compute.Setting;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableInfo {
    public LinkedHashMap<String, String> colTypeMap = new LinkedHashMap<>();
    public String databaseName;
    public String tableName;
    public String storageName;
    public String keyName = "";
    public TableInfo(String databaseName, String tableName){
        this.databaseName = databaseName;
        this.tableName = tableName;
    }
    public boolean getTableInfo(){
        String fileName = databaseName + " " + tableName;
        File file = new File(Setting.tableInfoPath + fileName);
        try{
            if(!file.exists() || file.isDirectory()){
                return false;
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            storageName = br.readLine();
            keyName = br.readLine();
            String line = br.readLine();
            while(line != null){
                String[] kv = line.split(" ");
                colTypeMap.put(kv[0], kv[1]);
                line = br.readLine();
            }
            br.close();
            fr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public void storeTableInfo(String storage){
        String fileName = databaseName + " " + tableName;
        File file = new File(Setting.tableInfoPath + fileName);
        try{
            File dir = new File(Setting.tableInfoPath);
            if(!dir.exists() || !dir.isDirectory()){
                dir.mkdirs();
            }
            if(file.exists() && !file.isDirectory()){
                throw new Exception("table info exists");
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(storage);
            fw.write('\n');
            if(keyName == null) keyName = "";
            fw.write(keyName);
            fw.write('\n');
            for(Map.Entry<String, String> entry: colTypeMap.entrySet()){
                fw.write(entry.getKey());
                fw.write(' ');
                fw.write(entry.getValue());
                fw.write('\n');
            }
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteFactTableInfo(){
        String fileName = databaseName + " " + tableName;
        File file = new File(Setting.tableInfoPath + fileName);
        if(file.exists()){
            file.delete();
        }
    }
    public String getType(String colName){
        return colTypeMap.get(colName);
    }
    public String[] getTypes(){
        String[] types = new String[colTypeMap.size()];
        return colTypeMap.keySet().toArray(types);
    }
    public ArrayList<String> getTypes(String... colNames){
        ArrayList<String> colTypes = new ArrayList<>();
        for(String colName: colNames){
            colTypes.add(this.colTypeMap.get(colName));
        }
        return colTypes;
    }
    public static String[] listTables(){
        File file = new File(Setting.tableInfoPath);
        return file.list();
    }
}

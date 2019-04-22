package compute.entity;

import compute.Setting;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableInfo {
    public LinkedHashMap<String, String> colTypes = new LinkedHashMap<>();
    String databaseName;
    String tableName;
    public TableInfo(String databaseName, String tableName){
        this.databaseName = databaseName;
        this.tableName = tableName;
    }
    public void getTableInfo(){
        String fileName = databaseName + " " + tableName;
        File file = new File(Setting.tableInfoPath + fileName);
        try{
            if(!file.exists() || file.isDirectory()){
                throw new Exception("table info not exists");
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null){
                String[] kv = line.split(" ");
                colTypes.put(kv[0], kv[1]);
                line = br.readLine();
            }
            br.close();
            fr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void storeTableInfo(){
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
            for(Map.Entry<String, String> entry: colTypes.entrySet()){
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
        return colTypes.get(colName);
    }
    public String[] getTypes(){
        String[] types = new String[colTypes.size()];
        return colTypes.keySet().toArray(types);
    }
}

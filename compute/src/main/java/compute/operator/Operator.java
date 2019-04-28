package compute.operator;

import compute.entity.LogicalPlan;
import compute.entity.Result;
import mapping.OpObject;
import mapping.operator.IO;
import mydb.operator.DBIO;
import mydb.operator.DBOpObject;
import myfile.operator.FileOpObject;

public class Operator {
    public Result operate(LogicalPlan logicalPlan) {
        Result operatorResult = null;
        switch (logicalPlan.type){
            case "select":
                operatorResult = SelectOperator.operate(logicalPlan);
                break;
            case "insert":
                operatorResult = InsertOperator.operate(logicalPlan);
                break;
            case "update":
                operatorResult = UpdateOperator.operate(logicalPlan);
                break;
            case "delete":
                operatorResult = DeleteOperator.operate(logicalPlan);
                break;
            case "create":
                operatorResult = CreateOperator.operate(logicalPlan);
                break;
            case "drop":
                operatorResult = DropOperator.operate(logicalPlan);
                break;
            case "truncate":
                operatorResult = TruncateOperator.operate(logicalPlan);
                break;
            case "show":
                operatorResult = ShowOperator.operate(logicalPlan);
                break;
        }
        return operatorResult;
    }
    public static OpObject getStorageOpObject(String storage){
        OpObject opObject = null;
        switch (storage){
            case "myfile":
                opObject = new FileOpObject();
                break;
            case "mydb":
                opObject = new DBOpObject();
                break;
        }
        return opObject;
    }
    public static IO getStorageIO(String storage){
        IO io = null;
        switch (storage){
            case "myfile":
                io = null;
                break;
            case "mydb":
                io = new DBIO();
                break;
        }
        return io;
    }
}

package compute;

import compute.entity.LogicalPlan;
import compute.entity.Result;
import compute.operator.Operator;
import compute.parser.Parser;

import java.util.Scanner;

public class Server {
    private Parser parser = null;
    private Operator operator = null;
    private Optimizer optimizer = null;
    public Server() {
        try {
            parser = getParser().getDeclaredConstructor().newInstance();
            operator = getOperator().getDeclaredConstructor().newInstance();
            optimizer = getOptimizer().getDeclaredConstructor().newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(parser == null)  parser = new Parser();
        if(operator == null)  operator = new Operator();
        if(optimizer == null)  optimizer = new Optimizer();
    }
    protected Class<? extends Operator> getOperator(){
        return Operator.class;
    }
    protected Class<? extends Parser> getParser(){
        return Parser.class;
    }
    protected Class<? extends Optimizer> getOptimizer(){
        return Optimizer.class;
    }
    public final Result runCommand(String command) {
        LogicalPlan logicalPlan = parser.parse(command);
        optimizer.optimize(logicalPlan);
        Result result = operator.operate(logicalPlan);
        result.command = command;
        return result;
    }
    protected static void run(Server server) {
        Scanner scanner = new Scanner(System.in);
        String command;
        while(true){
           System.out.print("wormhole> ");
           command = scanner.nextLine();
           Result Result = server.runCommand(command);
           Result.command =  command;
           System.out.println(Result.getString());
        }
    }
    public static void main(String[] args){
        run(new Server());
    }
}

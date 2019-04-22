package compute;

import compute.entity.LogicalPlan;
import compute.entity.OperatorResult;
import compute.entity.Result;
import compute.parser.Parser;

import java.util.Scanner;

public class Server {
    private Parser parser = null;
    private Operator operator = null;
    private Optimizer optimizer = null;
    private Formatter formatter = null;
    public Server() {
        try {
            parser = getParser().getDeclaredConstructor().newInstance();
            operator = getOperator().getDeclaredConstructor().newInstance();
            optimizer = getOptimizer().getDeclaredConstructor().newInstance();
            formatter = getFormatter().getDeclaredConstructor().newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(parser == null)  parser = new Parser();
        if(operator == null)  operator = new Operator();
        if(optimizer == null)  optimizer = new Optimizer();
        if(formatter == null)  formatter = new Formatter();
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
    protected Class<? extends Formatter> getFormatter(){
        return Formatter.class;
    }
    public final Result runCommand(String command) {
        LogicalPlan logicalPlan = parser.parse(command);
        optimizer.optimize(logicalPlan);
        OperatorResult operatorResult = operator.operate(logicalPlan);
        Result result = formatter.format(operatorResult);
        return result;
    }
    protected static void run(Server server) {
        Scanner scanner = new Scanner(System.in);
        String command;
        while(true){
           System.out.print("wormhole> ");
           command = scanner.nextLine();
           Result operatorResult = server.runCommand(command);
           System.out.println(operatorResult.toString());
        }
    }
    public static void main(String[] args){
        run(new Server());
    }
}

package sample;

/**
 * Created by Laimi on 06.12.2017.
 */
public class Arguments {
    private String[] args;
    private String mode;
public void setArgs(String[] args){
    if(args[0]==""){
        System.out.println("No arguments found! Please, choose mode:");
        String s = sc.nextLine();
        if(s=="parallel"||s=="p"){
            System.out.println("Starting parallel mode...");

        }
        if(s=="OpenPose"&&s=="op"){
            System.out.println("Starting OpenPose only mode...");
        }
        if(s=="NewVision"&&s=="nv"){
            System.out.println("Starting NewVision only mode...");
        }
        if(s=="q"&&s=="exit"){
            System.exit(0);
        }
    }
}
}
}

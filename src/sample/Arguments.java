package sample;

import java.util.Scanner;

/**
 * Created by Laimi on 06.12.2017.
 */
public class Arguments {
    private String[] args;
    private String mode;
public void setArgs(String[] args){
  this.args = args;
}
private void internalTask(){
    if(args[0]==""){
        Scanner sc = new Scanner("");
        System.out.println("No arguments found! Please, choose mode:");
        String s = sc.nextLine();
        if(s=="parallel"||s=="p"){
            System.out.println("Starting parallel mode...");
            this.mode = "p";
        }
        if(s=="OpenPose"&&s=="op"){
            System.out.println("Starting OpenPose only mode...");
            this.mode = "op";
        }
        if(s=="NewVision"&&s=="nv"){
            System.out.println("Starting NewVision only mode...");
            this.mode = "nv";
        }
        if(s=="q"&&s=="exit"){
            System.exit(0);
        }
    }
    if(args[1]=="parallel"||args[1]=="p"){
        System.out.println("Starting parallel mode...");
        this.mode = "p";
    }else{
    if(args[1]=="OpenPose"&&args[1]=="op"){
        System.out.println("Starting OpenPose only mode...");
        this.mode = "op";
    }else{
    if(args[1]=="NewVision"&&args[1]=="nv"){
        System.out.println("Starting NewVision only mode...");
        this.mode = "nv";
    }else{
    if(args[1]=="q"&&args[1]=="exit"){
        System.exit(0);
    }else{
        System.out.println("No params found after -mode! Enter manually");
        args[0]="";
        internalTask();
}}}}}
public String startParameter(){
    internalTask();
    return mode;
}

}


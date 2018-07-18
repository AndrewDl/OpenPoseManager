package sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Created by Laimi on 07.12.2017.
 */
public class Arguments {

    private Logger logger = LogManager.getLogger("OPManager");
        private String[] args;
        private String mode;
    Arguments(String[] args){
        this.args = args;
        internalTask();
    }
        public void setArgs(String[] args){
            this.args = args;
        }
        private void internalTask(){
            String p = "Starting parallel mode...";
            String op = "Starting OpenPose only mode...";
            String nv = "Starting NewVision only mode...";
            if(args.length<1||!args[0].equals("-mode")) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Arguments: p - parallel mode \n op - OpenPose only mode \n nv - Newvision only mode \n q - exit");
                System.out.println("No arguments found! Please, choose mode:");
                String s = sc.nextLine();
                if (s.equals("parallel") || s.equals("p")) {
                    System.out.println(p);
                    logger.info(p);
                    this.mode = "p";
                }
                if (s.equals("OpenPose") || s.equals("op")) {
                    System.out.println(op);
                    logger.info(op);
                    this.mode = "op";
                }
                if (s.equals("NewVision") || s.equals("nv")) {
                    System.out.println(nv);
                    logger.info(nv);
                    this.mode = "nv";
                }
                if (s.equals("q") || s.equals("exit")) {
                    System.out.println("bye");
                    System.exit(0);
                }
            }else{if(args[0].equals("-mode")){
            if(args[1].equals("parallel")||args[1].equals("p")){
                logger.info(p);
                System.out.println("Starting parallel mode...");
                this.mode = "p";
            }else{
                if(args[1].equals("OpenPose")||args[1].equals("op")){
                    logger.info(op);
                    System.out.println("Starting OpenPose only mode...");
                    this.mode = "op";
                }else{
                    if(args[1].equals("NewVision")||args[1].equals("nv")){
                        logger.info(nv);
                        System.out.println("Starting NewVision only mode...");
                        this.mode = "nv";
                    }else{
                        if(args[1].equals("q")||args[1].equals("exit")){
                            System.exit(0);
                        }else{
                            System.out.println("No params found after -mode! Enter manually");
                            args[0]="";
                        }}}}}}}
        public String getMode(){
            return mode;
        }

    }



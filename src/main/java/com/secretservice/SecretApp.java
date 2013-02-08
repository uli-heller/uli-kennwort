package com.secretservice;

public class SecretApp {
    private static final String HEADLINE="My Secret Application\n"
                                        +"---------------------\n";
    private static final String NO_ARGS="No command line arguments\n";
    private static final long SLEEP_MILLIS=60000L;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println(HEADLINE);
        if (args == null || args.length <= 0) {
            System.out.println(NO_ARGS);
        } else {
            int i=0;
            for (String a : args) {
                System.out.println("arg["+i+"]='"+a+"'");
                ++i;
            }
        }
        Thread.sleep(SLEEP_MILLIS);
    }
}

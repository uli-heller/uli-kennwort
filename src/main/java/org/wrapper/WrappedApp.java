package org.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WrappedApp {
    private static final String HEADLINE="Wrapped Application\n"
                +"-------------------\n";
    private static final String NO_ARGS="No command line arguments\n";
    private static final String SEPARATOR="\n===================\n\n";
    
    public static void main(String[] args) throws Exception {
        System.out.println(HEADLINE);
        if (args == null || args.length <= 0) {
            System.out.println(NO_ARGS);
            System.exit(1);
        } else {
            int i=0;
            for (String a : args) {
                System.out.println("arg["+i+"]='"+a+"'");
                ++i;
            }
        }
        
        String[] decryptedArgs = decryptMostArgs(args);
        System.out.println(SEPARATOR);
        executeMain(args[0], decryptedArgs);
    }
    
    final private static String[] decryptMostArgs(String[] args) throws Exception {
        int len = args.length;
        String[] decrypted = new String[len-1];
        for (int i=len-1; i>0; --i) {
            decrypted[i-1] = Crypton.getInstance().decrypt(args[i]);
        }
        return decrypted;
    }
    
    final private static void executeMain(String className, String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> app = Class.forName(className);
        Method m = app.getMethod("main", String[].class);
        m.invoke(null, (Object) args);
    }
}

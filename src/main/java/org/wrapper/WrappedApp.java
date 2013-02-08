package org.wrapper;

import com.secretservice.SecretApp;

public class WrappedApp {
    private static final String HEADLINE="Wrapped Application\n"
                +"-------------------\n";
    private static final String NO_ARGS="No command line arguments\n";
    private static final int ENCRYPTED_ARG_INDEX=2;
    private static final String SEPARATOR="\n===================\n\n";
    
    public static void main(String[] args) throws Exception {
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
        
        String[] decryptedArgs = decryptArgs(args);
        System.out.println(SEPARATOR);
        SecretApp.main(decryptedArgs);
    }
    
    final private static String[] decryptArgs(String[] args) throws Exception {
        int len = args.length;
        String[] decrypted = new String[len];
        System.arraycopy(args, 0, decrypted, 0, len);
        if (len > ENCRYPTED_ARG_INDEX) {
            decrypted[ENCRYPTED_ARG_INDEX] = Crypton.getInstance().decrypt(args[ENCRYPTED_ARG_INDEX]);
        }
        return decrypted;
    }
}

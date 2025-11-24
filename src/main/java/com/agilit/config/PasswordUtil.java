package com.agilit.config;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
    }

    public static boolean check(String senhaPura, String senhaHash) {
        return BCrypt.checkpw(senhaPura, senhaHash);
    }
}

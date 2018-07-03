package com.app.inpahu.securityapp.Helpers;

import java.util.regex.Pattern;

public class Generics {

    public static boolean validateMail(String email) {
        return (Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email)).find();
    }
}

package com.studentapp.utils;

import java.util.Random;

public class TestUtil {

    public static String generateRandomValue(){
        Random random = new Random();
        int randomInt = random.nextInt(100000);

        return Integer.toString(randomInt);
    }
}

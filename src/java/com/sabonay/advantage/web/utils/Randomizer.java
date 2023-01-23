/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author crash
 */
public class Randomizer {
    public static int generate() throws NoSuchAlgorithmException
    {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        int rand = random.nextInt();
        return rand;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        System.out.println(Randomizer.generate());
    }
}

package com.myapps.toualbiamine.food2class.Utils;

import org.libsodium.jni.NaCl;

public class PasswordHasher {
    //checks password and returns false if the password is incorrect
    public boolean checkPassword(String guess,String theHashedPassword){
        byte[] passBytes = guess.getBytes();
        byte[] hashedPass = stringToByteArray(theHashedPassword);
        if(NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_str_verify(hashedPass,passBytes,passBytes.length)!=0){
            //wrong password
            return false;
        }
        //right password
        return true;
    }
    public String hashPassword(String password){
        byte[] hashedPass = new byte[NaCl.sodium().crypto_pwhash_strbytes()];
        byte[] passBytes = password.getBytes();
        NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_str(hashedPass,passBytes,passBytes.length,NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_opslimit_interactive(),NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_memlimit_interactive());
        return byteArrayToString(hashedPass);
    }
    //converts a byte array into a special string that can be undone by stringToByteArray
    private String byteArrayToString(byte[] a){
        String s = "";
        for(byte c:a){
            s+=c+",";
        }
        return s;
    }
    //converts a string made from byteArrayToString back into a byte array
    private byte[] stringToByteArray(String encryptedPassword){
        byte[] hashedPass = new byte[NaCl.sodium().crypto_pwhash_strbytes()];
        String[] splitString = encryptedPassword.split(",");
        if(splitString.length == hashedPass.length){
            for(int i=0;i<splitString.length;i++) {
                hashedPass[i]=Byte.parseByte(splitString[i]);
            }

        }
        else{
            System.out.println("Why aren't they the same size?!");
        }
        return hashedPass;
    }
}

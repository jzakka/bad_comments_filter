package com.march.bad_comments_filter.security;

import com.march.bad_comments_filter.label.Labels;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class KeyGenerator {
    private final String algorithm;

    public String generate(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public List<String> getLabelKey(String hashedKey){
        return Arrays.stream(Labels.values()).map(label -> label + ":" + hashedKey).toList();
    }
}

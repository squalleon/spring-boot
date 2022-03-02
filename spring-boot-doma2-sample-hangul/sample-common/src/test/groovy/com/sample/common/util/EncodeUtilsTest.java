package com.sample.common.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.jupiter.api.Assertions.*;

class EncodeUtilsTest {

    @Test
    void encodeUtf8() {
        EncodeUtils encodeUtils = new EncodeUtils();
        String encoded = null;
        try {
            encoded = URLEncoder.encode("aa.txt", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(encoded);
        assertEquals("aa.txt", encoded);
        assertNotNull(encoded);
    }
}
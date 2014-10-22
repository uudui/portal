package com.nx;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Neal on 10/19 019.
 */
public class CipherTest {
    @Test
    public void md5() {
        SimpleHash s = new SimpleHash("MD5", ByteSource.Util.bytes("password"), ByteSource.Util.bytes("neal"), 1);
        Assert.assertEquals(s.toHex().toString(), "72a5b1a4e5097e2bc23ab774746221e5");

    }
}

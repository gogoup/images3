package com.images3.core.infrastructure;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;


public class ShortUUID {

    public static String randomUUID() {
        return new String(Base64.encodeBase64(toByteArray(UUID.randomUUID()), false, true)).toLowerCase();
    }
    
    private static byte[] toByteArray(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}

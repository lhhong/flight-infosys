package com.czce4013.marshaller;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.*;

public class Marshaller {

    private static final Logger logger = LoggerFactory.getLogger(Marshaller.class);

    public static <T> void unmarshall(T obj, byte[] data) {
    }

    public static byte[] marshall(Object obj) {
        List<Byte> res = new ArrayList<>();

        String className = obj.getClass().getName();
        marshallString(className, res);

        marshallObject(obj, res);
        logger.debug(Arrays.toString(res.toArray()));
        return Bytes.toArray(res);
    }

    private static void marshallObject(Object obj, List<Byte> res) {

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f: fields) {
            FieldId fieldId = f.getAnnotation(FieldId.class);

            if (fieldId == null) continue;

            byte id = fieldId.value();
            res.add(id);

            String type = f.getGenericType().getTypeName();
            f.setAccessible(true);
            try {
                Object o = f.get(obj);
                logger.debug("Type: {}, Id: {}, Val: {}", type, id, o);
                addObjectToBytes(type, o, res);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void marshallSelect(String[] typeWithGeneric, Object o, List<Byte> res) {

        switch (typeWithGeneric[0]) {
            case "java.lang.String":
                marshallString(o, res);
                break;
            case "int":
                marshallInt(o, res);
                break;
            case "double":
                marshallDouble(o, res);
                break;
            case "java.util.List":
                marshallList(o, Arrays.copyOfRange(typeWithGeneric,1,typeWithGeneric.length), res);
                break;
            default:
                marshallObject(o, res);
                break;
        }

    }

    private static void addObjectToBytes(String type, Object o, List<Byte> res) {

        String[] typeWithGeneric = type.split("[<>]");
        marshallSelect(typeWithGeneric, o, res);
    }

    private static void marshallList(Object o, String[] typeWithGeneric, List<Byte> res) {

        List<?> list = (List<?>) o;
        res.addAll(intToByteList(list.size()));

        for (Object item: list) {
            marshallSelect(typeWithGeneric, item, res);
        }
    }

    private static void marshallInt(Object o, List<Byte> res) {
        int i = (int) o;
        res.addAll(intToByteList(i));
    }

    private static void marshallDouble(Object o, List<Byte> res) {
        double d = (double) o;
        res.addAll(doubleToByteList(d));
    }

    private static void marshallString(Object o, List<Byte> res) {
        String s = (String) o;

        res.addAll(intToByteList(s.length()));
        res.addAll(Bytes.asList(s.getBytes()));
    }

    private static List<Byte> intToByteList(int v) {
        byte[] array = ByteBuffer.allocate(Integer.BYTES).putInt(v).array();
        return Bytes.asList(array);
    }

    private static List<Byte> doubleToByteList(double v) {
        byte[] array = ByteBuffer.allocate(Double.BYTES).putDouble(v).array();
        return Bytes.asList(array);
    }
}

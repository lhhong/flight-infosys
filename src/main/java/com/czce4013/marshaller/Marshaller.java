package com.czce4013.marshaller;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.*;

public class Marshaller {

    private static final Logger logger = LoggerFactory.getLogger(Marshaller.class);

    public static <T extends Marshallable> T unmarshall(byte[] data, Class<T> clazz) {
        List<Byte> byteList = new LinkedList<>(Bytes.asList(data));
        String className = unmarshallString(byteList);
        if (!className.equals(clazz.getName())) {
            logger.error("Wrong class type to unmarshall");
            return null;
        }
        return unmarshallObject(byteList, clazz);
    }

    public static byte[] marshall(Object obj) {
        List<Byte> res = new ArrayList<>();

        String className = obj.getClass().getName();
        marshallString(className, res);

        marshallObject(obj, res);
        logger.debug(Arrays.toString(res.toArray()));
        return Bytes.toArray(res);
    }

    private static <T> T unmarshallObject(List<Byte> byteList, Class<T> clazz) {

        T obj;
        try {
            obj = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Empty constructor not defined for {}", clazz);
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> fieldTypeMap = new HashMap<>();
        for (Field f: fields) {

            if (f.getAnnotation(IgnoreField.class) != null) continue;

            fieldTypeMap.put(f.getName(), f);
            f.setAccessible(true);
        }

        while (fieldTypeMap.size() > 0) {
            //byte fieldId = byteList.remove(0);
            String fieldName = unmarshallString(byteList);
            Field field = fieldTypeMap.get(fieldName);
            try {
                field.set(obj, unmarshallSelect(byteList, field.getGenericType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            fieldTypeMap.remove(fieldName);
        }

        return obj;
    }

    private static void marshallObject(Object obj, List<Byte> res) {

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f: fields) {

            if (f.getAnnotation(IgnoreField.class) != null) continue;

            marshallString(f.getName(), res);

            String type = f.getGenericType().getTypeName();
            f.setAccessible(true);
            try {
                Object o = f.get(obj);
                logger.debug("Type: {}, Name: {}, Val: {}", type, f.getName(), o);
                addObjectToBytes(type, o, res);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addObjectToBytes(String type, Object o, List<Byte> res) {

        String[] typeWithGeneric = type.split("[<>]");
        marshallSelect(typeWithGeneric, o, res);
    }

    private static Object unmarshallSelect(List<Byte> byteList, Type type) {

        String[] typeWithGeneric = type.getTypeName().split("[<>]");
        switch (typeWithGeneric[0]) {
            case "java.lang.String":
                return unmarshallString(byteList);
            case "int":
            case "java.lang.Integer":
                return unmarshallInt(byteList);
            case "double":
            case "java.lang.Double":
                return unmarshallDouble(byteList);
            case "java.util.List":
                Type genericType = ((ParameterizedType) type).getActualTypeArguments()[0];
                return unmarshallList(byteList, genericType);
            default:
                return unmarshallObject(byteList, (Class<?>) type);
        }
    }

    private static void marshallSelect(String[] typeWithGeneric, Object o, List<Byte> res) {

        switch (typeWithGeneric[0]) {
            case "java.lang.String":
                marshallString(o, res);
                break;
            case "int":
            case "java.lang.Integer":
                marshallInt(o, res);
                break;
            case "double":
            case "java.lang.Double":
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

    private static <T> List<T> unmarshallList(List<Byte> byteList, Type genericType) {
        List<T> list = new ArrayList<>();
        int size = intFromByteList(byteList);

        for (int i = 0; i < size; i++) {
            T obj = (T) unmarshallSelect(byteList, genericType);
            list.add(obj);
        }

        return list;
    }

    private static void marshallList(Object o, String[] typeWithGeneric, List<Byte> res) {

        List<?> list = (List<?>) o;
        res.addAll(intToByteList(list.size()));

        for (Object item: list) {
            marshallSelect(typeWithGeneric, item, res);
        }
    }

    private static int unmarshallInt(List<Byte> byteList) {
        return intFromByteList(byteList);
    }

    private static void marshallInt(Object o, List<Byte> res) {
        int i = (int) o;
        res.addAll(intToByteList(i));
    }

    private static double unmarshallDouble(List<Byte> byteList) {
        return doubleFromByteList(byteList);
    }

    private static void marshallDouble(Object o, List<Byte> res) {
        double d = (double) o;
        res.addAll(doubleToByteList(d));
    }

    private static String unmarshallString(List<Byte> byteList) {

        int size = intFromByteList(byteList);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append((char) byteList.remove(0).byteValue());
        }
        return builder.toString();
    }

    private static void marshallString(Object o, List<Byte> res) {
        String s = (String) o;

        res.addAll(intToByteList(s.length()));
        res.addAll(Bytes.asList(s.getBytes()));
    }

    private static int intFromByteList(List<Byte> byteList) {
        byte[] intBytes = new byte[Integer.BYTES];
        for (int i = 0; i < Integer.BYTES; i++) {
            intBytes[i] = byteList.remove(0);
        }
        return ByteBuffer.wrap(intBytes).getInt();
    }

    private static List<Byte> intToByteList(int v) {
        byte[] array = ByteBuffer.allocate(Integer.BYTES).putInt(v).array();
        return Bytes.asList(array);
    }

    private static double doubleFromByteList(List<Byte> byteList) {
        byte[] doubleBytes = new byte[Double.BYTES];
        for (int i = 0; i < Double.BYTES; i++) {
            doubleBytes[i] = byteList.remove(0);
        }
        return ByteBuffer.wrap(doubleBytes).getDouble();
    }

    private static List<Byte> doubleToByteList(double v) {
        byte[] array = ByteBuffer.allocate(Double.BYTES).putDouble(v).array();
        return Bytes.asList(array);
    }
}

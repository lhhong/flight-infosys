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

    public static Object unmarshall(byte[] data) throws ClassNotFoundException {
        List<Byte> byteList = new LinkedList<>(Bytes.asList(data));
        String className = unmarshallString(byteList);
        int id = unmarshallInt(byteList);
        Object o = unmarshallObject(byteList, Class.forName(className));
        if (o instanceof Marshallable) {
            Marshallable m = (Marshallable) o;
            m.setId(id);
            return m;
        }
        return o;
    }

    public static byte[] marshall(Marshallable obj) {
        List<Byte> res = new ArrayList<>();

        String className = obj.getClass().getName();
        marshallString(className, res);
        marshallInt(obj.getId(), res);

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
            case "short":
            case "java.lang.Short":
                return unmarshallShort(byteList);
            case "int":
            case "java.lang.Integer":
                return unmarshallInt(byteList);
            case "float":
            case "java.lang.Float":
                return unmarshallFloat(byteList);
            case "double":
            case "java.lang.Double":
                return unmarshallDouble(byteList);
            case "java.util.List":
                Type genericType = ((ParameterizedType) type).getActualTypeArguments()[0];
                return unmarshallList(byteList, genericType);
            case "java.util.Date":
                return unmarshallDate(byteList);
            default:
                return unmarshallObject(byteList, (Class<?>) type);
        }
    }

    private static void marshallSelect(String[] typeWithGeneric, Object o, List<Byte> res) {

        switch (typeWithGeneric[0]) {
            case "java.lang.String":
                marshallString(o, res);
                break;
            case "short":
            case "java.lang.Short":
                marshallShort(o, res);
                break;
            case "int":
            case "java.lang.Integer":
                marshallInt(o, res);
                break;
            case "float":
            case "java.lang.Float":
                marshallFloat(o, res);
                break;
            case "double":
            case "java.lang.Double":
                marshallDouble(o, res);
                break;
            case "java.util.List":
                marshallList(o, Arrays.copyOfRange(typeWithGeneric,1,typeWithGeneric.length), res);
                break;
            case "java.util.Date":
                marshallDate(o, res);
                break;
            default:
                marshallObject(o, res);
                break;
        }

    }

    private static <T> List<T> unmarshallList(List<Byte> byteList, Type genericType) {
        List<T> list = new ArrayList<>();
        int size = shortFromByteList(byteList);

        for (int i = 0; i < size; i++) {
            T obj = (T) unmarshallSelect(byteList, genericType);
            list.add(obj);
        }

        return list;
    }

    private static void marshallList(Object o, String[] typeWithGeneric, List<Byte> res) {

        List<?> list = (List<?>) o;
        if (list.size() > Short.MAX_VALUE) {
            logger.error("List length is too long, limit it to {}", Short.MAX_VALUE);
        }
        res.addAll(shortToByteList((short) list.size()));

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

    private static short unmarshallShort(List<Byte> byteList) {
        return shortFromByteList(byteList);
    }

    private static void marshallShort(Object o, List<Byte> res) {
        short i = (short) o;
        res.addAll(shortToByteList(i));
    }

    private static double unmarshallDouble(List<Byte> byteList) {
        return doubleFromByteList(byteList);
    }

    private static void marshallDouble(Object o, List<Byte> res) {
        double d = (double) o;
        res.addAll(doubleToByteList(d));
    }

    private static float unmarshallFloat(List<Byte> byteList) {
        return floatFromByteList(byteList);
    }

    private static void marshallFloat(Object o, List<Byte> res) {
        float d = (float) o;
        res.addAll(floatToByteList(d));
    }

    private static String unmarshallString(List<Byte> byteList) {

        int size = shortFromByteList(byteList);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append((char) byteList.remove(0).byteValue());
        }
        return builder.toString();
    }

    private static void marshallString(Object o, List<Byte> res) {
        String s = (String) o;
        if (s.length() > Short.MAX_VALUE) {
            logger.error("String value is too long, limit it to {}", Short.MAX_VALUE);
        }

        res.addAll(shortToByteList((short) s.length()));
        res.addAll(Bytes.asList(s.getBytes()));
    }

    private static Date unmarshallDate(List<Byte> byteList) {
        int epoch = unmarshallInt(byteList);
        return new Date((long) epoch * 1000);
    }

    private static void marshallDate(Object o, List<Byte> res) {
        Date date = (Date) o;
        int epoch = (int) (date.getTime() / 1000);
        res.addAll(intToByteList(epoch));
    }

    private static int intFromByteList(List<Byte> byteList) {
        byte[] intBytes = new byte[Integer.BYTES];
        for (int i = 0; i < Integer.BYTES; i++) {
            intBytes[i] = byteList.remove(0);
        }
        return ByteBuffer.wrap(intBytes).getInt();
    }

    private static List<Byte> shortToByteList(short v) {
        byte[] array = ByteBuffer.allocate(Short.BYTES).putShort(v).array();
        return Bytes.asList(array);
    }

    private static short shortFromByteList(List<Byte> byteList) {
        byte[] shortBytes = new byte[Short.BYTES];
        for (int i = 0; i < Short.BYTES; i++) {
            shortBytes[i] = byteList.remove(0);
        }
        return ByteBuffer.wrap(shortBytes).getShort();
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

    private static float floatFromByteList(List<Byte> byteList) {
        byte[] floatBytes = new byte[Float.BYTES];
        for (int i = 0; i < Float.BYTES; i++) {
            floatBytes[i] = byteList.remove(0);
        }
        return ByteBuffer.wrap(floatBytes).getFloat();
    }

    private static List<Byte> floatToByteList(float v) {
        byte[] array = ByteBuffer.allocate(Float.BYTES).putFloat(v).array();
        return Bytes.asList(array);
    }
}

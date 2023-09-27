package com.mpedy.beans.stix;

import com.google.gson.JsonObject;
import com.mpedy.beans.stix.sro.Relationship;
import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

/**
 *
 * @author matteo.provendola
 */
@ManagedBean(name = "utils")
@SessionScoped
public class Utils implements Serializable {

//    private static final Logger LOGGER = LogManager.getLogger(Utils.class);
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final static String GENERALOBJECT_PATH = URLDecoder.decode(GeneralObject.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8);
    private final static String SDO_PATH = GENERALOBJECT_PATH + "com\\mpedy\\beans\\stix\\sdo";
    private final static String SRO_PATH = GENERALOBJECT_PATH + "com\\mpedy\\beans\\stix\\sro";
    private final static String SCO_PATH = GENERALOBJECT_PATH + "\\com\\mpedy\\beans\\stix\\sco";
    public final static String SDO_PREFIX = "sdo";
    public final static String SRO_PREFIX = "sro";
    public final static String SCO_PREFIX = "sco";
    public final static JaroWinklerSimilarity jaroWinklerSimilarity = new JaroWinklerSimilarity();

    public final static Map<String, String> classPathForTypes = new HashMap<>();

    public static void init() {
        System.out.println("Inizializzando Utils");
        List<String> sdo = Arrays.asList(getAllSDOClassNames());
        List<String> sco = Arrays.asList(getAllSCOClassNames());
        List<String> sro = Arrays.asList(getAllSROClassNames());
        JsonObject jsonArr = new JsonObject();
        for (String s : sdo) {
            s = s.replaceAll("\\.class", "");
            GeneralObject as = buildFromClassName(s, SDO_PREFIX);
            classPathForTypes.put(as.getType(), s);
            JsonObject jsobj = new JsonObject();
            List<Field> fields = getAllFieldFromClass(as.getClass());
            for (Field f : fields) {
                jsobj.addProperty(f.getName(), f.getType().getName());
            }
            jsonArr.add(s, jsobj);
        }
        for (String s : sco) {
            s = s.replaceAll("\\.class", "");
            GeneralObject as = buildFromClassName(s, SCO_PREFIX);
            classPathForTypes.put(as.getType(), s);
            JsonObject jsobj = new JsonObject();
            List<Field> fields = getAllFieldFromClass(as.getClass());
            for (Field f : fields) {
                jsobj.addProperty(f.getName(), f.getType().getName());
            }
            jsonArr.add(s, jsobj);
        }
        for (String s : sro) {
            s = s.replaceAll("\\.class", "");
            Relationship as = buildFromClassName(s, SRO_PREFIX);
            classPathForTypes.put(as.getType(), s);
            JsonObject jsobj = new JsonObject();
            List<Field> fields = getAllFieldFromClass(as.getClass());
            for (Field f : fields) {
                jsobj.addProperty(f.getName(), f.getType().getName());
            }
            jsonArr.add(s, jsobj);
        }
        System.out.println("Questo è il json finale:");
        System.out.println(jsonArr.toString());
    }

    public static String[] getAllSDOClassNames() {
        File f = new File(SDO_PATH);
        FilenameFilter ffilter;
        ffilter = (File dir, String name) -> name.endsWith(".class");
        String[] fl = f.list(ffilter);
        Arrays.sort(fl);
        return fl;
    }

    public static String[] getAllSDOClassNames(Function<String, Boolean> customFilter) {
        if (customFilter != null) {
            File f = new File(SDO_PATH);
            FilenameFilter ffilter;
            ffilter = (File dir, String name) -> name.endsWith(".class") & customFilter.apply(name);
            String[] fl = f.list(ffilter);
            Arrays.sort(fl);
            return fl;
        }
        return getAllSDOClassNames();
    }

    public static String[] getAllSROClassNames() {
        File f = new File(SRO_PATH);
        FilenameFilter ffilter;
        ffilter = (File dir, String name) -> name.endsWith(".class");
        String[] fl = f.list(ffilter);
        Arrays.sort(fl);
        return fl;
    }

    public static String[] getAllSROClassNames(Function<String, Boolean> customFilter) {
        if (customFilter != null) {
            File f = new File(SRO_PATH);
            FilenameFilter ffilter;
            ffilter = (File dir, String name) -> name.endsWith(".class") & customFilter.apply(name);
            String[] fl = f.list(ffilter);
            Arrays.sort(fl);
            return fl;
        }
        return getAllSDOClassNames();
    }

    public static String[] getAllSCOClassNames() {
        File f = new File(SCO_PATH);
        FilenameFilter ffilter;
        ffilter = (File dir, String name) -> name.endsWith(".class");
        String[] fl = f.list(ffilter);
        Arrays.sort(fl);
        return fl;
    }

    public static String[] getAllSCOClassNames(Function<String, Boolean> customFilter) {
        if (customFilter != null) {
            File f = new File(SCO_PATH);
            FilenameFilter ffilter;
            ffilter = (File dir, String name) -> name.endsWith(".class") & customFilter.apply(name);
            String[] fl = f.list(ffilter);
            Arrays.sort(fl);
            return fl;
        }
        return getAllSCOClassNames();
    }

    public static <T> T buildFromClassName(String className, String prefix) {
        try {
            Class<?> cls1 = Class.forName("com.mpedy.beans.stix." + prefix + "." + className);
            return (T) cls1.getConstructors()[0].newInstance();
        } catch (Exception e) {
//            System.out.println("(2) - Errore con la classe " + className + ": " + e.toString());
            return null;
        }
    }

    public static <T> T buildFromClassName(String absoluteClassName) {
        try {
            Class<?> cls1 = Class.forName(absoluteClassName);
            return (T) cls1.getConstructors()[0].newInstance();
        } catch (Exception e) {

        }
        T t = buildFromClassName(absoluteClassName, SDO_PREFIX);
        if (t != null) {
            return t;
        }
        t = buildFromClassName(absoluteClassName, SCO_PREFIX);
        if (t != null) {
            return t;
        }
        t = buildFromClassName(absoluteClassName, SRO_PREFIX);
        if (t != null) {
            return t;
        }
        if (classPathForTypes == null || classPathForTypes.isEmpty()) {
            init();
        }
        if (classPathForTypes.containsKey(absoluteClassName)) {
            return Utils.buildFromClassName(classPathForTypes.get(absoluteClassName));
        }
        System.out.println("Errore con la classe " + absoluteClassName);
        return null;
    }

    public static <T> T buildFromType(String type) {
        if (classPathForTypes == null || classPathForTypes.isEmpty()) {
            init();
        }
        if (classPathForTypes.containsKey(type)) {
            return Utils.buildFromClassName(classPathForTypes.get(type));
        }
        System.out.println("(3) - Errore con la classe " + type);
        return null;
    }

    public static String getActualDateTime_str() {
        LocalDateTime localDateTime = LocalDateTime.now();

        // Aggiungi il fuso orario UTC (Z) alla data e ora corrente
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

        // Formatta la data e l'ora nel formato desiderato
        String formattedDateTime = zonedDateTime.format(Utils.dateFormatter);
        return formattedDateTime;
    }

    public static LocalDateTime getActualDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();

        // Aggiungi il fuso orario UTC (Z) alla data e ora corrente
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);
        return zonedDateTime.toLocalDateTime();
    }

    public static List<Field> getAllFieldFromClass(Class<?> clazz) {
        return Stream.concat(
                Arrays.asList(clazz.getDeclaredFields())
                        .stream()
                        .filter(f -> {
                            NoInput noInput = f.getAnnotation(NoInput.class);
                            return !(noInput != null && noInput.value());
                        }),
                Arrays.asList(clazz.getSuperclass().getDeclaredFields())
                        .stream()
                        .filter(f -> {
                            NoInput noInput = f.getAnnotation(NoInput.class);
                            return !(noInput != null && noInput.value());
                        })).collect(Collectors.toList()
        );
    }

    public static List<Field> getAllFieldFromClassForUI(Class<?> clazz) {
        return Stream.concat(
                Arrays.asList(clazz.getDeclaredFields())
                        .stream()
                        .filter(f -> {
                            NoPrint noPrint = f.getAnnotation(NoPrint.class);
                            return !(noPrint != null && noPrint.value());
                        }),
                Arrays.asList(clazz.getSuperclass().getDeclaredFields())
                        .stream()
                        .filter(f -> {
                            NoPrint noPrint = f.getAnnotation(NoPrint.class);
                            return !(noPrint != null && noPrint.value());
                        })).collect(Collectors.toList()
        );
    }

    public static List<Field> getAllFieldFromClass(Class<?> clazz, Predicate<Field> pred) {
        return Stream.concat(
                Arrays.asList(clazz.getDeclaredFields())
                        .stream()
                        .filter(pred),
                Arrays.asList(clazz.getSuperclass().getDeclaredFields())
                        .stream()
                        .filter(pred)).collect(Collectors.toList()
        );
    }

    public static String getTypeForLabel(String type) {
        StringBuilder result = new StringBuilder("");
        Boolean first = true;
        for (String t : type.replaceAll("-", " ").split(" ")) {
            if (!first) {
                result.append(" ");
            }
            result.append(t.substring(0, 1).toUpperCase());
            if (first) {
                first = false;
            }
            result.append(t.substring(1));
        }
        return result.toString();
    }

    public static LocalDateTime parseTime(String time, List<String> formats) {
        LocalDateTime ldt = null;
        for (String format : formats) {
            try {
                ldt = new SimpleDateFormat(format)
                        .parse(time)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                if (ldt != null) {
                    return ldt;
                }
            } catch (Exception e) {
                ldt = null;
            }
        }
        return ldt;
    }

    public static LocalDateTime parseTime(String time, String format) {
        try {
            return new SimpleDateFormat(format)
                    .parse(time)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            System.out.println("Errore nel parsare il testo in data: " + time + "  -  " + format);
        }
        return null;
    }

    public static LocalDateTime parseTimeFromString(String time) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})\\.(\\d+)");
        Matcher matcher = pattern.matcher(time);
        if (matcher.find()) {
            String datePart = matcher.group(1);
            String millisecondPart = matcher.group(2);

            // Aggiungi zeri mancanti nei millisecondi
            while (millisecondPart.length() < 3) {
                millisecondPart += "0";
            }
            if (millisecondPart.length() > 3) {
                millisecondPart = millisecondPart.substring(0, 3);
            }
            String formattedDateTime = datePart + "." + millisecondPart;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);
            return dateTime;
        } else {
            System.out.println("Formato non valido");
        }
        return null;
    }

    public static Float compareObj(Object r, Object l) {
        Float result = 0.0f;
        assert r.getClass().equals(l.getClass()) : "Classes dont match again";
        if (r instanceof String) {
            result += jaroWinklerSimilarity.apply(r.toString(), l.toString()).floatValue();
        } else if (r instanceof Boolean || r instanceof Integer) {
            result += r.equals(l) ? 1 : 0;
        } else if (r instanceof LocalDateTime) {
            LocalDateTime t1 = (LocalDateTime) r;
            LocalDateTime t2 = (LocalDateTime) l;
            if (t2.isAfter(t1.plusMinutes(2)) || t2.isBefore(t1.minusMinutes(2))) {
                result += 1;
            }
        }
        return result;
    }

    public static MatchResult compareObj(Object r, Object l, Float weight) {
        if (r == null && l == null) {
            return new MatchResult();
        }
        if ((r == null && l != null) || (r != null && l == null)) {
            return new MatchResult(0.0f, weight);
        }
        MatchResult result = new MatchResult();
        assert r.getClass().equals(l.getClass()) : "Classes dont match again";
        if (r.getClass().isEnum()) {
            try {
                r = r.getClass().getMethod("getValue").invoke(r);
                l = l.getClass().getMethod("getValue").invoke(l);
            } catch (Exception e) {
                System.out.println("Errore in compareObj: " + e.toString());
            }
        }
        if (r instanceof String) {
            result.setResult(jaroWinklerSimilarity.apply(r.toString(), l.toString()).floatValue());
        } else if (r instanceof Boolean || r instanceof Integer) {
            if (r.equals(l)) {
                result.setResult(1.0f);
            } else {
                result.setResult(0.0f);
            }
        } else if (r instanceof LocalDateTime) {
            LocalDateTime t1 = (LocalDateTime) r;
            LocalDateTime t2 = (LocalDateTime) l;
            if (t2.isAfter(t1.plusMinutes(2)) || t2.isBefore(t1.minusMinutes(2))) {
                result.setResult(1.0f);
            }
        }
        result.setComparisons(weight);
        result.setResult(result.getResult() * weight);
        return result;
    }

}

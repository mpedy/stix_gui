package com.mpedy.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.mpedy.beans.SessionManager;
//import com.mpedy.enumeration.KpiType;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author Matteo
 */
public class Utils {

    static FacesContext context = FacesContext.getCurrentInstance();
    static ResourceBundle bundle = context.getApplication().getResourceBundle(context, "txt");
    static final transient org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Utils.class.getName());
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String ROOTPATH = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/";
//    @ManagedProperty("#{sessionManager}")
//    private static SessionManager sessionManager = null;
//
//    public SessionManager getSessionManager() {
//        return sessionManager;
//    }

    public static void setSessionTenant(Session session) {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String schema = (String) sessionMap.get("schema");
        if (schema == null) {
//            SessionManager sessionManager = null;
//            try {
//                sessionManager = context.getApplication().evaluateExpressionGet(context, "#{sessionManager}", SessionManager.class);
//            } catch (Exception e) {
//                LOGGER.error("Errore nel caricare la variabile sessionManager: " + e);
//            }
//            if (sessionManager != null) {
//                LOGGER.info("sessionMap.get(schema) = null && sessionManager != null");
//                schema = sessionManager.getTenant();
//                session.createSQLQuery("SET search_path TO " + schema).executeUpdate();
//            } else {
                LOGGER.info("sessionManager = null - Faccio Logout");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();//remove("user");
                try {
                    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                    FacesContext.getCurrentInstance().getExternalContext().redirect(ROOTPATH + "login.xhtml");
                } catch (IOException ioe) {
                    LOGGER.error("Errore Utils.setSessionTenant() redirect: " + ioe);
                }
            }
//        } else {
//            session.createSQLQuery("SET search_path TO " + schema).executeUpdate();
//        }
    }

    public static String getPath() {
        String tmp = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("origin");
        return tmp;
    }

    public static String convertValue(Object o, int type) {
        String res = null;

        try {
            double db = (double) o;
            switch (type) {
                case 1:
                    res = String.valueOf((int) db);
                    break;
                case 2:
                case 3:
                    //funzione che controlla la cifra decimale
                    // da fare
                    res = String.format("%.1f", db);
                    break;
                case 4:
                    res = tFormat((int) db);
                    break;
                case 5:
                    res = String.format("%.1f", db) + "%";
                    break;
                default:
                    res = String.valueOf(db);
            }
        } catch (Exception e) {
            res = o.toString();
        }
        return res;
    }

    public static String tFormat(double t) { // secondi

        String formatted = null;

        int hours = (int) t / 3600;
        int remainder = (int) t - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        String sHours = String.valueOf(hours);
        String sMins = String.format("%02d", mins);
        String sSecs = String.format("%02d", secs);
        sHours = (sHours.length() == 1 ? "0" + sHours : sHours);
        formatted = sHours + ":" + sMins + ":" + sSecs;

        return formatted;
    }

    public static String tFormat(int secondsCount) {
        int seconds = secondsCount % 60;
        secondsCount -= seconds;
        int minutesCount = secondsCount / 60;
        int minutes = minutesCount % 60;
        minutesCount -= minutes;
        int hoursCount = minutesCount / 60;
        int hours = hoursCount;
        String sHours = String.format("%02d", hours);
        String sMins = String.format("%02d", minutes);
        String sSecs = String.format("%02d", seconds);
        sHours = (sHours.length() == 1 ? "0" + sHours : sHours);
        String result = sHours + ":" + sMins + ":" + sSecs;
//        if (hoursCount > 24) {
//            int days = (int) (milliseconds / (1000 * 60 * 60 * 24));
//            String sDays = String.valueOf(days);
//            result = sDays + "g:" + sHours + "h";
//        }
        return result;
    }

    public static String tFormatNoSec(int secondsCount) {
        int seconds = secondsCount % 60;
        secondsCount -= seconds;
        int minutesCount = secondsCount / 60;
        int minutes = minutesCount % 60;
        minutesCount -= minutes;
        int hoursCount = minutesCount / 60;
        int hours = hoursCount;
        String sHours = String.format("%02d", hours);
        String sMins = String.format("%02d", minutes);
        String result = sHours + ":" + sMins;
//        if (hoursCount > 24) {
//            int days = (int) (milliseconds / (1000 * 60 * 60 * 24));
//            String sDays = String.valueOf(days);
//            result = sDays + "g:" + sHours + "h";
//        }
        return result;
    }

    public static int getMinutesFromSeconds(int secondsCount) {
        int seconds = secondsCount % 60;
        secondsCount -= seconds;
        int minutesCount = secondsCount / 60;
        return minutesCount;
    }

    public static String getPercValue(BigDecimal value) {
        String result;
        float val = value.floatValue() * 100;
        if (val % 1 == 0) {
            result = String.valueOf((int) val);
        } else {
            result = String.format("%.2f", val);
        }
        return result + "%";
    }

    public static String getPercValue(double value) {
        String result;
//        float val = value.floatValue() * 100;
        if (value % 1 == 0) {
            result = String.valueOf((int) value);
        } else {
            result = String.format("%.2f", value);
        }
        return result + "%";
    }

    public static String checkDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(value);
        return result;
    }

    public static String checkDecimalSingle(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        String result = df.format(value).replace(".", ",");
        if (!result.contains(",") && !result.equals("0") && !result.equals("100")) {
            result += ",0";
        }
        return result;
    }

    public static boolean checkPassword(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            messageError(bundle.getString("message.pwConfirmKO"));
            return false;
        }
        if (!pwMatches(newPassword)) {
            messageError(bundle.getString("message.invalidFormat"));
            return false;
        }
        return true;
    }

    public static boolean pwMatches(String pw) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String pattern = ctx.getExternalContext().getInitParameter("pwPattern");
        return pw.matches(pattern);
    }

    public static boolean emailMatches(String email) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String pattern = ctx.getExternalContext().getInitParameter("emailPattern");
        if (!email.matches(pattern)) {
            messageError(bundle.getString("message.invalidEmail"));
            return false;
        }
        return true;
    }

//    public static boolean checkInput(User user, boolean requiredTelephone, String newPassword, String confirmPassword) {
//        if ((requiredTelephone && user.getTelnumber().isEmpty()) || user.getUserid().isEmpty()
//                || user.getFirstname().isEmpty() || user.getLastname().isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
//            messageError(bundle.getString("message.required"));
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean checkInput(User user, boolean requiredTelephone) {
//        if ((requiredTelephone && user.getTelnumber().isEmpty()) || user.getEmail().isEmpty() || user.getUserid().isEmpty()
//                || user.getFirstname().isEmpty() || user.getLastname().isEmpty()) {
//            messageError(bundle.getString("message.required"));
//            return false;
//        }
//        return true;
//    }

    public static String encrypt(String input) {
        String md5 = null;

        if (null == input) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error in encrypt: " + e.getMessage());
        }
        return md5;
    }

    public static void messagePopup(String title, String message, FacesMessage.Severity sev) {
        //RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(sev,title,message));
        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(sev, title, message));
    }

    /**
     * Accetta anche codice html.<br>Aggiunge automaticamente
     * <b>FacesMessage.SEVERITY_WARN</b> come icona del popup.
     *
     * @param title titolo del popup
     * @param message messaggio del popup
     *
     */
    public static void messagePopup(String title, String message) {
        messagePopup(title, message, FacesMessage.SEVERITY_WARN);
    }

    public static void messageInfo(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    public static void messageInfo(String id, String message) {
        FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    public static void messageInfo(String id, String message, String detail) {
        FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, message, detail));
    }

    public static void messageError(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public static void messageError(String id, String message) {
        FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public static void messageError(String id, String message, String detail) {
        FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail));
    }

    public static String getLastTimerefOfDay() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(IntervalBoundaries.getStartOfHalfH(new DateTime().minusMinutes(30)).getMillis()));
    }

    public static int stringCompare(String str1, String str2) {

        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);

        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int) str1.charAt(i);
            int str2_ch = (int) str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }

        // Edge case for strings like 
        // String 1="Geeks" and String 2="Geeksforgeeks" 
        if (l1 != l2) {
            return l1 - l2;
        } // If none of the above conditions is true, 
        // it implies both the strings are equal 
        else {
            return 0;
        }
    }

    public static int timeToSeconds(String time) {
        String arr[] = time.split(":");
        long res = 0;
        if (arr.length == 2) {
            for (int i = 0; i < arr.length; i++) {
                long tmp = 0;
                if (i == 0) {
                    tmp = Long.valueOf(arr[i].replace("g", ""));
                    tmp *= 86400;
                } else if (i == 1) {
                    tmp = Long.valueOf(arr[i].replace("h", ""));
                    tmp *= 3600;
                }
                res += tmp;
            }
        } else if (arr.length == 3) {
            for (int i = 0; i < arr.length; i++) {
                long tmp = Long.valueOf(arr[i]);
                if (i == 0) {
                    tmp *= 3600;
                } else if (i == 1) {
                    tmp *= 60;
                }
                res += tmp;
            }
        }
        return (int) res;
    }

    public static String format(Duration duration) {
        long hours = duration.toHours();
        long mins = duration.minusHours(hours).toMinutes();
        long seconds = duration.getSeconds() - mins * 60 - hours * 60 * 60;
        return String.format("%02d:%02d:%02d", hours, mins, seconds);
    }

    public static Integer getDayOfMonth(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static float divide(float a, int b) {
        if (b == 0) {
            return 0;
        }
        return a / b;
    }

    public static int divide(int a, int b) {
        if (b == 0) {
            return 0;
        }
        float tmp = (float) a / b;
        return Math.round(tmp);
    }

    public static int divide(long a, int b) {
        if (b == 0) {
            return 0;
        }
        float tmp = (float) a / b;
        return Math.round(tmp);
    }

    public static String generatePassword(int lenght) {
        StringBuilder builder = new StringBuilder();
        while (lenght-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        if (!pwMatches(builder.toString())) {
            generatePassword(lenght);
        }
        return builder.toString();
    }

//    public static String generatePassword(int lenght) {
//        int leftLimit = 48; // numeral '0'
//        int rightLimit = 122; // letter 'z'
//        int targetStringLength = lenght;
//        Random random = new Random();
//        String generatedString = random.ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
//        return generatedString;
//    }
    public static <E> List<E> addToResult(List<E> A, List<E> B, BiPredicate<E, E> p) {
        for (E b : B) {
            Boolean found = false;
            for (E a : A) {
                if (p.test(a, b)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                A.add(b);
            }
        }
        return A;
    }

    public static int getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getDifferenceDays(DateTime d1, DateTime d2) {
        long diff = d2.getMillis() - d1.getMillis();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getDifferenceHours(DateTime d1, DateTime d2) {
        long diff = d2.getMillis() - d1.getMillis();
        return (int) TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getDifferenceMinutes(DateTime d1, DateTime d2) {
        long diff = d2.getMillis() - d1.getMillis();
        return (int) TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static boolean isBusinessDay(DateTime dt) {
        return !isWeekEnd(dt) && !isHoliday(dt);
    }

    public static boolean isWeekEnd(DateTime dt) {
        return dt.getDayOfWeek() == 6 || dt.getDayOfWeek() == 7;
    }

    public static boolean isHoliday(DateTime dt) {
        if (dt.getMonthOfYear() == 1 && dt.getDayOfMonth() == 1) { // Capodanno
            return true;
        }
        if (dt.getMonthOfYear() == 1 && dt.getDayOfMonth() == 6) { // Epifania
            return true;
        }
        if (dt.getMonthOfYear() == 4 && dt.getDayOfMonth() == 25) { // Festa della liberazione
            return true;
        }
        if (dt.getMonthOfYear() == 5 && dt.getDayOfMonth() == 1) { // Festa dei lavoratori
            return true;
        }
        if (dt.getMonthOfYear() == 6 && dt.getDayOfMonth() == 2) { // Festa della Repubblica italiana
            return true;
        }
        if (dt.getMonthOfYear() == 8 && dt.getDayOfMonth() == 15) { // Ferragosto
            return true;
        }
        if (dt.getMonthOfYear() == 11 && dt.getDayOfMonth() == 1) { // Giorno dei Santi
            return true;
        }
        if (dt.getMonthOfYear() == 12 && dt.getDayOfMonth() == 8) { // Immacolata concezione
            return true;
        }
        if (dt.getMonthOfYear() == 12 && dt.getDayOfMonth() == 25) { // Natale
            return true;
        }
        if (dt.getMonthOfYear() == 12 && dt.getDayOfMonth() == 26) { // Santo Stefano
            return true;
        }
        DateTime easterSundayDate = getEasterSundayDate(dt.getYear());
        if (dt.getMonthOfYear() == easterSundayDate.getMonthOfYear() && dt.getDayOfMonth() == easterSundayDate.getDayOfMonth()) { // Pasquetta
            return true;
        }
        return false;
    }

    private static DateTime getEasterSundayDate(int year) {
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                n = (h - m + r + 90) / 25,
                p = (h - m + r + n + 19) % 32;
        return new DateTime(year, n, p, 0, 0);
    }

    public static String getRandomString(int lenght) {
        StringBuilder builder = new StringBuilder();
        while (lenght-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static JsonObject toJsonObject(JSONObject obj) {
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(obj.toString());
    }

    public static Map<String, String> readObj(JsonObject jsonObject) {
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        Map<String, String> keyValueMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            if (jsonObject.get(entry.getKey()).isJsonPrimitive()) {
                keyValueMap.put(entry.getKey(), jsonObject.get(entry.getKey()).getAsString());
            }
        }
        return keyValueMap;
    }

    public static boolean containsIgnoreCase(List<String> list, String elem) {
        for (String current : list) {
            if (current.equalsIgnoreCase(elem)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean compareList(List<T> a, List<T> b) {
        for (T t : a) {
            if (!b.contains(t)) {
                return false;
            }
        }
        for (T t : b) {
            if (!a.contains(t)) {
                return false;
            }
        }
        return true;
    }
}

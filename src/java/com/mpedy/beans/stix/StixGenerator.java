package com.mpedy.beans.stix;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mpedy.beans.stix.comparison.StixComparator;
import com.mpedy.beans.stix.sdo.AttackPattern;
import com.mpedy.beans.stix.sdo.Grouping;
import com.mpedy.beans.stix.sro.Relationship;
import com.mpedy.beans.stix.vocabulary.GroupingContextOv;
import com.mpedy.beans.stix.vocabulary.RELATIONSHIP_TYPE;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author cuore
 */
@ManagedBean(name = "stixGenerator")
@ViewScoped
public class StixGenerator implements Serializable {

    public GeneralObject sdo_choosen = new GeneralObject();
    private Relationship sro_choosen = new Relationship();
    private List<GeneralObject> allsdo;
    private List<GeneralObject> sdolist;
    private List<Relationship> allsro;
    private List<Relationship> srolist;
    private GeneralObject selectedObject;
    private GeneralObject selectedObject_left;
    private GeneralObject selectedObject_right;
    private LinkedHashMap<String, FieldVAL> fieldMap;
    private boolean isShowing;
    private boolean isDrawing;
    private String typeOfRelationship;
    private String jsonData = "";
    private String jsonFile = "";
    private String jsonFile1 = "";
    private String jsonFile2 = "";
    private String rel_timestamp1;
    private String rel_timestamp2;
    /*private List<String> typesOfRelationship = Arrays.asList(
            "analysis of",
            "attributed to",
            "authored by",
            "av analysis of",
            "based on",
            "beacons to",
            "belongs to",
            "characterizes",
            "communicates with",
            "compromises",
            "consists of",
            "controls",
            "created by",
            "delivers",
            "derived from",
            "downloads",
            "drops",
            "duplicate of",
            "dynamic analysis of",
            "exfiltrates to",
            "exploits",
            "has",
            "hosts",
            "impersonates",
            "indicates",
            "investigates",
            "located at",
            "mitigates",
            "originates from",
            "owns",
            "related to",
            "remediates",
            "resolves to",
            "static analysis of",
            "targets",
            "uses",
            "variant of");
     */
    private List<String> typesOfRelationship = Arrays.asList(
            RELATIONSHIP_TYPE.HAS,
            RELATIONSHIP_TYPE.TARGETS,
            RELATIONSHIP_TYPE.USES,
            RELATIONSHIP_TYPE.FROM,
            RELATIONSHIP_TYPE.IS_FOLLOWED_BY
    );
    private List<GeneralObject> allsdo_for_attrs;
    private Bundle bundle;
    private Float comparisonResult = 0.0f;
    private String jsCommand1 = "";
    private String jsCommand2 = "";

    @PostConstruct
    public void init() {
        allsdo = new ArrayList<>();
        allsdo_for_attrs = new ArrayList<>();
        sdolist = new ArrayList<>();
        allsro = new ArrayList<>();
        srolist = new ArrayList<>();
        fieldMap = new LinkedHashMap<>();
        isShowing = false;
        isDrawing = false;
        bundle = new Bundle();
        bundle.setObjects(sdolist);
        bundle.setRels(srolist);
//        test();
//        test1();
        try {
            String[] SDOClassNames = Utils.getAllSDOClassNames();
            for (String className : SDOClassNames) {
                className = className.replaceAll("\\.class", "");
                GeneralObject gObj = (GeneralObject) Utils.buildFromClassName(className, Utils.SDO_PREFIX);
                allsdo.add(gObj);
                ForAttributes isOkForAttributes = gObj.getClass().getAnnotation(ForAttributes.class);
                if (isOkForAttributes != null && isOkForAttributes.value()) {
                    allsdo_for_attrs.add(gObj);
                }
            }
            String[] SCOClassNames = Utils.getAllSCOClassNames();
            for (String className : SCOClassNames) {
                className = className.replaceAll("\\.class", "");
                GeneralObject gObj = (GeneralObject) Utils.buildFromClassName(className, Utils.SCO_PREFIX);
                allsdo.add(gObj);
                ForAttributes isOkForAttributes = gObj.getClass().getAnnotation(ForAttributes.class);
                if (isOkForAttributes != null && isOkForAttributes.value()) {
                    allsdo_for_attrs.add(gObj);
                }

            }
            String[] SROClassNames = Utils.getAllSROClassNames();
            for (String className : SROClassNames) {
                className = className.replaceAll("\\.class", "");
                allsro.add((Relationship) Utils.buildFromClassName(className, Utils.SRO_PREFIX));
            }
            allsdo.sort((GeneralObject o1, GeneralObject o2) -> o1.getTypeForLabel().compareToIgnoreCase(o2.getTypeForLabel()));
        } catch (Exception ex) {
            System.out.println("Errore in init: " + ex.toString());
            ex.printStackTrace(System.out);
        }
    }

    public void test1() {
        String jsonStr = "{\"created_time\": \"2023-09-14T14:53:15.711687400\",\"mime_type\": \"jpeg\",\"created\": \"2023-09-14T14:53:15.711Z\",\"name\": \"\",\"description\": \"\",\"id\": \"artifact--ec813870-ce65-4c82-a0cb-aab04588cb5f\",\"type\": \"artifact\",\"url\": \"ciccio\"}";
        JsonObject json = new JsonParser().parse(jsonStr).getAsJsonObject();
//        jsonStr = jsonStr.replaceAll("\\s*\\\\", " ");
        GeneralObject obj = Utils.buildFromType(json.get("type").getAsString());
        obj.parse(json);
        sdolist.add(obj);
        System.out.println(obj.getId().equals("artifact--ec813870-ce65-4c82-a0cb-aab04588cb5f"));
        System.out.println(obj.toGeneralString());
    }

    public void test() {
        AttackPattern atk = new AttackPattern();
        atk.setName("Attacco 1");
        atk.setDescription("Descrizione dell'attacco 1");
        Grouping gr = new Grouping();
        gr.setName("gruppo 1");
        gr.setDescription("Descrizione del grouping 1");
        gr.setContext(GroupingContextOv.MALWARE_ANALYSIS);
        Relationship rel = new Relationship();
        rel.setRelationship_type("uses");
        rel.setLeft(atk);
        rel.setRight(gr);
        atk.getRels().add(rel);
        gr.getRels().add(rel);
        sdolist.add(atk);
        sdolist.add(gr);
        srolist.add(rel);
    }

    public void update(String id) {
        PrimeFaces.current().ajax().update(id);
    }

    public void clearRelVar() {
        this.typeOfRelationship = null;
        this.selectedObject_left = null;
        this.selectedObject_right = null;
    }

    public void selectedElement(Integer scelta, Integer index) {
        Attributes sel = selectedObject.getAttrs().get(index);
        if (scelta == 1) {
            sel.setObj_existing(null);

        } else if (scelta == 2) {
            sel.setObj(null);
        }
        sel = ((ArrayList<Attributes>) fieldMap.get("attrs").value).get(index);
        if (scelta == 1) {
            sel.setObj_existing(null);

        } else if (scelta == 2) {
            sel.setObj(null);
        }
        PrimeFaces.current().ajax().update("SDODialogDataForm");
    }

    public void deleteAttr(Integer index) {
        selectedObject.getAttrs().remove(index);
        if (fieldMap.get("attrs").value.getClass().equals(ArrayList.class)) {
            try {
                fieldMap.get("attrs").value.getClass().getMethod("remove", java.lang.Integer.TYPE).invoke(fieldMap.get("attrs").value, index);
            } catch (Exception e) {
                System.out.println("Impossibile cancellare indice " + index + " dal fielmap.attrs: " + e.toString());
            }
        }
    }

    public void saveRelData(String id_left, String id_right) {
        System.out.println("Dopo il salvataggio: " + id_left + "  -  " + id_right);
        System.out.println("REL: " + rel_timestamp1 + "-" + rel_timestamp2);
        List<GeneralObject> l = sdolist.stream().filter(a -> a.getId().equals(id_left)).collect(Collectors.toList());
        selectedObject_left = l.get(0);
        l = sdolist.stream().filter(a -> a.getId().equals(id_right)).collect(Collectors.toList());
        selectedObject_right = l.get(0);
        Relationship rel = new Relationship();
        rel.setLeft(selectedObject_left);
        rel.setRight(selectedObject_right);
        rel.setRelationship_type(typeOfRelationship);
        if (typeOfRelationship.equals("is followed by")) {
            rel.setSource_timeref(rel_timestamp1);
            rel.setTarget_timeref(rel_timestamp2);
            rel.setSource_timeref_t(Utils.parseTime(rel_timestamp1, "yyyy-MM-dd HH:mm:ss"));
            rel.setTarget_timeref_t(Utils.parseTime(rel_timestamp2, "yyyy-MM-dd HH:mm:ss"));
        }
        selectedObject_left.getRels().add(rel);
        selectedObject_right.getRels().add(rel);
        srolist.add(rel);
    }

    public void showSDOChosen() {
        System.out.println("IsShowing è : " + isShowing);
        if (!isShowing) {
            if (sdo_choosen != null) {
                sdolist.add(sdo_choosen);
            }
            sdo_choosen = null;
        }
    }

    public void showSROChosen() {
        srolist.add(sro_choosen);
        sro_choosen = null;
    }

    public void saveData() {
        try {
            for (Map.Entry<String, FieldVAL> entry : fieldMap.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue().getValue();
                Field field = null;
                try {
                    field = selectedObject.getClass().getDeclaredField(fieldName);
                } catch (Exception e) {
                    field = selectedObject.getClass().getSuperclass().getDeclaredField(fieldName);
                }
                field.setAccessible(true);
                fieldValue = convertValue(field.getType(), fieldValue);
                field.set(selectedObject, fieldValue);
            }
        } catch (Exception e) {
            System.out.println("Errore in saveData: " + e.toString());
        }

        try {
            if (selectedObject.getAttrs().size() > 0) {
                for (int i = 0; i < selectedObject.getAttrs().size(); i++) {
                    Attributes attr = selectedObject.getAttrs().get(i);
                    boolean shouldCreateObject = true;
                    boolean shouldCreateRel = true;
                    boolean isExisting = false;
                    GeneralObject gObj = attr.getObj();
                    if (gObj == null) {
                        gObj = attr.getObj_existing();
                        if (gObj == null) {
                            System.out.print("OGGETTO NULLO: " + attr.toString());
                            selectedObject.getAttrs().remove(i);
                            i--;
                            continue;
                        }
                        isExisting = true;
                    }
                    if (gObj.getOldId() == null) {
                        gObj.setOldId(gObj.getId());
                    }
                    if (attr.getOldId() == null) {
                        attr.setOldId(gObj.getId());
                    }
                    final String idgObj = gObj.getId();
                    List<GeneralObject> list_gObj = sdolist.stream().filter(sdo -> sdo.getId().equals(idgObj)).collect(Collectors.toList());
                    if (!list_gObj.isEmpty()) {
                        gObj = list_gObj.get(0);
                        shouldCreateObject = false;
                    }
//                    } else {
//                        if (!isExisting) {
//                            gObj.renovateID();
//                        }
//                    }
                    if (!isExisting) {
                        try {
                            gObj.getClass().getMethod("setValue", String.class).invoke(gObj, attr.getValue());
                        } catch (Exception e) {
                            gObj.getClass().getMethod("setName", String.class).invoke(gObj, attr.getValue());
                        }
                    }
                    Relationship rel = attr.getRel();
                    List<Relationship> list_rels = srolist.stream().filter(relatio -> relatio.getId().equals(attr.getRel().getId())).collect(Collectors.toList());
                    if (!list_rels.isEmpty()) {
                        rel = list_rels.get(0);
                        shouldCreateRel = false;
                    }
                    if (shouldCreateRel) {
                        rel.setRelationship_type(attr.getVerb());
                        rel.setLeft(selectedObject);
                        rel.setRight(gObj);
                        selectedObject.getRels().add(rel);
                        gObj.getRels().add(rel);
                        srolist.add(rel);
                    }
                    if (shouldCreateObject) {
                        sdolist.add(gObj);
                        attr.setObj_existing(gObj);
                        attr.setObj(null);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore qui: " + e.toString());
        }
        fieldMap = new LinkedHashMap<>();

    }

    public Object
            convertValue(Class<?> fieldType, Object value) {
        if (value != null) {
            if (fieldType == String.class) {
                return (String) value;
            } else if (fieldType == Integer.class) {
                return Integer.valueOf(
                        (String) value);
            } else if (fieldType == Boolean.class) {
                return Boolean.valueOf(
                        (String) value);
            } else if (fieldType.isEnum()) {
                try {
                    //                return Enum.valueOf(Utils.buildFromClassName(fieldType.getSimpleName(), "vocabulary"),value.toString());
                    return fieldType.getMethod("valueOf", String.class
                    ).invoke(null, value);
                } catch (Exception e) {
                    System.out.println("Could not parse Enum value: " + value);

                }
            } else if (fieldType == List.class) {
                return value;
            }
        }
        return null;
    }

    public void delete(GeneralObject obj) {
        System.out.println("Eliminando: " + obj + "  - " + sdolist.size() + " -- " + srolist.size());
        for (int i = 0; i < sdolist.size(); i++) {
            if (sdolist.get(i).getId().equals(obj.getId())) {
                sdolist.remove(i);
                break;
            }
        }
        if (!obj.getRels().isEmpty()) {
            for (Relationship rel : obj.getRels()) {
                for (int i = 0; i < srolist.size(); i++) {
                    if (srolist.get(i).getId().equals(rel.getId())) {
                        Relationship relRemoved = srolist.remove(i);
                        i--;
                        for (GeneralObject gObj : sdolist) {
                            if (gObj.getRels().contains(relRemoved)) {
                                gObj.getRels().remove(relRemoved);
                            }
                        }
                    }
                }
            }
        }
    }

    public void showINFO(String id) {
        fieldMap = new LinkedHashMap<>();
        isShowing = true;
        List<GeneralObject> list = sdolist.stream().filter(sdo -> sdo.getId().equals(id)).collect(Collectors.toList());
        if (list.size() != 1) {
            selectedObject = null;
        } else {
            selectedObject = list.get(0);
            List<Field> fields = Utils.getAllFieldFromClass(selectedObject.getClass());
            for (Field f1 : fields) {
                Object value = null;
                f1.setAccessible(true);
                try {
                    value = f1.get(selectedObject);
                } catch (Exception e) {
                    System.out.println("Errore in showInfo: " + e.toString());
                }
                String classSimpleName = f1.getType().getSimpleName();
                if (classSimpleName.toLowerCase().endsWith("ov")) {
                    try {
                        fieldMap.put(f1.getName(), new FieldVAL(value, 1, null));
                        List<Object> f2 = getListOfPossibleValues_3(f1.getType());
                        fieldMap.put(f1.getName(), new FieldVAL(value, 1, f2));
                    } catch (Exception e) {
                        System.out.println("Errore in showInfo: " + e.toString());
                    }
                } else {
                    fieldMap.put(f1.getName(), new FieldVAL(value, 0, null));
                }
            }
        }
    }

    public void produceJSON() {
        JSONObject element = new JSONObject();
        element.put("type", "bundle");
        element.put("id", "bundle--" + UUID.randomUUID());
        element.put("spec_version", "2.0");
        Predicate<Field> pred = f -> {
            NoJSON noJson = f.getAnnotation(NoJSON.class
            );
            return !(noJson != null && noJson.value());
        };
        JSONArray objects = new JSONArray();
        for (GeneralObject obj : sdolist) {
            JSONObject object = new JSONObject();
            List<Field> fields = Utils.getAllFieldFromClass(obj.getClass(), pred);
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    object.put(field.getName(), field.get(obj).toString());
                } catch (Exception e) {
                    object.put(field.getName(), "");
                }
            }
            objects.put(object);
        }
        for (Relationship rel : srolist) {
            JSONObject relationship = new JSONObject();
            List<Field> fields = Utils.getAllFieldFromClass(rel.getClass(), pred);
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    relationship.put(field.getName(), field.get(rel).toString());
                } catch (Exception e) {
                    relationship.put(field.getName(), "");
                }
            }
            objects.put(relationship);
        }
        element.put("objects", objects);
        System.out.println("JSON:\n\n");
        System.out.println(element.toString());
        jsonData = element.toString();
        String jsCommand = "var jsonData = " + jsonData + "; vizStixWrapper(jsonData, null, " + String.valueOf(!isDrawing).toLowerCase() + ", ['uploader','canvas-container','canvas']); document.getElementById('json_textarea_el').value = JSON.stringify(jsonData, null, 4);";
        isDrawing = true;
        //PrimeFaces.current().ajax().update("drawStix");
        PrimeFaces.current().executeScript(jsCommand);
    }

    public void closeDraw() {
        System.out.println("aaaa CLOSING DRAW");
        isDrawing = false;
        PrimeFaces.current().executeScript("toggleView();");
    }

    public void addAttributeTo(GeneralObject obj) {
        System.out.println("GENERAL OBBJ TO ADD ATTRIBUTES: " + obj.toString());
        Attributes attr = new Attributes();
        obj.getAttrs().add(attr);
        System.out.println(fieldMap.toString());
    }

    public List<Object> getListOfPossibleValues_1(Class<?> clazz) throws InvocationTargetException, IllegalArgumentException, SecurityException, NoSuchMethodException, IllegalAccessException {
        List<Object> f2 = Arrays.asList((Object[]) clazz.getDeclaredMethod("values").invoke(null));
        return f2;
    }

    public List<Object> getListOfPossibleValues_2(Class<?> clazz) throws Exception {
        Field f = clazz.getDeclaredField("$VALUES");
        f.setAccessible(true);
        List<Object> objects = Arrays.asList((Object[]) f.get(null));
        return objects;
    }

    public List<Object> getListOfPossibleValues_3(Class<?> clazz) throws Exception {
        List<Object> objects = Arrays.asList((Object[]) clazz.getEnumConstants());
        return objects;
    }

    public void compareJSONFile() {
        JsonObject json1 = null;
        JsonObject json2 = null;
        try {
            json1 = new JsonParser().parse(jsonFile1).getAsJsonObject();
            json2 = new JsonParser().parse(jsonFile2).getAsJsonObject();
        } catch (Exception e) {
            System.out.println("Errore nel parsing del file json: " + e.toString());
        }
        if (json1 != null && json2 != null) {
            assert json1.get("type").getAsString().equals("bundle") : "Not a Bundle type";
            assert json1.has("objects") : "Not has 'objects' JsonArray";
            assert json2.get("type").getAsString().equals("bundle") : "Not a Bundle type";
            assert json2.has("objects") : "Not has 'objects' JsonArray";
            JsonArray objects1 = json1.get("objects").getAsJsonArray();
            JsonArray objects2 = json2.get("objects").getAsJsonArray();
            List<GeneralObject> sdolist1 = new ArrayList<>();
            List<GeneralObject> sdolist2 = new ArrayList<>();
            List<Relationship> srolist1 = new ArrayList<>();
            List<Relationship> srolist2 = new ArrayList<>();
            for (JsonElement elem : objects1) {
                JsonObject object = elem.getAsJsonObject();
                try {
                    GeneralObject gObj = Utils.buildFromType(object.getAsJsonObject().get("type").getAsString());
                    gObj.parse(object);
                    sdolist1.add(gObj);
                } catch (Exception e) {
//                    System.out.println("Errore 1 in parse: " + e.toString());
                }
                try {
                    Relationship rel = Utils.buildFromType(object.getAsJsonObject().get("type").getAsString());
                    rel.parse(object);
                    srolist1.add(rel);
                } catch (Exception e) {
//                    System.out.println("Errore 2 in parse: " + e.toString());
                }
            }
            for (JsonElement elem : objects2) {
                JsonObject object = elem.getAsJsonObject();
                try {
                    GeneralObject gObj = Utils.buildFromType(object.getAsJsonObject().get("type").getAsString());
                    gObj.parse(object);
                    sdolist2.add(gObj);
                } catch (Exception e) {
//                    System.out.println("Errore 1 in parse: " + e.toString());
                }
                try {
                    Relationship rel = Utils.buildFromType(object.getAsJsonObject().get("type").getAsString());
                    rel.parse(object);
                    srolist2.add(rel);
                } catch (Exception e) {
//                    System.out.println("Errore 2 in parse: " + e.toString());
                }
            }

            for (Relationship rel : srolist1) {
                String leftId = rel.getSource_ref();
                String rightId = rel.getTarget_ref();
                for (GeneralObject gObj : sdolist1) {
                    if (leftId != null) {
                        if (gObj.getId().equals(leftId)) {
                            rel.setLeft(gObj);
                            gObj.getRels().add(rel);
                            leftId = null;
                        }
                    }
                    if (rightId != null) {
                        if (gObj.getId().equals(rightId)) {
                            rel.setRight(gObj);
                            gObj.getRels().add(rel);
                            rightId = null;
                        }
                    }
                    if (rightId == null && leftId == null) {
                        break;
                    }
                }
            }
            for (Relationship rel : srolist2) {
                String leftId = rel.getSource_ref();
                String rightId = rel.getTarget_ref();
                for (GeneralObject gObj : sdolist2) {
                    if (leftId != null) {
                        if (gObj.getId().equals(leftId)) {
                            rel.setLeft(gObj);
                            gObj.getRels().add(rel);
                            leftId = null;
                        }
                    }
                    if (rightId != null) {
                        if (gObj.getId().equals(rightId)) {
                            rel.setRight(gObj);
                            gObj.getRels().add(rel);
                            rightId = null;
                        }
                    }
                    if (rightId == null && leftId == null) {
                        break;
                    }
                }
            }
            Bundle bundle1 = new Bundle();
            Bundle bundle2 = new Bundle();
            bundle1.setObjects(sdolist1);
            bundle1.setRels(srolist1);
            bundle2.setObjects(sdolist2);
            bundle2.setRels(srolist2);
            StixComparator stixComparator = new StixComparator();
            Float result = stixComparator.compare(bundle1, bundle2);
            System.out.println("Risultato della comparison: " + result);
            comparisonResult = result;
            jsCommand1 = "var jsonData = " + jsonFile1 + "; vizStixWrapper(jsonData, null, " + String.valueOf(!isDrawing).toLowerCase() + ", ['uploader','canvas-container','canvas']); document.getElementById('json_textarea_el').value = JSON.stringify(jsonData, null, 4);";
            jsCommand2 = "var jsonData = " + jsonFile2 + "; vizStixWrapper(jsonData, null, " + String.valueOf(!isDrawing).toLowerCase() + ", ['uploader','canvas-container','canvas']); document.getElementById('json_textarea_el').value = JSON.stringify(jsonData, null, 4);";

        }

    }

    public void loadJSONFromFile() {
        JsonObject json = null;
        try {
            json = new JsonParser().parse(jsonFile).getAsJsonObject();
        } catch (Exception e) {
            System.out.println("Errore nel parsing del file json: " + e.toString());
        }
        if (json != null) {
            assert json.get("type").getAsString().equals("bundle") : "Not a Bundle type";
            assert json.has("objects") : "Not has 'objects' JsonArray";
            JsonArray objects = json.get("objects").getAsJsonArray();
            sdolist = new ArrayList<>();
            srolist = new ArrayList<>();
            for (JsonElement elem : objects) {
                JsonObject object = elem.getAsJsonObject();
                try {
                    GeneralObject gObj = Utils.buildFromType(object.getAsJsonObject().get("type").getAsString());
                    gObj.parse(object);
                    sdolist.add(gObj);
                    continue;
                } catch (Exception e) {
//                    System.out.println("Errore 1 in parse: " + e.toString());
                }
                try {
                    Relationship rel = Utils.buildFromType(object.getAsJsonObject().get("type").getAsString());
                    rel.parse(object);
                    srolist.add(rel);
                } catch (Exception e) {
                    System.out.println("Errore 2 in parse: " + e.toString());
                }
            }
            System.out.println("SDO LIST NUOVA: " + sdolist.size());
            System.out.println("SRO LIST NUOVA: " + srolist.size());
            for (Relationship rel : srolist) {
                String leftId = rel.getSource_ref();
                String rightId = rel.getTarget_ref();
                for (GeneralObject gObj : sdolist) {
                    if (leftId != null) {
                        if (gObj.getId().equals(leftId)) {
                            rel.setLeft(gObj);
                            gObj.getRels().add(rel);
                            leftId = null;
                        }
                    }
                    if (rightId != null) {
                        if (gObj.getId().equals(rightId)) {
                            rel.setRight(gObj);
                            gObj.getRels().add(rel);
                            rightId = null;
                        }
                    }
                    if (rightId == null && leftId == null) {
                        break;
                    }
                }
            }
            List<Relationship> check_left = srolist.stream().filter(r -> r.getLeft() == null).collect(Collectors.toList());
            List<Relationship> check_right = srolist.stream().filter(r -> r.getRight() == null).collect(Collectors.toList());
            assert check_left.isEmpty() : "There are Relationships with left = null";
            assert check_right.isEmpty() : "There are Relationships with right = null";
            if (!check_left.isEmpty()) {
                for (Relationship r : check_left) {
                    System.out.println("LEFT: " + r.toString());
                }
            }
            if (!check_right.isEmpty()) {
                for (Relationship r : check_right) {
                    System.out.println("Right: " + r.toString());
                }
            }
            bundle = new Bundle();
            bundle.setObjects(sdolist);
            bundle.setRels(srolist);
            StixComparator stixComparator = new StixComparator();
            Float result = stixComparator.compare(bundle, bundle);
            System.out.println("Risultato della comparison: " + result);
        }
    }

    public List<GeneralObject> getAllsdo() {
        return allsdo;
    }

    public void setAllsdo(List<GeneralObject> allsdo) {
        this.allsdo = allsdo;
    }

    public GeneralObject getSdo_choosen() {
        return sdo_choosen;
    }

    public void setSdo_choosen(GeneralObject sdo_choosen) {
        this.sdo_choosen = sdo_choosen;
    }

    public Relationship getSro_choosen() {
        return sro_choosen;
    }

    public void setSro_choosen(Relationship sro_choosen) {
        this.sro_choosen = sro_choosen;
    }

    public List<GeneralObject> getSdolist() {
        return sdolist;
    }

    public void setSdolist(List<GeneralObject> sdolist) {
        this.sdolist = sdolist;
    }

    public List<Relationship> getAllsro() {
        return allsro;
    }

    public void setAllsro(List<Relationship> allsro) {
        this.allsro = allsro;
    }

    public List<Relationship> getSrolist() {
        return srolist;
    }

    public void setSrolist(List<Relationship> srolist) {
        this.srolist = srolist;
    }

    public GeneralObject getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(GeneralObject selectedObject) {
        this.selectedObject = selectedObject;
    }

    public LinkedHashMap<String, FieldVAL> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(LinkedHashMap<String, FieldVAL> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public boolean isIsShowing() {
        return isShowing;
    }

    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
        selectedObject = null;
        fieldMap = new LinkedHashMap<>();
        PrimeFaces.current().ajax().update("advanced_sdo");
    }

    public GeneralObject getSelectedObject_left() {
        return selectedObject_left;
    }

    public void setSelectedObject_left(GeneralObject selectedObject_left) {
        this.selectedObject_left = selectedObject_left;
    }

    public GeneralObject getSelectedObject_right() {
        return selectedObject_right;
    }

    public void setSelectedObject_right(GeneralObject selectedObject_right) {
        this.selectedObject_right = selectedObject_right;
    }

    public String getTypeOfRelationship() {
        return typeOfRelationship;
    }

    public void setTypeOfRelationship(String typeOfRelationship) {
        this.typeOfRelationship = typeOfRelationship;
    }

    public List<String> getTypesOfRelationship() {
        return typesOfRelationship;
    }

    public void setTypesOfRelationship(List<String> typesOfRelationship) {
        this.typesOfRelationship = typesOfRelationship;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public boolean isIsDrawing() {
        return isDrawing;
    }

    public void setIsDrawing(boolean isDrawing) {
        this.isDrawing = isDrawing;
    }

    public String getRel_timestamp1() {
        return rel_timestamp1;
    }

    public void setRel_timestamp1(String rel_timestamp1) {
        this.rel_timestamp1 = rel_timestamp1;
    }

    public String getRel_timestamp2() {
        return rel_timestamp2;
    }

    public void setRel_timestamp2(String rel_timestamp2) {
        this.rel_timestamp2 = rel_timestamp2;
    }

    public List<GeneralObject> getAllsdo_for_attrs() {
        return allsdo_for_attrs;
    }

    public void setAllsdo_for_attrs(List<GeneralObject> allsdo_for_attrs) {
        this.allsdo_for_attrs = allsdo_for_attrs;
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getJsonFile1() {
        return jsonFile1;
    }

    public void setJsonFile1(String jsonFile1) {
        this.jsonFile1 = jsonFile1;
    }

    public String getJsonFile2() {
        return jsonFile2;
    }

    public void setJsonFile2(String jsonFile2) {
        this.jsonFile2 = jsonFile2;
    }

    public Float getComparisonResult() {
        return comparisonResult;
    }

    public void setComparisonResult(Float comparisonResult) {
        this.comparisonResult = comparisonResult;
    }

    public String getJsCommand1() {
        return jsCommand1;
    }

    public void setJsCommand1(String jsCommand1) {
        this.jsCommand1 = jsCommand1;
    }

    public String getJsCommand2() {
        return jsCommand2;
    }

    public void setJsCommand2(String jsCommand2) {
        this.jsCommand2 = jsCommand2;
    }

}

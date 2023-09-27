package stix;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpedy.beans.stix.sco.Artifact;
import org.junit.Test;

public class Tests {

    @Test
    public void test() {
        JsonObject json = new JsonObject();
        Gson gson = new Gson();
        Artifact artifact = gson.fromJson("{\n"
                + "            \"created_time\": \"2023-09-14T14:53:15.711687400\",\n"
                + "            \"mime_type\": \"\",\n"
                + "            \"created\": \"2023-09-14T14:53:15.711Z\",\n"
                + "            \"name\": \"\",\n"
                + "            \"description\": \"\",\n"
                + "            \"id\": \"artifact--ec813870-ce65-4c82-a0cb-aab04588cb5f\",\n"
                + "            \"type\": \"artifact\",\n"
                + "            \"url\": \"\"\n"
                + "        }", Artifact.class);
        System.out.println("ciao");
        assert artifact.getId().equals("artifact--ec813870-ce65-4c82-a0cb-aab04588cb5f");

    }
}

import java.io.*;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import org.yaml.snakeyaml.Yaml;

public class DeserializationTestCases {

    // TP 1: HTTP input stream directly into ObjectInputStream.readObject()
    public Object deserializeFromRequest(HttpServletRequest req) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
        // ruleid: unsafe-java-deserialization
        return ois.readObject();
    }

    // TP 2: user-controlled base64 parameter decoded and deserialized
    public Object deserializeFromParam(HttpServletRequest req) throws Exception {
        String data = req.getParameter("payload");
        byte[] decoded = Base64.getDecoder().decode(data);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(decoded));
        // ruleid: unsafe-java-deserialization
        return ois.readObject();
    }

    // TN 1: ValidatingObjectInputStream with whitelist — safe deserialization
    public Object deserializeSafe(HttpServletRequest req) throws Exception {
        ValidatingObjectInputStream vois = new ValidatingObjectInputStream(req.getInputStream());
        vois.accept(SafeClass.class);
        // ok: unsafe-java-deserialization
        return vois.readObject();
    }

    // TN 2: hardcoded file, no user input involved
    public Object deserializeFromFile() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/app/config/settings.ser"));
        // ok: unsafe-java-deserialization
        return ois.readObject();
    }
}
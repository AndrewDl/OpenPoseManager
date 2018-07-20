package sample;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;


/**
 * Created by July on 10.12.2016.
 */
public class XMLwriterReader<T> {
    String address;
    private Logger logger = LogManager.getLogger("General");
    public XMLwriterReader(String address) {
        this.address = address;
    }

    XStream xstream = new XStream(new DomDriver("UTF-8"));

    public void WriteFile(T object, Class c) throws IOException {
        xstream.alias(c.getClass().getName(), c);

        File f = new File(address);
        File folder = f.getParentFile();

        if (!folder.exists())
            folder.mkdirs();

        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(this.address));

        try {
            out.writeObject(object);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        out.close();
    }

    public T ReadFile(Class c){
        xstream.alias(c.getClass().getName(), c);
        ObjectInputStream in = null;
        try {
            in = xstream.createObjectInputStream(new FileReader(address));
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }

        T newObject = null;
        try {
            newObject = (T) in.readObject();
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
            System.out.println("Can't read Parameters File");
        } catch (ClassNotFoundException e) {
            logger.error(e);
            e.printStackTrace();
            System.out.println("Class Not Found");
        }

        return newObject;
    }
}

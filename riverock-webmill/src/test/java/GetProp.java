import java.util.Properties;
import java.util.StringTokenizer;

/**
 * User: rb033151
 * Date: 15.11.11
 * Time: 15:22
 */
public class GetProp {

    public static void main(String[] args) {
        System.out.println( "free memory " + Runtime.getRuntime().freeMemory() +", total memory " + Runtime.getRuntime().totalMemory() +", max memory " + Runtime.getRuntime().maxMemory() );

        System.out.println("\n\nSystem properties:");
        Properties prop = System.getProperties();
        for (String name : prop.stringPropertyNames()) {
            System.out.println(""+name+" --> " + prop.getProperty(name));
        }

        System.out.println("\n\nList of custom jars:");
        StringTokenizer st = new StringTokenizer(prop.getProperty("java.class.path"), ";");
        while(st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }
}

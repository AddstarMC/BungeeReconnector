package au.com.addstar.reconnector;

import static org.junit.Assert.*;
import org.junit.Test;
import sun.applet.Main;

import java.io.File;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 23/09/2017.
 */
public class MainConfigTest {
    private MainConfig testConfig;
    private File testFile = new File("target/tests/testConfig.yml");
    @Test
    public void save() throws Exception {
        testConfig = new MainConfig();

        testConfig.init(testFile);
        testConfig.save();
        MainConfig newConfig =  new MainConfig();
        newConfig.init(testFile);
        newConfig.load();
        assertEquals(testConfig.defaultServer,newConfig.defaultServer);
    }

    @Test
    public void load() throws Exception {
        testFile = new File(getClass().getClassLoader().getResource("testConfig.yml").getFile());
        testConfig  = new MainConfig();
        testConfig.init(testFile);
        testConfig.load();
        assertEquals(testConfig.rules.size(),1);
        assertEquals(testConfig.rules.get("main").servers.size(),6);
    }

}
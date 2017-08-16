package au.com.addstar.reconnector;

import java.util.ArrayList;
import java.util.HashMap;

import net.cubespace.Yamler.Config.*;

public class MainConfig extends YamlConfig
{

	@Comments({"All connection rules go here.", "Format:", "<rulename>:", "  servers: [<list>]", "  destination: <dest>", "", "desination can be blank meaning stay on same server, or * for use default server"})
	public HashMap<String, ServerGroup> rules = new HashMap<String, ServerGroup>();
	
	@Comment("The server to respawn players on when they login if no rules match them. Leave blank for bungee default")
	public String defaultServer = "";

	public MainConfig()
	{
		ServerGroup defRule = new ServerGroup();
		defRule.destination = "hub";
		defRule.servers.add("survival");
		rules.put("main", defRule);
	}
	
	public static class ServerGroup extends ConfigSection
	{
		@Comment("The list of servers.")
		public ArrayList<String> servers = new ArrayList<String>();
		@Comment("The destination server to respawn players on the listed servers on. Leave blank to allow logging back in on the same server.")
		public String destination = "";

    }


}

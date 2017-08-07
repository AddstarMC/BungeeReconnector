package au.com.addstar.reconnector;

import java.util.ArrayList;
import java.util.HashMap;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Comments;
import net.cubespace.Yamler.Config.Config;

public class MainConfig extends Config
{
	public MainConfig()
	{
		ServerGroup defRule = new ServerGroup();
		defRule.destination = "hub";
		defRule.servers.add("survival");
		rules.put("main", defRule);
	}
	
	@Comments({"All connection rules go here.", "Format:", "<rulename>:", "  servers: [<list>]", "  destination: <dest>", "", "desination can be blank meaning stay on same server, or * for use default server"})
	public HashMap<String, ServerGroup> rules = new HashMap<String, ServerGroup>();
	
	@Comment("The server to respawn players on when they login if no rules match them. Leave blank for bungee default")
	public String defaultServer = "";
	
	public static class ServerGroup extends Config
	{
		public ArrayList<String> servers = new ArrayList<String>();
		@Comment("The destination server to respawn players on the listed servers on. Leave blank to allow logging back in on the same server.")
		public String destination = "";
	}
}

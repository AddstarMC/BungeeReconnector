package au.com.addstar.reconnector;

import java.util.ArrayList;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Config;

public class MainConfig extends Config
{
	public ArrayList<ServerGroup> rules = new ArrayList<ServerGroup>();
	
	@Comment("The server to respawn players on when they login if no rules match them")
	public String defaultServer = "";
	
	public static class ServerGroup extends Config
	{
		public ArrayList<String> servers = new ArrayList<String>();
		@Comment("The destination server to respawn players on the listed servers on. Leave blank to allow logging back in on the same server.")
		public String destination = "";
	}
}

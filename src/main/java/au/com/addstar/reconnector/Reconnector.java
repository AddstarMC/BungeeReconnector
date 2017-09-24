package au.com.addstar.reconnector;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Reconnector implements ReconnectHandler
{
	private ReconnectorPlugin mPlugin;
	
	public Reconnector(ReconnectorPlugin plugin)
	{
		mPlugin = plugin;
	}
	
	public void close()
	{
	}

	public ServerInfo getServer( ProxiedPlayer player )
	{
		PlayerData data = mPlugin.loadPlayerData(player.getUniqueId());
		ServerInfo reconnecter =  mPlugin.getReconnectServer(data.lastServer);
		if(reconnecter == null){
			reconnecter = ProxyServer.getInstance().getServerInfo(player.getPendingConnection().getListener().getServerPriority().get(0));
		}
		return reconnecter;
	}

	public void save()
	{
	}

	public void setServer( ProxiedPlayer player )
	{
		PlayerData data = mPlugin.loadPlayerData(player.getUniqueId());
		data.lastServer = player.getServer().getInfo().getName();
		
		try
		{
			data.save();
		}
		catch(InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}

}

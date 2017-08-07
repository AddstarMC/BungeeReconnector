package au.com.addstar.reconnector;

import java.io.File;
import java.util.UUID;

import au.com.addstar.reconnector.MainConfig.ServerGroup;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ReconnectorPlugin extends Plugin implements Listener
{
	private MainConfig mConfig;
	private File mPlayerDataFolder;
	
	@Override
	public void onEnable()
	{
		if(!getDataFolder().exists())
			getDataFolder().mkdirs();
		
		mConfig = new MainConfig();
		
		try
		{
			mConfig.init(new File(getDataFolder(), "config.yml"));
			mConfig.save();
		}
		catch ( InvalidConfigurationException e )
		{
			e.printStackTrace();
		}
		
		mPlayerDataFolder = new File(getDataFolder(), "playerdata");
		if(!mPlayerDataFolder.exists())
			mPlayerDataFolder.mkdirs();
		
		getProxy().setReconnectHandler(new Reconnector(this));
		getProxy().getPluginManager().registerListener(this, new ReconnectionHandler(this));
		getProxy().getPluginManager().registerListener(this, this);
	}
	
	public PlayerData loadPlayerData(UUID player)
	{
		File file = new File(mPlayerDataFolder, player.toString().toLowerCase() + ".yml");
		
		PlayerData data = new PlayerData();
		try
		{
			data.init(file);
		}
		catch(InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
		
		return data;
	}
	
	public ServerInfo getReconnectServer(String lastServer)
	{
		for(ServerGroup group : mConfig.rules.values())
		{
			if(!group.servers.contains(lastServer))
				continue;
			
			if(group.destination == null || group.destination.isEmpty())
				return getProxy().getServerInfo(lastServer);
			else if(group.destination.equals("*"))
				return getProxy().getServerInfo(mConfig.defaultServer);
			else
				return getProxy().getServerInfo(group.destination);
		}
		
		return getProxy().getServerInfo(mConfig.defaultServer);
	}
	
	@EventHandler
	public void onPlayerJoin(PostLoginEvent event)
	{
		PlayerData data = loadPlayerData(event.getPlayer().getUniqueId());
		data.name = event.getPlayer().getName();
		
		try
		{
			data.save();
		}
		catch ( InvalidConfigurationException e )
		{
			e.printStackTrace();
		}
	}
}

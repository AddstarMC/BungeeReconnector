package au.com.addstar.reconnector;

import java.lang.reflect.Method;
import java.util.WeakHashMap;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ReconnectionHandler implements Listener
{
	private WeakHashMap<ProxiedPlayer, Boolean> mHandled = new WeakHashMap<ProxiedPlayer, Boolean>();
	private ReconnectorPlugin mPlugin;
	
	public ReconnectionHandler(ReconnectorPlugin plugin)
	{
		mPlugin = plugin;
	}
	
	@EventHandler
	public void onFirstConnection(ServerConnectEvent event)
	{
		if(event.getPlayer().getServer() != null)
			return;
		
		if(mHandled.put(event.getPlayer(), true) != null)
			return;
		
		event.setCancelled(true);
		
		// Try connection to reconnect server
		PlayerData data = mPlugin.loadPlayerData(event.getPlayer().getUniqueId());
		ServerInfo initialServer = mPlugin.getReconnectServer(data.lastServer);
		if(initialServer == null)
			initialServer = ProxyServer.getInstance().getServerInfo(event.getPlayer().getPendingConnection().getListener().getServerPriority().get(0));
		
		try
		{
			Method method = event.getPlayer().getClass().getMethod("setDimensionChange", Boolean.TYPE);
			method.invoke(event.getPlayer(), false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		connectTo(event.getPlayer(), initialServer);
	}
	
	private ServerInfo getDefaultServer(ProxiedPlayer player)
	{
		return ProxyServer.getInstance().getServerInfo(player.getPendingConnection().getListener().getServerPriority().get(0));
	}
	
	private ServerInfo getFallbackServer(ProxiedPlayer player)
	{
		String info =
				player.getPendingConnection().getListener().getServerPriority().size() > 1 ? player.getPendingConnection().getListener().getServerPriority().get(1) : player.getPendingConnection().getListener().getServerPriority().get(0);
		return ProxyServer.getInstance().getServerInfo(info);
	}
	
	private void connectTo(final ProxiedPlayer player, final ServerInfo server)
	{
		player.connect(server, new Callback<Boolean>()
		{
			public void done( Boolean success, Throwable error )
			{
				if (success == null || !success)
				{
					if (server == getDefaultServer(player))
						connectTo(player, getFallbackServer(player));
					else if(server != getFallbackServer(player))
						connectTo(player, getDefaultServer(player));
					else
					{
						player.disconnect(TextComponent.fromLegacyText(ProxyServer.getInstance().getTranslation("fallback_kick")));
					}
				}
			}
		});
	}
}

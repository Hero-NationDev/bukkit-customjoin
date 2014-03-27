package org.ivran.customjoin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.ivran.customjoin.CustomJoinPlugin;
import org.ivran.customjoin.MessageType;
import org.ivran.customjoin.Strings;

public class SetPlayerMsgExecutor extends AbstractExecutor implements ICommandCheck {

  private final Strings strings;
  private final FileConfiguration config;

  public SetPlayerMsgExecutor(CustomJoinPlugin plugin) {
    super(plugin,
        new PermissionCheck("customjoin.setplayermessage"),
        new ArgumentCountCheck(2, -1));

    addCheck(this);

    this.strings = plugin.getStrings();
    this.config = plugin.getConfig();
  }

  @Override
  public void doCheck(CommandSender sender, Command cmd, String alias, String[] args) throws CheckException {
    try {
      MessageType.valueOf(args[1].toUpperCase());
    }
    catch (IllegalArgumentException e) {
      throw new CheckException(strings.format("Command.UnknownMessageType", args[1]));
    }
  }

  @Override
  protected void execute(CommandSender sender, Command cmd, String[] args) {
    String message = null;

    if (args.length - 2 > 0) {
      StringBuilder messageBuilder = new StringBuilder();

      for (int i = 2; i < args.length; i++) {
        messageBuilder.append(args[i]).append(' ');
      }

      message = messageBuilder.toString().trim();
    }

    String player = args[0];
    String type = args[1];

    config.set(String.format("custom.%s.%s", type, player), message);

    sender.sendMessage(ChatColor.GRAY
        + strings.format(message == null ? "Command.PlayerMessageDeleted" : "Command.PlayerMessageSet", player, type));
  }
}

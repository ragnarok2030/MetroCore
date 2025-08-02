package net.metroCore.Modules.guns.command;

import net.metroCore.Core.command.AbstractCommand;

public class GunCommand extends AbstractCommand {
    public GunCommand() {
        // this.command is what gets shown in help and registered in plugin.yml
        this.command = "gun";

        // register both reload AND give
        registerSubCommand("reload", new ReloadGunCommand());
        registerSubCommand("give",   new GiveGunCommand());
    }
}

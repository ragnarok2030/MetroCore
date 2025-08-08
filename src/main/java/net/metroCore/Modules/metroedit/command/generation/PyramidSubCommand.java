package net.metroCore.Modules.metroedit.command.generation;

import net.metroCore.Core.command.AbstractSubCommand;
import net.metroCore.MetroCore;
import net.metroCore.Modules.metroedit.MetroEditModule;
import net.metroCore.Modules.metroedit.handler.GenerationHandler;
import net.metroCore.Modules.metroedit.region.*;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler;
import net.metroCore.Modules.metroedit.handler.UndoRedoHandler.BlockChange;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class PyramidSubCommand extends AbstractSubCommand {
    @Override public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) return usage(sender);
        int base = args.length>0?parseInt(sender,args[0],-1):-1;
        int height = args.length>1?parseInt(sender,args[1],base):-1;
        if (base<0||height<0) return true;
        MetroEditModule mod = MetroCore.getInstance().getModuleRegistry().get(MetroEditModule.class);
        GenerationHandler gen = new GenerationHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();
        Location apex = p.getLocation();
        List<BlockChange> batch = new ArrayList<>();
        for(Location loc: new PyramidRegion(apex, base, height)){
            batch.add(new BlockChange(loc.clone(),loc.getBlock().getBlockData(),p.getInventory().getItemInMainHand().getType().createBlockData()));
        }
        undo.recordBulk(p,batch);
        batch.forEach(BlockChange::applyRedo);
        p.sendMessage(ChatColor.GREEN+"Generated pyramid base="+base+" h="+height);
        return true;
    }
    private boolean usage(CommandSender s){s.sendMessage("§cUsage: /metroedit pyramid <baseRadius> [height]");return true;}
    private int parseInt(CommandSender s,String v,int def){try{return Integer.parseInt(v);}catch(Exception e){s.sendMessage("§cInvalid number: "+v);return def;}}
    @Override public String getDescription(){return "Create a pyramid";}
    @Override public String getUsage(){return "pyramid <baseRadius> [height]";}
    @Override public String getPermission(){return "metrocore.metroedit.pyramid";}
}

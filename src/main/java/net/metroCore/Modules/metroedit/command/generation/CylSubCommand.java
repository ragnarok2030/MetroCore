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

public class CylSubCommand extends AbstractSubCommand {
    @Override public boolean onExecute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player p)) return usage(sender);
        int radius = args.length>0?parseInt(sender,args[0],-1):-1;
        int height = args.length>1?parseInt(sender,args[1],-1):-1;
        if (radius<0||height<0) return true;
        MetroEditModule mod = MetroCore.getInstance().getModuleRegistry().get(MetroEditModule.class);
        GenerationHandler gen = new GenerationHandler();
        UndoRedoHandler undo = mod.getUndoRedoHandler();
        Location center = p.getLocation();
        List<BlockChange> batch = new ArrayList<>();
        for (Location loc : new CylinderRegion(center.clone().add(0,-height/2,0), center.clone().add(0,height/2,0))) {
            batch.add(new BlockChange(loc.clone(), loc.getBlock().getBlockData(), p.getInventory().getItemInMainHand().getType().createBlockData()));
        }
        undo.recordBulk(p,batch);
        batch.forEach(BlockChange::applyRedo);
        p.sendMessage(ChatColor.GREEN+"Generated cylinder r="+radius+" h="+height);
        return true;
    }
    private boolean usage(CommandSender s) { s.sendMessage("§cUsage: /metroedit cyl <radius> <height>"); return true; }
    private int parseInt(CommandSender s,String v,int def){try{return Integer.parseInt(v);}catch(Exception e){s.sendMessage("§cInvalid number: "+v);return def;}}
    @Override public String getDescription(){return "Create a filled cylinder";}
    @Override public String getUsage(){return "cyl <radius> <height>";}
    @Override public String getPermission(){return "metrocore.metroedit.cyl";}
}
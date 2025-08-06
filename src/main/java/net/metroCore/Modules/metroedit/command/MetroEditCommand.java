package net.metroCore.Modules.metroedit.command;

import net.metroCore.Core.command.AbstractCommand;

// ── general ─────────────────────────────────────────────
import net.metroCore.Modules.metroedit.command.general.InfoSubCommand;
import net.metroCore.Modules.metroedit.command.general.UndoSubCommand;
import net.metroCore.Modules.metroedit.command.general.RedoSubCommand;
import net.metroCore.Modules.metroedit.command.general.ClearHistorySubCommand;
import net.metroCore.Modules.metroedit.command.general.LimitSubCommand;
import net.metroCore.Modules.metroedit.command.general.TimeoutSubCommand;
import net.metroCore.Modules.metroedit.command.general.PerfSubCommand;
import net.metroCore.Modules.metroedit.command.general.UpdateSubCommand;
import net.metroCore.Modules.metroedit.command.general.ReorderSubCommand;
import net.metroCore.Modules.metroedit.command.general.DrawSelSubCommand;
import net.metroCore.Modules.metroedit.command.general.WorldSubCommand;
import net.metroCore.Modules.metroedit.command.general.WatchdogSubCommand;
import net.metroCore.Modules.metroedit.command.general.GMaskSubCommand;
import net.metroCore.Modules.metroedit.command.general.TogglePlaceSubCommand;
import net.metroCore.Modules.metroedit.command.general.PlacementSubCommand;
import net.metroCore.Modules.metroedit.command.general.SearchItemSubCommand;

// ── navigation ──────────────────────────────────────────
import net.metroCore.Modules.metroedit.command.navigation.UnstuckSubCommand;
import net.metroCore.Modules.metroedit.command.navigation.AscendSubCommand;
import net.metroCore.Modules.metroedit.command.navigation.DescendSubCommand;
import net.metroCore.Modules.metroedit.command.navigation.CeilSubCommand;
import net.metroCore.Modules.metroedit.command.navigation.ThruSubCommand;
import net.metroCore.Modules.metroedit.command.navigation.JumpToSubCommand;
import net.metroCore.Modules.metroedit.command.navigation.UpSubCommand;

// ── selection ───────────────────────────────────────────
import net.metroCore.Modules.metroedit.command.selection.Pos1SubCommand;
import net.metroCore.Modules.metroedit.command.selection.Pos2SubCommand;
import net.metroCore.Modules.metroedit.command.selection.HPos1SubCommand;
import net.metroCore.Modules.metroedit.command.selection.HPos2SubCommand;
import net.metroCore.Modules.metroedit.command.selection.PosSubCommand;
import net.metroCore.Modules.metroedit.command.selection.ChunkSubCommand;
import net.metroCore.Modules.metroedit.command.selection.WandSubCommand;
import net.metroCore.Modules.metroedit.command.selection.ToggleEditWandSubCommand;
import net.metroCore.Modules.metroedit.command.selection.ContractSubCommand;
import net.metroCore.Modules.metroedit.command.selection.ShiftSubCommand;
import net.metroCore.Modules.metroedit.command.selection.OutsetSubCommand;
import net.metroCore.Modules.metroedit.command.selection.InsetSubCommand;
import net.metroCore.Modules.metroedit.command.selection.TrimSubCommand;
import net.metroCore.Modules.metroedit.command.selection.SizeSubCommand;
import net.metroCore.Modules.metroedit.command.selection.CountSubCommand;
import net.metroCore.Modules.metroedit.command.selection.NoneSubCommand;
import net.metroCore.Modules.metroedit.command.selection.SelSubCommand;

// ── region ──────────────────────────────────────────────
import net.metroCore.Modules.metroedit.command.region.SetSubCommand;
import net.metroCore.Modules.metroedit.command.region.ReplSubCommand;
import net.metroCore.Modules.metroedit.command.region.CutSubCommand;
import net.metroCore.Modules.metroedit.command.region.CopySubCommand;
import net.metroCore.Modules.metroedit.command.region.PasteSubCommand;
import net.metroCore.Modules.metroedit.command.region.StackSubCommand;
import net.metroCore.Modules.metroedit.command.region.MoveSubCommand;
import net.metroCore.Modules.metroedit.command.region.FlipSubCommand;
import net.metroCore.Modules.metroedit.command.region.RotateSubCommand;
import net.metroCore.Modules.metroedit.command.region.DistrSubCommand;
import net.metroCore.Modules.metroedit.command.region.FillSubCommand;
import net.metroCore.Modules.metroedit.command.region.OverlaySubCommand;
import net.metroCore.Modules.metroedit.command.region.WallsSubCommand;
import net.metroCore.Modules.metroedit.command.region.FacesSubCommand;

// ── generation ─────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.generation.SphereSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.CylSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.HSphereSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.HCylSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.PyramidSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.PrismSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.ForestSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.BrushSubCommand;
//import net.metroCore.Modules.metroedit.command.generation.GenerateBiomeSubCommand;

// ── schematic ───────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.schematic.SchematicSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.CloneSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.ButcherSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.RegenSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.SnapshotSaveSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.SnapshotRestoreSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.SnapshotListSubCommand;
//import net.metroCore.Modules.metroedit.command.schematic.SnapshotDeleteSubCommand;

// ── tools ───────────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.tools.ToolNoneSubCommand;
//import net.metroCore.Modules.metroedit.command.tools.ToolWandSubCommand;
//import net.metroCore.Modules.metroedit.command.tools.PermissionSubCommand;

// ── superpickaxe ────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.superpickaxe.ThruSubCommand as SuperThruSubCommand;
//import net.metroCore.Modules.metroedit.command.superpickaxe.CLSubCommand;
//import net.metroCore.Modules.metroedit.command.superpickaxe.SnowSubCommand as SuperSnowSubCommand;
//import net.metroCore.Modules.metroedit.command.superpickaxe.DrainSubCommand as SuperDrainSubCommand;
//import net.metroCore.Modules.metroedit.command.superpickaxe.FillLakeSubCommand;
//import net.metroCore.Modules.metroedit.command.superpickaxe.DrainLakeSubCommand;

// ── brushes ────────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.brushes.BrushSphereSubCommand;
//import net.metroCore.Modules.metroedit.command.brushes.BrushCylSubCommand;
//import net.metroCore.Modules.metroedit.command.brushes.BrushEllipsoidSubCommand;
//import net.metroCore.Modules.metroedit.command.brushes.BrushSmoothSubCommand;
//import net.metroCore.Modules.metroedit.command.brushes.BrushOverlaySubCommand;

// ── biomes ──────────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.biomes.SetBiomeSubCommand;
//import net.metroCore.Modules.metroedit.command.biomes.ReplaceBiomeSubCommand;
//import net.metroCore.Modules.metroedit.command.biomes.SmoothBiomeSubCommand;

// ── chunks ──────────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.chunks.ChunkInfoSubCommand;
//import net.metroCore.Modules.metroedit.command.chunks.ChunkApplySubCommand;
//import net.metroCore.Modules.metroedit.command.chunks.ChunkDeleteSubCommand;
//import net.metroCore.Modules.metroedit.command.chunks.ChunkCopySubCommand;
//import net.metroCore.Modules.metroedit.command.chunks.ListChunksSubCommand;

// ── scripting ───────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.scripting.ScriptRunSubCommand;
//import net.metroCore.Modules.metroedit.command.scripting.ScriptSaveSubCommand;
//import net.metroCore.Modules.metroedit.command.scripting.ScriptListSubCommand;

// ── utility ─────────────────────────────────────────────
//import net.metroCore.Modules.metroedit.command.utility.CalculateSubCommand;
//import net.metroCore.Modules.metroedit.command.utility.CalcSubCommand;
//import net.metroCore.Modules.metroedit.command.utility.ExtinguishSubCommand;

public class MetroEditCommand extends AbstractCommand {

    public MetroEditCommand() {
        this.command = "metroedit";

        // general
        registerSubCommand("info",           new InfoSubCommand());
        registerSubCommand("undo",           new UndoSubCommand());
        registerSubCommand("redo",           new RedoSubCommand());
        registerSubCommand("clearhistory",   new ClearHistorySubCommand());
        registerSubCommand("limit",          new LimitSubCommand());
        registerSubCommand("timeout",        new TimeoutSubCommand());
        registerSubCommand("perf",           new PerfSubCommand());
        registerSubCommand("update",         new UpdateSubCommand());
        registerSubCommand("reorder",        new ReorderSubCommand());
        registerSubCommand("drawsel",        new DrawSelSubCommand());
        registerSubCommand("world",          new WorldSubCommand());
        registerSubCommand("watchdog",       new WatchdogSubCommand());
        registerSubCommand("gmask",          new GMaskSubCommand());
        registerSubCommand("toggleplace",    new TogglePlaceSubCommand());
        registerSubCommand("placement",      new PlacementSubCommand());
        registerSubCommand("searchitem",     new SearchItemSubCommand());

        // navigation
        registerSubCommand("unstuck",        new UnstuckSubCommand());
        registerSubCommand("ascend",         new AscendSubCommand());
        registerSubCommand("descend",        new DescendSubCommand());
        registerSubCommand("ceil",           new CeilSubCommand());
        registerSubCommand("thru",           new ThruSubCommand());
        registerSubCommand("jumpto",         new JumpToSubCommand());
        registerSubCommand("up",             new UpSubCommand());

        // selection
        registerSubCommand("pos1",           new Pos1SubCommand());
        registerSubCommand("pos2",           new Pos2SubCommand());
        registerSubCommand("hpos1",          new HPos1SubCommand());
        registerSubCommand("hpos2",          new HPos2SubCommand());
        registerSubCommand("pos",            new PosSubCommand());
        registerSubCommand("chunk",          new ChunkSubCommand());
        registerSubCommand("wand",           new WandSubCommand());
        registerSubCommand("selwand",        new ToggleEditWandSubCommand());
        registerSubCommand("contract",       new ContractSubCommand());
        registerSubCommand("shift",          new ShiftSubCommand());
        registerSubCommand("outset",         new OutsetSubCommand());
        registerSubCommand("inset",          new InsetSubCommand());
        registerSubCommand("trim",           new TrimSubCommand());
        registerSubCommand("size",           new SizeSubCommand());
        registerSubCommand("count",          new CountSubCommand());
        registerSubCommand("none",           new NoneSubCommand());
        registerSubCommand("sel",            new SelSubCommand());

        // region
        registerSubCommand("set",            new SetSubCommand());
        registerSubCommand("repl",           new ReplSubCommand());
        registerSubCommand("cut",            new CutSubCommand());
        registerSubCommand("copy",           new CopySubCommand());
        registerSubCommand("paste",          new PasteSubCommand());
        registerSubCommand("stack",          new StackSubCommand());
        registerSubCommand("move",           new MoveSubCommand());
        registerSubCommand("flip",           new FlipSubCommand());
        registerSubCommand("rotate",         new RotateSubCommand());
        registerSubCommand("distr",          new DistrSubCommand());
        registerSubCommand("fill",           new FillSubCommand());
        registerSubCommand("overlay",        new OverlaySubCommand());
        registerSubCommand("walls",          new WallsSubCommand());
        registerSubCommand("faces",          new FacesSubCommand());

//        // generation
//        registerSubCommand("sphere",         new SphereSubCommand());
//        registerSubCommand("cyl",            new CylSubCommand());
//        registerSubCommand("hsphere",        new HSphereSubCommand());
//        registerSubCommand("hcyl",           new HCylSubCommand());
//        registerSubCommand("pyramid",        new PyramidSubCommand());
//        registerSubCommand("prism",          new PrismSubCommand());
//        registerSubCommand("forest",         new ForestSubCommand());
//        registerSubCommand("brush",          new BrushSubCommand());
//        registerSubCommand("generatebiome",  new GenerateBiomeSubCommand());
//
//        // schematic
//        registerSubCommand("schematic",      new SchematicSubCommand());
//        registerSubCommand("clone",          new CloneSubCommand());
//        registerSubCommand("butcher",        new ButcherSubCommand());
//        registerSubCommand("regen",          new RegenSubCommand());
//        registerSubCommand("snapshotSave",   new SnapshotSaveSubCommand());
//        registerSubCommand("snapshotRestore",new SnapshotRestoreSubCommand());
//        registerSubCommand("snapshotList",   new SnapshotListSubCommand());
//        registerSubCommand("snapshotDelete", new SnapshotDeleteSubCommand());
//
//        // tools
//        registerSubCommand("toolnone",       new ToolNoneSubCommand());
//        registerSubCommand("toolwand",       new ToolWandSubCommand());
//        registerSubCommand("permission",     new PermissionSubCommand());
//
//        // superpickaxe
//        registerSubCommand("superthru",      new SuperThruSubCommand());
//        registerSubCommand("cl",             new CLSubCommand());
//        registerSubCommand("supersnow",      new SuperSnowSubCommand());
//        registerSubCommand("superdrain",     new SuperDrainSubCommand());
//        registerSubCommand("filllake",       new FillLakeSubCommand());
//        registerSubCommand("drainlake",      new DrainLakeSubCommand());
//
//        // brushes
//        registerSubCommand("brushsphere",    new BrushSphereSubCommand());
//        registerSubCommand("brushcyl",       new BrushCylSubCommand());
//        registerSubCommand("brushellipsoid", new BrushEllipsoidSubCommand());
//        registerSubCommand("brushsmooth",    new BrushSmoothSubCommand());
//        registerSubCommand("brushoverlay",   new BrushOverlaySubCommand());
//
//        // biomes
//        registerSubCommand("setbiome",       new SetBiomeSubCommand());
//        registerSubCommand("replacebiome",   new ReplaceBiomeSubCommand());
//        registerSubCommand("smoothbiome",    new SmoothBiomeSubCommand());
//
//        // chunks
//        registerSubCommand("chunkinfo",      new ChunkInfoSubCommand());
//        registerSubCommand("chunkapply",     new ChunkApplySubCommand());
//        registerSubCommand("chunkdelete",    new ChunkDeleteSubCommand());
//        registerSubCommand("chunkcopy",      new ChunkCopySubCommand());
//        registerSubCommand("listchunks",     new ListChunksSubCommand());
//
//        // scripting
//        registerSubCommand("scriptrun",      new ScriptRunSubCommand());
//        registerSubCommand("scriptsave",     new ScriptSaveSubCommand());
//        registerSubCommand("scriptlist",     new ScriptListSubCommand());
//
//        // utility
//        registerSubCommand("calculate",      new CalculateSubCommand());
//        registerSubCommand("calc",           new CalcSubCommand());
//        registerSubCommand("extinguish",     new ExtinguishSubCommand());
    }
}

package vazkii.quark.content.automation.module;

import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.quark.api.IIndirectConnector;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.AUTOMATION)
public class ChainsConnectBlocksModule extends QuarkModule {

	public static boolean staticEnabled;
	
	@Override
	public void register() {
		IIndirectConnector.INDIRECT_STICKY_BLOCKS.add(Pair.of(ChainConnection.PREDICATE, ChainConnection.INSTANCE));
	}
	
	@Override
	public void configChanged() {
		staticEnabled = enabled;
	}
	
	public static class ChainConnection implements IIndirectConnector {

		public static ChainConnection INSTANCE = new ChainConnection();
		public static Predicate<BlockState> PREDICATE = s -> s.getBlock() == Blocks.CHAIN;
		
		@Override
		public boolean isEnabled() {
			return ChainsConnectBlocksModule.staticEnabled;
		}
		
		@Override
		public boolean canConnectIndirectly(Level world, BlockPos ourPos, BlockPos sourcePos, BlockState ourState, BlockState sourceState) {
			Axis axis = ourState.getValue(ChainBlock.AXIS);
			
			switch(axis) {
			case X:
				if(ourPos.getX() == sourcePos.getX())
					return false;
				break;
			case Y:
				if(ourPos.getY() == sourcePos.getY())
					return false;
				break;
			case Z:
				if(ourPos.getZ() == sourcePos.getZ())
					return false;
			}
			
			if(sourceState.getBlock() == ourState.getBlock()) {
				Axis otherAxis = sourceState.getValue(ChainBlock.AXIS);
				return axis == otherAxis;
			}
			
			return true;
		}
		
	}
	
}

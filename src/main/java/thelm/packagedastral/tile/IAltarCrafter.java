package thelm.packagedastral.tile;

import hellfirepvp.astralsorcery.common.tile.altar.AltarCollectionCategory;
import thelm.packagedastral.starlight.IStarlightReceiverLinkableTile;

public interface IAltarCrafter extends IStarlightReceiverLinkableTile, IHasFakeAltar {

	void collectStarlight(float amount, AltarCollectionCategory relay);
}

package game.environments;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.GraceRestAction;

/**
 * An abstract class that represents a Site of Lost Grace.
 * Created by:
 * @author Jun Lim
 *
 */
public abstract class SiteOfLostGrace extends Ground {
	private String name;
	/**
	 * Constructor.
	*/
	public SiteOfLostGrace(String name) {
		super('U');
		this.name = name;
	}

	@Override
	public ActionList allowableActions(Actor actor, Location location, String direction) {
		ActionList actionList = new ActionList();
		actionList.add(new GraceRestAction(location, this.name));
		return actionList;
	}
}

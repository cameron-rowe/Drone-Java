package edu.unr.ecsl.ents;

import edu.unr.ecsl.Engine;
import edu.unr.ecsl.enums.EntityType;

/**
 * Created by cam on 1/5/15.
 */
public class Marine extends Entity {
    public Marine(Engine eng) {
        super(eng, EntityType.MARINE);
    }
}

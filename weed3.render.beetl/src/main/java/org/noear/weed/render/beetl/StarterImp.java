package org.noear.weed.render.beetl;

import org.noear.weed.IStarter;
import org.noear.weed.SQLRenderManager;

public class StarterImp implements IStarter {

    @Override
    public void start() {
        SQLRenderManager.global().mapping(".jsp", SQLBeetlRender.global());
    }
}

package org.noear.weed.solon.plugin;

import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.event.EventListener;

import javax.sql.DataSource;

class DsEventListener implements EventListener<BeanWrap> {

   @Override
   public void onEvent(BeanWrap bw) {
       if (bw.raw() instanceof DataSource) {
           DbManager.global().reg(bw);
       }
   }
}

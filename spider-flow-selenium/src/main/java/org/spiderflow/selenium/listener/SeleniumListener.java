package org.spiderflow.selenium.listener;

import org.spiderflow.context.SpiderContext;
import org.spiderflow.listener.SpiderListener;
import org.spiderflow.utils.SpiderResponseHolder;
import org.springframework.stereotype.Component;

@Component
public class SeleniumListener implements SpiderListener{
	
	@Override
	public void beforeStart(SpiderContext context) {
		
	}

	@Override
	public void afterEnd(SpiderContext context) {
		SpiderResponseHolder.clear(context);
	}

}

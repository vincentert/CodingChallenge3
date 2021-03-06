package com.takima.adsample.rule;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.takima.adsample.entity.AdSample;
import com.takima.adsample.service.BlackListClientService;

@Component
public class RuleRegisterNumberBlackList implements IadSampleRule {

	@Autowired
	BlackListClientService blackListClientService;
		
    private ExecutorService executor 
    = Executors.newSingleThreadExecutor();
	
	@Override
	public Future<Optional<Rule>> validate(AdSample adSample) throws InterruptedException, ExecutionException, TimeoutException {
		return executor.submit(() -> {
			Rule rule = null;
			
			String eanCode = adSample.getItem().getEanCode();
			
			List<String> quotationClient = blackListClientService.getBlackListedClients(eanCode).get(100, TimeUnit.MILLISECONDS);
			
			if (quotationClient.contains(eanCode)) rule = Rule.RULE_REGISTER_NUMBER_BLACKLIST;
			
			return Optional.ofNullable(rule);
		});

	}

}

package br.com.viewtec.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viewtec.springboot.model.InternalUrl;
import br.com.viewtec.springboot.repository.InternalUrlRepository;

@Service
public class InternalUrlService {

	@Autowired
	private InternalUrlRepository repository;

	public InternalUrl save(InternalUrl iu) {
		return this.repository.save(iu);
	}

	public InternalUrl findByFullUrl(String fullUrl) {
		return this.repository.findByFullUrl(fullUrl);
	}

	public InternalUrl findByShortUrl(String shortUrl) {
		return this.repository.findByShortUrl(shortUrl);
	}
}

package br.com.viewtec.springboot.facade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viewtec.springboot.model.InternalUrl;
import br.com.viewtec.springboot.service.InternalUrlService;

/**
 * Class responsable for decisions and to be out of the transactional context
 * allowing to handle rollbacks.
 *
 * @author Rodrigo Medda Pereira
 *
 */
@Service
public class InternalUrlFacade {
	@Autowired
	private InternalUrlService service;

	/*
	 * Stores the top 100 urls.
	 */
	private static Map<String, InternalUrl> top100 = new HashMap<String, InternalUrl>();

	/**
	 * Check if the url already exists in the database.  If don't exists, creates a new one.
	 * @param url
	 * @return
	 */
	public InternalUrl resolve(String fullUrl) {
		//include a MAP for cached urls
		InternalUrl iu = this.service.findByFullUrl(fullUrl);
		if (iu == null) {
			 //create short url
			//check for colision
			String shortUrl = this.createShortUrl();
			iu = this.service.findByShortUrl(shortUrl);
			if (iu != null) {
				shortUrl = this.createShortUrl();
			}
			iu = new InternalUrl();
			iu.setFullUrl(fullUrl);
			iu.setShortUrl(shortUrl);
			iu.setCalls(1L);  //first time called

			//save
			this.service.save(iu);

			//validate top100
		}

		return iu;
	}

	/**
	 * Creates a random short url with a limited size.
	 * @return
	 */
	private String createShortUrl() {
		char[] cs = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
		             'o', 'p', 'q', 'r', 's', 'x', 'y', 'w', 'z', '1', '2', '3', '4', '5', '6',
		             '7','8','9','0'};
		int max = cs.length - 1;
		int min = 0;
		StringBuilder shortUrl = new StringBuilder();
		for (int i = 0; i < InternalUrl.SHORT_URL_MAX_SIZE; i++) {
			int index = (int) ((Math.random() * (max - min)) + min);
			shortUrl.append(cs[index]);
		}
		return shortUrl.toString();
	}

	/**
	 *
	 * @param shortUrl
	 * @return
	 */
	public InternalUrl findByShortUrl(String shortUrl) {
		//search in the "cache" first.
		InternalUrl topUrl = top100.get(shortUrl);
		InternalUrl iu = null;
		if (topUrl != null) {
			synchronized(this) {
				topUrl.setCalls(topUrl.getCalls() + 1);
				this.service.save(topUrl);
			}

			iu = topUrl;
		} else {
			iu = this.service.findByShortUrl(shortUrl);

			//ranking
			if (iu != null) {
				synchronized(this) {
					iu.setCalls(iu.getCalls() + 1);
				}
				this.service.save(iu);

				//check if is top 100.
				//see in the database if this is one of the top 100.
				//if it is, read the top 100 again and stores them in memory.
			}
		}

		return iu;
	}

	private void validateTop100() {
	}
}

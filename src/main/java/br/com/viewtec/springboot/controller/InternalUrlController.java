package br.com.viewtec.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viewtec.springboot.facade.InternalUrlFacade;
import br.com.viewtec.springboot.model.InternalUrl;

@RestController
@RequestMapping("/shorterUrl")
public class InternalUrlController {

	@Autowired
	private InternalUrlFacade facade;

	@PostMapping("/resolve")
	public InternalUrl resolve(@RequestBody String fullUrl) {
		return this.facade.resolve(fullUrl);
	}

	@GetMapping("/findByShortUrl/{url}")
	public String findByShortUrl(@PathVariable String url) {
		InternalUrl iu = this.facade.findByShortUrl(url);
		String response = null;
		if (iu == null) {
			response = "URL not found.";
		} else {
			response = iu.getFullUrl();
		}

		return response;
	}
}

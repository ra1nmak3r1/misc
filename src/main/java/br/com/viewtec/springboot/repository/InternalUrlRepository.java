package br.com.viewtec.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.viewtec.springboot.model.InternalUrl;

@Repository
public interface InternalUrlRepository extends JpaRepository<InternalUrl, Integer> {

	public InternalUrl findByFullUrl(String fullUrl);

	public InternalUrl findByShortUrl(String shortUrl);
}

package com.byopvr.discourse.smfmapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) throws BeansException, IOException {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		ctx.getBean(Application.class).map();
	}

	public void map() throws IOException {
		final List<MapEntry> mapEntries = MapEntry.getMapEntries(jdbcTemplate);

		final Iterator<String> lines = mapEntries
				.stream()
				.map(mapEntry -> String.format(
						"~/dvr/index\\.php[/\\?]topic[=,]%d\\. /t/%s/%d;",
						mapEntry.getSmfTopicId(), mapEntry.getDiscourseSlug(),
						mapEntry.getDiscourseTopicId())).iterator();

		Files.write(Paths.get("smf2.map"), new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return lines;
			}
		}, StandardCharsets.UTF_8);

		/*
		 * ~/dvr/index\.php[/\?]topic[=,]12033\. /t/what-would-i-need/10101;
		 * ~/dvr/.* /;
		 */
	}
}

package com.byopvr.discourse.smfmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class MapEntry {

	private static final String MAP_ENTRY_QUERY = //
	"select distinct(posts.topic_id) as discourse_topic_id, value as smf_topic_id, "
			+ "topics.slug as smf_slug "
			+ "from post_custom_fields, posts, topics "
			+ "where name = 'import_topic_id' and post_id = posts.id "
			+ "and posts.topic_id = topics.id";

	private final long smfTopicId;
	private final long discourseTopicId;
	private final String discourseSlug;

	public MapEntry(long smfTopicId, long discourseTopicId, String discourseSlug) {
		this.smfTopicId = smfTopicId;
		this.discourseTopicId = discourseTopicId;
		this.discourseSlug = discourseSlug;
	}

	public long getSmfTopicId() {
		return smfTopicId;
	}

	public long getDiscourseTopicId() {
		return discourseTopicId;
	}

	public String getDiscourseSlug() {
		return discourseSlug;
	}

	public static List<MapEntry> getMapEntries(JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.query(MAP_ENTRY_QUERY, new RowMapper());
	}
	
	private static class RowMapper implements
			org.springframework.jdbc.core.RowMapper<MapEntry> {
		public MapEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new MapEntry(rs.getLong("smf_topic_id"),
					rs.getLong("discourse_topic_id"), rs.getString("smf_slug"));
		}
	}
}

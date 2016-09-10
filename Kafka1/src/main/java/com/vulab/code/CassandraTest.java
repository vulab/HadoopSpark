package com.vulab.code;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/*CREATE KEYSPACE vulab
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

use vulab;

CREATE TABLE users(
   username text PRIMARY KEY,
   firstname text,
   lastname text,
   password text   
   );
 * 
 * 
 */

public class CassandraTest {

	public static void main(String[] args) {

		Cluster cluster;
		Session session;
		ResultSet results;
		Row rows;

		cluster = Cluster.builder().addContactPoint("localhost").withPort(9042)
				.withRetryPolicy(DefaultRetryPolicy.INSTANCE).build();
		Metadata md = cluster.getMetadata();
		List<KeyspaceMetadata> keyspacemetadatas = md.getKeyspaces();
		for (KeyspaceMetadata meta : keyspacemetadatas) {
			String name = meta.getName();
			System.out.println("name " + name);
		}
		session = cluster.connect("vulab");

		// Insert one record into the users table
		PreparedStatement statement = session
				.prepare("INSERT INTO users" + "(lastname, username, password, firstname) VALUES (?,?,?,?);");

		BoundStatement boundStatement = new BoundStatement(statement);

		session.execute(boundStatement.bind("Jones", "JJ", "Austin", "Bob"));

		// Use select to get the user we just entered
		Clause clause = QueryBuilder.eq("lastname", "Jones");

		com.datastax.driver.core.querybuilder.Select select = QueryBuilder.select().all().from("vulab", "users");
		select.allowFiltering();
		select.where(clause);

		results = session.execute(select);
		for (Row row : results) {
			System.out.format("%s %s \n", row.getString("firstname"), row.getString("password"));
		}

		// Update the same user with a new age
		Statement update = QueryBuilder.update("vulab", "users").with(QueryBuilder.set("password", "secret"))
				.where((QueryBuilder.eq("username", "JJ")));

		session.execute(update);

		// Select and show the change
		select = QueryBuilder.select().all().from("vulab", "users");

		select.allowFiltering();
		Clause cl = QueryBuilder.eq("lastname", "Jones");
		select.where(cl);
		results = session.execute(select);
		for (Row row : results) {
			System.out.format("%s %s \n", row.getString("firstname"), row.getString("password"));
		}

		// Delete the user from the users table
		Statement delete = QueryBuilder.delete().from("users").where(QueryBuilder.eq("username", "JJ"));
		results = session.execute(delete);

		// Show that the user is gone
		select = QueryBuilder.select().all().from("vulab", "users");
		results = session.execute(select);
		for (Row row : results) {
			System.out.format("%s %s %s %s\n", row.getString("lastname"), row.getString("username"),
					row.getString("password"), row.getString("firstname"));
		}

		// Clean up the connection by closing it
		cluster.close();
	}
}

This project will help you in creating an Nginx mapping file that you
can use to redirect old Simple Machine Forum 2 links to your fresh
Discourse installation.

# 0. Preconditions

Before anything in here is useful, you first need to be able to run
the SMF2 importer that is provided with Discourse:
https://meta.discourse.org/t/importer-for-simple-machines-2-forums/17656

I strongly suggest that you run all of this on a local development
installation and use the backup/restore functionality to move it to the
actual live server.


# 1. Store some additional meta-data

I only wanted to redirect topics and not messages. However, the old topic
IDs are not stored by the importer. This can be easily added. 

Find the "import_posts" method and in there, locate the section where
posts are actually inserted in the database. It starts with "create_posts".

There is a section that defines how everything is mapped into the post
table. Starting with "post =".

Just underneath that section add the following (currently this is line 259)

    post[:custom_fields] ||= {}
    post[:custom_fields]['import_topic_id'] = message[:id_topic]

This will store the SMF topic ID in the custom field of the posts.


# 2. Make Postgres accessible from the outside.

*DO NOT DO THIS ON A SERVER CONNECTED TO THE INTERNET!!*

There are two steps to this process:

Expose the Postgres port in your containers/app.yml:

    expose:
      - "5432:5432" # postgres

And set a password for the postgres user, for instance run "psql" and "\password"


# 3. Run the Java code

You may need to change:

* The exact location and username/password of Postgres in SpringConfiguration
* The format of the original URLs. My forum was hosted in a folder "dvr", which I removed during the migration


# 4. Using the file

Put the created mapping file in /var/nginx/conf.d and add the following section to the bottom
of discourse.conf:

      if ($new) {
        rewrite ^ $new? permanent;
      }
    }
    map $request$is_args$args $new {
      include conf.d/smf2.map;
    }


create table atm_attachment (
	id binary(16) not null,
	category_id varchar(50),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit,
	deleted_date datetime,
	last_accessed_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	multiple bit,
	primary key (id)
) engine=InnoDB;

create table atm_attachment_access_log (
	id bigint not null auto_increment,
	access_type varchar(20),
	accessed_date datetime,
	accessor_id varchar(50),
	accessor_name varchar(50),
	attachment_id binary(16),
	attachment_item_id binary(16),
	content_length bigint,
	content_type varchar(100),
	name varchar(300),
	primary key (id)
) engine=InnoDB;

create table atm_attachment_item (
	id binary(16) not null,
	attachment_id binary(16),
	content_length bigint,
	content_type varchar(100),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	deleted bit,
	deleted_date datetime,
	last_accessed_date datetime,
	name varchar(300),
	storage_key varchar(50),
	primary key (id)
) engine=InnoDB;

create index IDX55aoqwd00x11xb0h5rhfvcvus
	on atm_attachment_access_log (attachment_id);

create index IDX3viggd8m94jgmepo7y8uvkbv2
	on atm_attachment_item (attachment_id);

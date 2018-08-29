alter table atm_attachment_item change removed deleted bit null;
alter table atm_attachment_item change removed_date deleted_date datetime null;
ALTER TABLE atm_attachment_item ADD last_accessed_date datetime NULL;
ALTER TABLE atm_attachment_item ADD created_by_id varchar(50) null;
ALTER TABLE atm_attachment_item ADD created_by_name varchar(50) null;
ALTER TABLE atm_attachment_item ADD created_date datetime null;

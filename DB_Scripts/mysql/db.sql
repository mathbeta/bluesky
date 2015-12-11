-- user info
drop table if exists `user_info`;
create table `user_info` (
	`id` bigint(22) not null auto_increment,
	`user_name` varchar(30),
	`password` varchar(20),
	primary key (`id`),
	unique key `pk_user_info` (`id`)
);

insert into `user_info` (`id`, `user_name`, `password`) values (1, 'admin', 'admin');


-- file info
drop table if exists `file_info`;
create table `file_info` (
	`id` bigint(22) NOT NULL AUTO_INCREMENT,                                 
	`file_name` varchar(300) COMMENT 'real file name on node',  
	`length` bigint(22) DEFAULT '0' COMMENT 'file size in bytes',            
	`user_id` bigint(22),                                       
	`repl_count` int(11),                                       
	`description` text,                                                      
	`original_name` varchar(300) COMMENT 'original file name',  
	`parent_id` bigint(22),                                     
	`created_time` timestamp default now(),
	primary key (`id`),
	unique key `pk_file_info` (`id`)
);


-- node info
drop table if exists `node_info`;
create table `node_info` (
	`id` bigint(22) not null auto_increment,
	`name` varchar(300),
	`ip` varchar(60),
	`isolated` tinyint(1) default 0,
	`transfer_ports` text,
	`storage_root` varchar(1000) comment 'node dir as storage root',
	`status` tinyint(1) default 1 comment 'node status, 1-running, 0-stopped',
	`disk_size` int(11) DEFAULT '0' COMMENT 'unit MB',
	primary key (`id`),
	unique key `pk_node_info` (`id`)
);


-- file node association
drop table if exists `file_node`;
create table `file_node` (
	`file_id` bigint(22),
	`node_id` bigint(22)
);


-- params
drop table if exists `params`;
create table `params` (
	`id` bigint(22) not null auto_increment,
	`param_name` varchar(100),
	`param_value` varchar(200),
	`description` text,
	`type` varchar(10) comment 'param type, console or node',
	primary key (`id`),
	unique key `pk_params` (`id`)
);

insert into `params` (`id`, `param_name`, `param_value`, `description`, `type`) values (1, 'node.storage.dir', '/data/storage', 'node上存放用户数据的根路径', 'node');
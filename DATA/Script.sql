create table buildings (
	building_id integer primary key,
	class_id integer,
	x_coordinate text,
	y_coordinate text,
	nearest_node_code text,
	foreign key (class_id) references classification(class_id)
	)
	
create table classification (
	class_id integer primary key,
	description text,
	building_type text,
	foreign key (building_type) references buildingtypes(building_type)
)

create table buildingtypes (
	building_type integer primary key,
	description text
)

insert into buildingtypes values(0, "residence");
insert into buildingtypes values(-2, "shop");
insert into buildingtypes values(-3, "work");
insert into buildingtypes values(-4, "other");
insert into buildingtypes values(-5, "recreation");
insert into buildingtypes values(-6, "warehouse");
insert into buildingtypes values(-7, "education");
insert into buildingtypes values(-8, "evacuation");

select building_id, x_coordinate, y_coordinate, nearest_node_code, c.class_id, c.description, b2.description 
from buildings b
inner join classification c on b.class_id=c.class_id
inner JOIN buildingtypes b2 on b2.building_type = c.building_type 
where b2.building_type = -8

select nearest_node_code
from buildings b
inner join classification c on b.class_id=c.class_id
inner JOIN buildingtypes b2 on b2.building_type = c.building_type 
where b2.building_type = -8

select * from buildingtypes b 


SELECT * from buildings b 
where class_id=624
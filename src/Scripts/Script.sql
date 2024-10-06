create table buildings (
	class_id integer,
	x_coordinate text,
	y_coordinate text,
	nearest_node_code
	)
	
create table classification (
	code integer,
	description text,
	building_type text
)

select b.class_id, c.description from buildings b
left join classification c on b.class_id=c.code
where class_id=625


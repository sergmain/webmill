

select is_group, id, id_main, absolete, id_item, item 
from    price_list 			
where   id_shop = 3  and absolete = 0 and is_Group=1
order by id_main , id asc
	
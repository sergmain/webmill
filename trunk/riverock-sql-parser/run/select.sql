select id, id_main, id_forum, header, fio, email, text,

       id_thread, ip, date_post

from   main_forum_threads cs

where  id_thread=11 and id_thread=? and "level"=? and id_main=? and

cs.id_main  in 

(

    SELECT id_distance 

    FROM   prr_distance  d,  prr_road_direction  rd 

    WHERE  d.id_road_direction  =  rd.id_road_direction  and 

           rc.id_reper_category=rsh.id_x_reper_category  and 

           r.id_x_reper=rsh.id_x_reper  AND 

           rd.id_road=?  AND 

           rd.id_direction=?

);


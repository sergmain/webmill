declare

count_rec number;exception_name         EXCEPTION; 
too_many_records         EXCEPTION; 

begin

select count(*) into count_rec
from
(
SELECT count(*), a.id_site, a.id_language,a.custom_language
  FROM site_support_language a
  group by a.id_site, a.id_language,a.custom_language
having count(*)>1
);

if (count_rec>0) then
    raise too_many_records;
end if;

end;

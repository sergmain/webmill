insert into wm_portal_ids
(select 'wm_price_shop_list' name, max(ID_SHOP)+1 max_value from wm_price_shop_list)
/

insert into wm_portal_ids
(select 'wm_cash_currency_std' name, max(ID_STD_CURR)+1 max_value from wm_cash_currency_std)
/

insert into wm_portal_ids
(select 'wm_price_shop_precision' name, max(ID_PRICE_SHOP_PRECISION)+1 max_value from wm_price_shop_precision)
/

insert into wm_portal_ids
(select 'wm_cash_curr_value' name, max(ID_CURVAL)+1 max_value from wm_cash_curr_value)
/

insert into wm_portal_ids
(select 'wm_cash_curs_std' name, max(ID_STD_CURS)+1 max_value from wm_cash_curs_std)
/


insert into wm_portal_ids
(select 'wm_price_list' name, max(ID_ITEM)+1 max_value from wm_price_list)
/


insert into wm_portal_ids
(select 'wm_price_import_table' name, max(ID_UPLOAD_PRICE)+1 max_value from wm_price_import_table)
/


insert into wm_portal_ids
(select 'wm_cash_currency' name, max(ID_CURRENCY)+1 max_value from wm_cash_currency)
/




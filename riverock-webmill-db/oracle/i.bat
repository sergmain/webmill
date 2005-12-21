set NLS_LANG=RUSSIAN_CIS.UTF8
imp userid=sys/qqq@mill  file=mill_2003-09-24.dat fromuser=millennium touser=millennium recordlength=100000 buffer=100000 log=backup.log ignore=n rows=y RECALCULATE_STATISTICS=y ANALYZE=y

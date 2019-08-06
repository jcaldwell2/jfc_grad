--Query A



select username from PV.vaulters

MINUS

select distinct username
from

(select * from
(select  username from PV.PERFORMANCES)

cross join

(select meetid from PV.MEETS  

where venuetype = 'Indoor' and substr(meetdate ,8) = '11')


MINUS 

select  username,meetid from PV.performances)


--Query B

-- Had to look up a formatting for PersonalBestInMeters and found To-Char on http://stackoverflow.com/questions/11700546/number-of-decimal-digits-in-oracle-sql-developer-query-result-sheet
 select *
 FROM 
 (select  Rank() OVER (ORDER BY PersonalBestInMeters desc )AS Rank, username, firstname, lastname, TO_CHAR(Personalbestinmeters,'9.99') ,
  (select englishheight from pv.heights where PersonalBestInMeters = pv.heights.metricheight) as PersonalBestInEnglish 
  from
     ( select distinct vault.username, vault.firstname, vault.lastname, 
          max(perf.performance) over(partition by perf.username) as PersonalBestInMeters          
                   FROM PV.Performances Perf  inner join PV.Vaulters Vault
                   ON Perf.username = Vault.username
                   order by personalbestinmeters desc)
                   
inner join PV.heights  Hei
ON personalbestinmeters = Hei.metricheight)
where Rank < 11


-- Query C


-- Could get extract the year properly from the meetdate so I used concatentation from https://docs.oracle.com/cd/B28359_01/server.111/b28286/operators003.htm

Select  username, firstname, lastname, '20' || substr(meetdate,8), venuetype, gender, performance
FROM PV.Meets
natural inner join PV.performances
NATURAL inner join PV.vaulters
where substr(meetname ,1 , 3) = 'ACC'
order by meetdate, venuetype




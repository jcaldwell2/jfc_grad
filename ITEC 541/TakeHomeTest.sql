----------------------------------
-- QUERY 1
----------------------------------


SELECT * 
FROM HR.JOBS j
INNER JOIN HR.EMPLOYEES e
ON j.JOB_ID = e.JOB_ID
WHERE  e.SALARY > J.max_salary or e.SALARY < J.min_salary;



---------------------------------------------
--QUERY 2
-- Note: I only pulled jobs where there were
-- male and female employees otherwise
-- there wouldn't be a proper comparison
-- and it does appear to have some discrimination goin on as well
----------------------------------------------


SELECT job_id, ROUND(Femalehours,1) as AVG_F_Hours,Female_Sal, ROUND(ManHours,1) as AVG_M_Hours, Male_Sal, F_minus_M as Difference FROM(
    SELECT job_id, (FemaleTime - sysdate) FemaleHours, (MaleTime - sysdate) ManHours,Female_Sal, Male_Sal, (Female_Sal - Male_Sal)as F_minus_M FROM(
    SELECT m.job_id, MAX(f.fdate) as FemaleTime, MAX(m.mdate) as MaleTime, avg(F_Salary) as Female_Sal, avg(M_Salary) as Male_Sal
    FROM(
          (SELECT JOB_ID, hire_date ,(round(hire_date,'year'))as mdate,  ROUND(AVG(Salary) OVER (partition by JOB_ID))as M_Salary, gender
          FROM HR.employees 
          WHERE gender = 'M'  
          )M
    INNER JOIN
          (SELECT job_ID, (round(hire_date,'year')) as FDATE, ROUND(AVG(Salary) OVER (partitiON by JOB_ID))as F_Salary, gender
          FROM HR.employees 
          WHERE gender = 'F') F
          ON m.JOB_ID = f.JOB_ID ) 
    GROUP BY m.job_id
    )
);


----------------------------------
-- QUERY 3
----------------------------------

select  EmpId, FirstJob, PastAvgSal, CurJob, CurAvgSal, Salary from (


Select EmpId, FirstJob, PastAvgSal, salary 

FROM 
(
Select EmpID, FirstJob,((max_salary -min_salary)/2) as PastAvgSal From (

Select X.Employee_ID as EmpID, Y.Job_ID as FirstJob
FROM (
Select Employee_ID
FROM
HR.EMPLOYEES

minus


Select Employee_ID FROM
    (select E.EMPLOYEE_ID, E.JOB_ID, Rank() OVER(partition by e.employee_id order by Start_date) as Rank
  FROM HR.JOB_HISTORY e)
   where rank = 1
) X
INNER JOIN 
HR.EMPLOYEES Y
ON y.Employee_ID = x.Employee_ID

UNION


Select Employee_ID,Job_ID FROM
    (select E.EMPLOYEE_ID, E.JOB_ID, Rank() OVER(partition by e.employee_id order by Start_date) as Rank
  FROM HR.JOB_HISTORY e)
   where rank = 1

) tt
inner join
(select job_id, min_salary, max_salary
from hr.jobs) ts
On tt.FirstJob = ts.job_id

) FIN


inner join

(select employee_id, Salary from hr.employees
  ) AlLY

on fin.empid = ally.employee_id


) again

inner join

(select xx.employee_id, xx.job_id as CurJob, ((max_salary -min_salary)/2) as CurAvgSal
from hr.employees xx
inner join
(select job_id, min_salary, max_salary from hr.jobs) yy 
on xx.job_id = yy.job_id) ww
ON again.empid = ww.employee_id

order by empid asc;
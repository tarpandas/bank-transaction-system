create or replace procedure "CHECK_BAL"
(a IN NUMBER,
bal OUT NUMBER)
is
begin
select balance into bal from Customers where Customers.accountno=a;
end;

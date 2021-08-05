create or replace procedure "SET_CUS"
(a IN OUT VARCHAR2,
b IN OUT  NUMBER,
c IN OUT NUMBER)
is
begin
insert into customers values (a, b, c);
end;

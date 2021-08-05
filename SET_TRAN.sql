create or replace procedure "SET_TRAN"
(a IN OUT NUMBER,
b IN OUT NUMBER,
c IN OUT NUMBER)
is
begin
UPDATE Customers
   SET balance = CASE accountno
                      WHEN a THEN balance - c
                      WHEN b THEN balance + c
                      ELSE balance
                      END
 WHERE accountno IN(a,b);
end;

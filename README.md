# bookstore
JEE Book Store Project

#Fragen
1. Meinung zu ConstructorExpressions vs. NativeQuery - ResultSetMapping
 - Egal, bevorzugt ConstructorExpressions -> als Ausnahme pure SQL
2. Testdaten Vorgaben
 - Egal
3. Uni- oder Bydirektionale Verbindungen
 - analog Modell d.h. undirektional
4. Modell erweitern? DTO's
 - eher nicht erwünscht, allerdings für Statistik-Query ok
5. Wann DTO wann Entity zurückgeben.
 - Immer DTO zurückgeben.
6. Kaskadierungen und Fetch-Typs 
 - Überlegungen machen bzw. begründen, OrderItem eager laden, 
 Kaskadierung Order-Customer sicher unterschiedlich gegenüber Order-OrderItem

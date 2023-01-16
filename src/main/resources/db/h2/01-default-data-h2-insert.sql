--liquibase formatted sql
--changeset mich:1

INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Uszkodzona jezdnia','fake@mail.com');
INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Uszkodzony chodnik','fake@mail.com');
INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Uszkodzone oznakowanie','fake@mail.com');
INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Uszkodzona sygnalizacja','fake@mail.com');
INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Uszkodzone oświetlenie','fake@mail.com');
INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Nielegalne wysypisko','fake@mail.com');
INSERT INTO report_type(active,created,updated,description,email) VALUES (true,now(),now(),'Palenie śmieci','fake@mail.com');

INSERT INTO REPORT_STATUS(created,updated,status_code,description,color,stage) VALUES (now(),now(),1,'Oczekuję','#FF0000','WAITING');
INSERT INTO REPORT_STATUS(created,updated,status_code,description,color,stage) VALUES (now(),now(),2,'Weryfikacja','#FFF333','MIDDLE');
INSERT INTO REPORT_STATUS(created,updated,status_code,description,color,stage) VALUES (now(),now(),3,'Realizacja','#333CFF','MIDDLE');
INSERT INTO REPORT_STATUS(created,updated,status_code,description,color,stage) VALUES (now(),now(),4,'Zakończone','#00FF00','FINISHED');
INSERT INTO REPORT_STATUS(created,updated,status_code,description,color,stage) VALUES (now(),now(),5,'Odrzucone','#000000','FINISHED');
INSERT INTO REPORT_STATUS(created,updated,status_code,description,color,stage) VALUES (now(),now(),6,'Przekazane innej jednostce','#FF33F6','FINISHED');

INSERT INTO USERS(created,updated,enabled,username,password,user_role,email) VALUES(now(),now(),true,'user','$2a$10$U61vXrAb0IwB4x2fo8Z.1es5.K7kCy0KHi1wFxbdjHWqa8Hh9VeyK','ROLE_USER','useri@mail.com');
INSERT INTO USERS(created,updated,enabled,username,password,user_role,email) VALUES(now(),now(),true,'admin','$2a$10$mm/AxR2CvIWU5z/9yWzpKuCJXtouY1WhWbmbfybPDSfuRpvrR7q.G','ROLE_ADMIN','admin@mail.com');

INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_enabled_boolean','false','Powiadomienia włączone');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_host',null,'Adres Email Host');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_port','25','Email Port');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_username',null,'Użytkownik');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_password',null,'Hasło');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_transport_protocol','smtp','Protokół');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_smtp_auth_boolean','true','SMTP AUTH');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_smtp_starttls_enable_boolean','true','SMTP start TTLS');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'mail_debug_boolean','false', 'Email Debug');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_logo_file',null,'Logo firmy');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_name','Twoja nazwa firmy','Nazwa firmy');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_email','adrespocztyemail@email.com','Adres email firmy');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_www','twojastrona.pl','Adres strony WWW');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_rodo_info','Tutaj wprowdź informację RODO','Deklaracja RODO');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_json_borders_file',null,'Plik geojson (wgs84) granice zakresu');
INSERT INTO SETTINGS(created,updated,key,value,display_name) VALUES (now(),now(),'company_json_roads_file',null,'Plik geojson (wgs84) dróg pod zarządem');
CREATE TABLE earth(
	num number(4) primary key,
	SGG_NM varchar2(10 char) not null,
	EQUP_NM varchar2(35 char) not null,
	LOC_SFPR_A varchar2(40 char) not null,
	XCORD number(9,6) not null,
	YCORD number(8,6) not null
);

CREATE SEQUENCE earth_seq;
DROP SEQUENCE earth_seq;

DROP TABLE earth cascade constraint purge;

INSERT INTO earth_check VALUES(earth_seq,'가나다','마바사','서울시행복동',127.000015,63.000134);

insert into earth VALUES(earth_seq.nextval,'A','B','서울시행복동',123.06,63.183);

SELECT * FROM earth;

TRUNCATE TABLE earth;

SELECT * FROM earth ORDER BY SGG_NM ASC;
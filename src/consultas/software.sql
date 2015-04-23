DROP PROCEDURE IF EXISTS consultar_software_aulas;
DELIMITER //
CREATE PROCEDURE consultar_software_aulas()
BEGIN
/*Muestra el SW que tiene cada una de las aulas. En el caso de que el aula no tenga nada cargado, no la muestra*/
SELECT a.ATTRIBUTE_VALUE, b.ATTRIBUTE_VALUE
FROM resource_attribute_value as a JOIN resource_attribute_value as b
ON a.RESOURCE_ID = b.RESOURCE_ID
WHERE a.ATTRIBUTE_KEY <> b.ATTRIBUTE_KEY
and b.ATTRIBUTE_KEY = 'a2'
group by a.RESOURCE_ID
order by a.ATTRIBUTE_VALUE;
END //

DROP PROCEDURE IF EXISTS consultar_software_cursos_anual;
DELIMITER //
CREATE PROCEDURE consultar_software_cursos_anual(
IN id_nombre_materia INT(11),
IN anio YEAR,
IN software VARCHAR(20)
)
BEGIN
SELECT  a.ID, a.APPOINTMENT_START, eav.ATTRIBUTE_KEY, eav.ATTRIBUTE_VALUE, eav2.ATTRIBUTE_KEY, eav2.ATTRIBUTE_VALUE
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
	INNER JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    INNER JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    INNER JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
	INNER JOIN event_attribute_value eav2 ON eav.EVENT_ID = eav2.EVENT_ID
	WHERE eav2.ATTRIBUTE_KEY = software
	AND eav.ATTRIBUTE_VALUE = id_nombre_materia
	AND YEAR(a.APPOINTMENT_START) = anio
	group by eav.EVENT_ID;
END
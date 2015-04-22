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


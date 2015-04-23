USE rapla;

DROP PROCEDURE IF EXISTS consultar_aula_por_nombre;
DELIMITER //
CREATE PROCEDURE consultar_aula_por_nombre(
    IN nombre_aula VARCHAR(4)
)
BEGIN
SELECT * FROM rapla.resource_attribute_value AS rav
WHERE rav.RESOURCE_ID = (
	SELECT rav1.RESOURCE_ID FROM rapla.resource_attribute_value AS rav1
	WHERE rav1.ATTRIBUTE_KEY = "name" 
	AND rav1.ATTRIBUTE_VALUE = nombre_aula
);
END //



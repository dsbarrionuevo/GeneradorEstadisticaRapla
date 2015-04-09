USE rapla;

DROP PROCEDURE IF EXISTS consultar_cantidad_diplomaturas_por_anio_y_mes;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_diplomaturaas_por_anio_y_mes
(
    IN idNombreMateria INT(11),
    IN fecha DATE,
    IN anio YEAR,
    IN mes INT(11)
)
BEGIN
SELECT COUNT(*) AS cantidad_diplomaturas FROM (
    SELECT * 
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    JOIN rapla.category c on c.ID = eav.ATTRIBUTE_VALUE 
    WHERE a.REPETITION_TYPE = "weekly" AND /* las citas que se repiten semanalmente supongo que son cursos */
    /* filtros en la tabla de citas */
    YEAR(a.APPOINTMENT_START) = anio AND 
    MONTH(a.appointment_start) <= mes AND mes <= MONTH(a.repetition_end) AND 
    /* filtros en la tabla de atributos de evento */
    /* filtro que tenga un curso cargado, pues existen cosas como seminarios que son semanales tamb... */
    /* filtro por materia*/
    c.LABEL like "Diplo%"
    /* filtro sin importar la cantidad de dias a la semana que viene */
    GROUP BY a.event_id
) AS x;
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_diplomaturas_por_anio;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_diplomaturaas_por_anio
(
    IN idNombreMateria INT(11),
    IN fecha DATE,
    IN anio YEAR
)
BEGIN
SELECT COUNT(*) AS cantidad_diplomaturas FROM (
    SELECT * 
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    JOIN rapla.category c on c.ID = eav.ATTRIBUTE_VALUE 
    WHERE a.REPETITION_TYPE = "weekly" AND /* las citas que se repiten semanalmente supongo que son cursos */
    /* filtros en la tabla de citas */
    YEAR(a.APPOINTMENT_START) = anio AND 
    /* filtros en la tabla de atributos de evento */
    /* filtro que tenga un curso cargado, pues existen cosas como seminarios que son semanales tamb... */
    /* filtro por materia*/
    c.LABEL like "Diplo%"
    /* filtro sin importar la cantidad de dias a la semana que viene */
    GROUP BY a.event_id
) AS x;
END //

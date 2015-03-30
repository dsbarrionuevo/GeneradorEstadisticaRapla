USE rapla;

DROP PROCEDURE IF EXISTS consultar_categorias_por_categoria_padre;
DELIMITER //
CREATE PROCEDURE consultar_categorias_por_categoria_padre(
    IN idCategoriaPadre INT(11)
)
BEGIN
SELECT * FROM rapla.category where parent_id=idCategoriaPadre;
END //

DROP PROCEDURE IF EXISTS consultar_materias_sistemas;
DELIMITER //
CREATE PROCEDURE consultar_materias_sistemas(
)
BEGIN
CALL consultar_categorias_por_categoria_padre(568);
END //

/*
DELIMITER //
CREATE PROCEDURE consultar_cursos_por_materias_por_anio(
    IN anio YEAR
)
CALL consultar_materias_sistemas()
DECLARE creditlim double;

SELECT creditlimit INTO creditlim
FROM customers
WHERE customerNumber = p_customerNumber;
BEGIN
END //
*/

DROP PROCEDURE IF EXISTS consultar_cantidad_citas_por_materia_anio_mes_inicio_fin;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_citas_por_materia_anio_mes_inicio_fin(
    IN idNombreMateria INT(11),
    IN anio YEAR,
    IN mes_inicio INT(11),
    IN mes_fin INT(11)
)
BEGIN
SELECT COUNT(*) AS cantidad_citas FROM (
    SELECT COUNT(a.event_id) AS cantidad_repeticiones_por_semana 
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    WHERE a.REPETITION_TYPE = "weekly" AND /* las citas que se repiten semanalmente supongo que son cursos */
    /* filtros en la tabla de citas */
    YEAR(a.APPOINTMENT_START) = anio AND 
    MONTH(a.appointment_start) > mes_inicio AND MONTH(a.repetition_end) < mes_fin AND 
    /* filtros en la tabla de atributos de evento */
    /* filtro que tenga un curso cargado, pues existen cosas como seminarios que son semanales tamb... */
    /*
    eav.ATTRIBUTE_KEY = "a5" AND 
    eav.ATTRIBUTE_VALUE IS NOT NULL AND
    */
    /* filtro por materia*/
    eav.ATTRIBUTE_KEY = "title" AND 
    eav.ATTRIBUTE_VALUE = idNombreMateria 
    /* filtro sin importar la cantidad de dias a la semana que viene */
    GROUP BY a.event_id
) AS x;
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_citas_por_materia_anio;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_citas_por_materia_anio(
    IN idNombreMateria INT(11),
    IN anio YEAR
)
BEGIN
CALL consultar_cantidad_citas_por_materia_anio_mes_inicio_fin(
    idNombreMateria, 
    anio,
    0,
    12
);
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_citas_por_materia_anio_primer_cuatrimestre;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_citas_por_materia_anio_primer_cuatrimestre(
    IN idNombreMateria INT(11),
    IN anio YEAR
)
BEGIN
CALL consultar_cantidad_citas_por_materia_anio_mes_inicio_fin(
    idNombreMateria, 
    anio,
    0,
    8
);
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_citas_por_materia_anio_segundo_cuatrimestre;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_citas_por_materia_anio_segundo_cuatrimestre(
    IN idNombreMateria INT(11),
    IN anio YEAR
)
BEGIN
CALL consultar_cantidad_citas_por_materia_anio_mes_inicio_fin(
    idNombreMateria, 
    anio,
    6,
    12
);
END //

/*
DELIMITER //
CREATE PROCEDURE consultar_materias_sistemas_2015(
    
)
BEGIN
END //
*/
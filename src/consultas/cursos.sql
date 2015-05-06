USE rapla;

DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_materia_anio_mes_inicio_mes_fin;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_materia_anio_mes_inicio_mes_fin(
    IN id_nombre_materia INT(11),
    IN anio YEAR,
    IN mes_inicio INT(11),
    IN mes_fin INT(11)
)
BEGIN
SELECT COUNT(*) AS cantidad_citas FROM (
    SELECT COUNT(a.event_id) AS cantidad_repeticiones_por_semana
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    INNER JOIN rapla.category AS c ON eav.ATTRIBUTE_VALUE = c.id 
    WHERE 
    a.REPETITION_TYPE = "weekly" AND /* las citas que se repiten semanalmente supongo que son cursos */
    /* tenemos que evitar filtrar por 'weekly', mejor es filtra por comision no null, pero esto tene la contra que a5 no siempre se refiere a comision */
    /* filtro que tenga un curso cargado, pues existen cosas como seminarios que son semanales tamb... */
    /* a.event_id = (
	SELECT eav2.event_id
	FROM event_attribute_value AS eav2 
	WHERE eav2.event_id = a.event_id AND 
	eav2.ATTRIBUTE_KEY = "a5" AND 
	eav2.ATTRIBUTE_VALUE IS NOT NULL 
    ) AND */
    /* filtros en la tabla de citas */
    YEAR(a.APPOINTMENT_START) = anio AND 
    MONTH(a.appointment_start) >= mes_inicio AND MONTH(a.repetition_end) <= mes_fin AND 
    /* filtros en la tabla de atributos de evento */
    /* filtro por materia*/
    eav.ATTRIBUTE_KEY = "especialidad" AND 
    eav.ATTRIBUTE_VALUE = id_nombre_materia 
    /* filtro sin importar la cantidad de dias a la semana que viene */
    GROUP BY a.event_id
) AS x;
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_materia_anio;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_materia_anio(
    IN id_nombre_materia INT(11),
    IN anio YEAR
)
BEGIN
CALL consultar_cantidad_cursos_por_materia_anio_mes_inicio_mes_fin(
    id_nombre_materia, 
    anio,
    1,
    12
);
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_materia_anio_primer_cuatrimestre;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_materia_anio_primer_cuatrimestre(
    IN id_nombre_materia INT(11),
    IN anio YEAR
)
BEGIN
CALL consultar_cantidad_cursos_por_materia_anio_mes_inicio_mes_fin(
    id_nombre_materia, 
    anio,
    1,
    7
);
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_materia_anio_segundo_cuatrimestre;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_materia_anio_segundo_cuatrimestre(
    IN id_nombre_materia INT(11),
    IN anio YEAR
)
BEGIN
CALL consultar_cantidad_cursos_por_materia_anio_mes_inicio_mes_fin(
    id_nombre_materia, 
    anio,
    7,
    12
);
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_materia_anio_mes;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_materia_anio_mes(
    IN id_nombre_materia INT(11),
    IN anio YEAR,
    IN mes INT(11)
)
BEGIN
SELECT COUNT(*) AS cantidad_citas FROM (
    SELECT COUNT(a.event_id) AS cantidad_repeticiones_por_semana
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    INNER JOIN rapla.category AS c ON eav.ATTRIBUTE_VALUE = c.id 
    WHERE 
    a.REPETITION_TYPE = "weekly" AND /* las citas que se repiten semanalmente supongo que son cursos */
    /* tenemos que evitar filtrar por 'weekly', mejor es filtra por comision no null, pero esto tene la contra que a5 no siempre se refiere a comision */
    /* filtro que tenga un curso cargado, pues existen cosas como seminarios que son semanales tamb... */
    /* a.event_id = (
	SELECT eav2.event_id
	FROM event_attribute_value AS eav2 
	WHERE eav2.event_id = a.event_id AND 
	eav2.ATTRIBUTE_KEY = "a5" AND 
	eav2.ATTRIBUTE_VALUE IS NOT NULL 
    ) AND */
    /* filtros en la tabla de citas */
    YEAR(a.APPOINTMENT_START) = anio AND 
    /*MONTH(a.appointment_start) >= 1 AND MONTH(a.repetition_end) <= 8 AND *//*original*/
    /*MONTH(a.appointment_start) = 1 AND *//*comienza en...*/
    /*MONTH(a.repetition_end) = 12 AND *//*termina en...*/
    MONTH(a.appointment_start) <= mes AND MONTH(a.repetition_end) >= mes AND /*esta en tal fecha... y no termino aun*/
    /* filtros en la tabla de atributos de evento */
    /* filtro por materia*/
    eav.ATTRIBUTE_KEY = "title" AND 
    eav.ATTRIBUTE_VALUE = id_nombre_materia 
    /* filtro sin importar la cantidad de dias a la semana que viene */
    GROUP BY a.event_id
) AS x;
END //


DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_aula_anio;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_aula_anio(
    IN nombre_aula CHAR(2),
    IN anio YEAR
)
BEGIN
SELECT count(*) FROM (
    SELECT count(*)
    FROM rapla.appointment AS ap
    INNER JOIN allocation AS al ON ap.id = al.APPOINTMENT_ID 
    INNER JOIN rapla_resource rr ON rr.id = al.RESOURCE_ID 
    INNER JOIN resource_attribute_value AS rav ON rav.RESOURCE_ID = rr.id 
    WHERE 
    rav.ATTRIBUTE_KEY = "name" AND 
    rav.ATTRIBUTE_VALUE = nombre_aula AND 
    YEAR(ap.APPOINTMENT_START) = anio 
    GROUP BY event_id
) AS x;
END //

DROP PROCEDURE IF EXISTS consultar_cantidad_citas_por_materia_anio_mes_inicio_mes_fin;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_citas_por_materia_anio_mes_inicio_mes_fin(
    IN id_nombre_materia INT(11),
    IN anio YEAR,
    IN mes_inicio INT(11),
    IN mes_fin INT(11)
)
BEGIN
SELECT COUNT(*) AS cantidad_citas FROM (
SELECT count(*)
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    INNER JOIN rapla.category AS c ON eav.ATTRIBUTE_VALUE = c.id 


    WHERE 

        a.REPETITION_TYPE = "weekly" AND 
        a.event_id = (
            SELECT eav2.event_id
            FROM event_attribute_value AS eav2 
            WHERE eav2.event_id = a.event_id AND 
            eav2.ATTRIBUTE_KEY = "curso" AND 
            eav2.ATTRIBUTE_VALUE IS NOT NULL 
            ) AND 
        a.event_id = (
            SELECT eav3.event_id
            FROM event_attribute_value AS eav3 
            WHERE eav3.event_id = a.event_id AND 
            eav3.ATTRIBUTE_KEY = "especialidad" AND 
            eav3.ATTRIBUTE_VALUE = id_nombre_materia
            ) AND 
	a.event_id = (
            SELECT eav4.event_id
            FROM event_attribute_value AS eav4
            WHERE eav4.event_id = a.event_id AND 
            eav4.ATTRIBUTE_KEY = "estadoReserva" AND 
            (eav4.ATTRIBUTE_VALUE ='259' OR 
            eav4.ATTRIBUTE_VALUE ='260')
            ) AND 
	
	eav.ATTRIBUTE_KEY = "curso" AND 
        YEAR(a.APPOINTMENT_START) = anio AND 
        MONTH(a.appointment_start) >= mes_inicio AND MONTH(a.appointment_start) <= mes_fin 
    GROUP BY  eav.ATTRIBUTE_VALUE
) AS x;
END //

DROP PROCEDURE IF EXISTS consultar_cantidad_alumnos_curso_y_aula_anual;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_alumnos_curso_y_aula_anual(
    IN id_nombre_materia INT(11),
    IN anio YEAR
)
BEGIN
SELECT  a.ID, a.APPOINTMENT_START, eav.ATTRIBUTE_KEY, eav.ATTRIBUTE_VALUE, eav2.ATTRIBUTE_KEY, eav2.ATTRIBUTE_VALUE, eav3.ATTRIBUTE_KEY, eav3.ATTRIBUTE_VALUE
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
	INNER JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    INNER JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    INNER JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
	INNER JOIN event_attribute_value eav2 ON eav.EVENT_ID = eav2.EVENT_ID
	INNER JOIN event_attribute_value eav3 ON eav.EVENT_ID = eav3.EVENT_ID
	WHERE eav2.ATTRIBUTE_KEY = 'curso'
	AND eav.ATTRIBUTE_VALUE = id_nombre_materia
	AND eav3.ATTRIBUTE_KEY = 'cantidadAlumnos'
	AND YEAR(a.APPOINTMENT_START) = anio
	group by eav.EVENT_ID;
END

DROP PROCEDURE IF EXISTS consultar_cursos_por_materia_por_anio;
DELIMITER //
CREATE PROCEDURE consultar_cursos_por_materia_por_anio
(
IN id_materia INT(11),
IN anio YEAR
)
BEGIN
SELECT  a.ID, a.APPOINTMENT_START, eav.ATTRIBUTE_KEY, eav.ATTRIBUTE_VALUE as idMateria, eav2.ATTRIBUTE_KEY, eav2.ATTRIBUTE_VALUE as curso
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
	INNER JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    INNER JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    INNER JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
	INNER JOIN event_attribute_value eav2 ON eav.EVENT_ID = eav2.EVENT_ID
	INNER JOIN event_attribute_value eav3 ON eav.EVENT_ID = eav3.EVENT_ID
	WHERE eav2.ATTRIBUTE_KEY = 'curso'
	AND eav.ATTRIBUTE_VALUE = id_materia
	AND YEAR(a.APPOINTMENT_START) = anio
	group by eav.EVENT_ID;
END


DROP PROCEDURE IF EXISTS consultar_cantidad_alumnos_curso_especifico;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_alumnos_curso_especifico(
    IN id_nombre_materia INT(11),
	IN curso VARCHAR(50),
    IN anio YEAR
)
BEGIN
SELECT  eav3.ATTRIBUTE_VALUE as cantidadAlumnos
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    INNER JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
	INNER JOIN event_attribute_value eav2 ON eav.EVENT_ID = eav2.EVENT_ID
	INNER JOIN event_attribute_value eav3 ON eav.EVENT_ID = eav3.EVENT_ID
	WHERE eav2.ATTRIBUTE_KEY = 'curso'
	AND eav.ATTRIBUTE_VALUE = id_nombre_materia
	AND eav2.ATTRIBUTE_VALUE = curso
	AND eav3.ATTRIBUTE_KEY = 'cantidadAlumnos'
	AND YEAR(a.APPOINTMENT_START) = anio
	group by eav.EVENT_ID;
END

/*
//Union de cursas con y sin cantidadAlumnos.
SELECT * 
    FROM rapla.appointment AS a 
    RIGHT JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
	WHERE 1=1 
	AND a.event_id = (
		SELECT eav2.event_id
		FROM event_attribute_value AS eav2 
		WHERE eav2.event_id = a.event_id AND 
		eav2.ATTRIBUTE_KEY = "especialidad" AND 
		eav2.ATTRIBUTE_VALUE = '572'
	) 
	AND eav.attribute_key = "cantidadAlumnos" 
	AND YEAR(a.APPOINTMENT_START) = 2015
	group by eav.EVENT_ID
	UNION
	SELECT *
    FROM rapla.appointment AS a2 
    RIGHT JOIN rapla.event_attribute_value AS eav ON a2.event_id = eav.event_id 
	WHERE 1=1 
	AND a2.event_id = (
		SELECT eav2.event_id
		FROM event_attribute_value AS eav2 
		WHERE eav2.event_id = a2.event_id AND 
		eav2.ATTRIBUTE_KEY = "especialidad" AND 
		eav2.ATTRIBUTE_VALUE = '572'
	) 
	AND YEAR(a2.APPOINTMENT_START) = 2015 
	group by eav.EVENT_ID



*/
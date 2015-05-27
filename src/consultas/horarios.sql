USE rapla;

DROP PROCEDURE IF EXISTS consultar_total_horas_por_fecha_hora_inicio_fin;
DELIMITER //
CREATE PROCEDURE consultar_total_horas_por_fecha_hora_inicio_fin(
    IN fecha DATE,
    IN hora_inicio TIME,
    IN hora_fin TIME
)
BEGIN
SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(total_horas_por_dia))) AS horas_lunes FROM (
    SELECT TIMEDIFF(a.APPOINTMENT_END, a.APPOINTMENT_START) AS total_horas_por_dia
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
    WHERE rav.ATTRIBUTE_KEY = "name" 
    AND eav.ATTRIBUTE_KEY = "especialidad" 
    AND ((fecha BETWEEN a.APPOINTMENT_START AND a.REPETITION_END) OR (DATE_FORMAT(a.APPOINTMENT_START, '%Y-%m-%d') = fecha))
    AND DATE_FORMAT(a.APPOINTMENT_START,'%T') BETWEEN hora_inicio AND hora_fin
    AND DAYNAME(a.APPOINTMENT_START) = DAYNAME(fecha)
    AND a.ID NOT IN(
        SELECT ae.APPOINTMENT_ID 
        FROM appointment_exception ae 
        WHERE DATE(ae.EXCEPTION_DATE) = fecha
    )
) AS x;
END //



DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_por_rango_horario_fecha;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_por_rango_horario_fecha(
    IN fecha DATE,
    IN hora_inicio TIME,
    IN hora_fin TIME
)
BEGIN
    SELECT COUNT(*) AS cantidad_cursos
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
    WHERE rav.ATTRIBUTE_KEY = "name" 
    AND eav.ATTRIBUTE_KEY = "especialidad" 
    AND fecha BETWEEN a.APPOINTMENT_START AND a.REPETITION_END 
    AND ((DATE_FORMAT(a.APPOINTMENT_START,'%T') BETWEEN hora_inicio AND SUBTIME(hora_fin,'00:01:00')) OR (DATE_FORMAT(a.APPOINTMENT_END,'%T') < SUBTIME(hora_fin,'00:01:00')))
    AND DAYNAME(a.APPOINTMENT_START) = DAYNAME(fecha)
    AND a.ID NOT IN(
        SELECT ae.APPOINTMENT_ID 
        FROM appointment_exception ae 
        WHERE DATE(ae.EXCEPTION_DATE) = fecha
    );
END //

/*
DROP PROCEDURE IF EXISTS consultar_total_horas_entre_fecha_inicio_fin;
DELIMITER //
CREATE PROCEDURE consultar_total_horas_entre_fecha_inicio_fin(
    IN fecha_inicio DATE,
    IN fecha_fin DATE
)
BEGIN
DECLARE _total_horas_fecha_inicio DECIMAL 
CALL consultar_total_horas_por_fecha_hora_inicio_fin(fecha_inicio, '08:00:00','23:00:00',@total_horas_fecha_inicio);
SELECT @total_horas_fecha_inicio IN @_total_horas_fecha_inicio;
END //
*/

DROP PROCEDURE IF EXISTS consultar_total_horas_por_dia;
DELIMITER //
CREATE PROCEDURE consultar_total_horas_por_dia(
    IN dia VARCHAR(50),
    IN anio YEAR,
    IN mes_inicio INT(11),
    IN mes_fin INT(11)
)
BEGIN
SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(total_horas_por_dia))) AS horasTotalesDia, count(*) as cantidadDias FROM (
    SELECT TIMEDIFF(a.APPOINTMENT_END, a.APPOINTMENT_START) AS total_horas_por_dia
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
    WHERE rav.ATTRIBUTE_KEY = "name" 
    AND eav.ATTRIBUTE_KEY = "especialidad" 
	AND YEAR(a.APPOINTMENT_START) = anio
	AND ((MONTH(a.appointment_start) >= mes_inicio AND MONTH(a.repetition_end) <= mes_fin) OR ((MONTH(a.appointment_start) >= mes_inicio AND MONTH(a.APPOINTMENT_END) <= mes_fin) )) /*esta en tal fecha... y no termino aun*/
    AND DATE_FORMAT(a.APPOINTMENT_START,'%T') BETWEEN '08:00:00' AND '23:00:00'
    AND DAYNAME(a.APPOINTMENT_START) = dia
    AND a.ID NOT IN(
        SELECT ae.APPOINTMENT_ID
        FROM appointment_exception ae 
        WHERE DAYNAME(ae.EXCEPTION_DATE) = dia
    )
) AS x;
END

DROP PROCEDURE IF EXISTS consultar_total_horas_por_dia_anio;
DELIMITER //
CREATE PROCEDURE consultar_total_horas_por_dia_anio(
    IN dia VARCHAR(50),
    IN anio YEAR
)
BEGIN
CALL consultar_total_horas_por_dia(
    dia, 
    anio,
    1,
    12
);
END //


DROP PROCEDURE IF EXISTS consultar_periodos;
DELIMITER //
CREATE PROCEDURE consultar_periodos(
)
BEGIN
SELECT  *
FROM period
ORDER BY PERIOD_START DESC;
END //

DROP PROCEDURE IF EXISTS consultar_total_horas_por_fecha_hora_todo_el_dia;
DELIMITER //
CREATE PROCEDURE consultar_total_horas_por_fecha_hora_todo_el_dia(
    IN fecha DATE
)
BEGIN
SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(total_horas_por_dia))) AS horas_lunes FROM (
    SELECT TIMEDIFF(a.APPOINTMENT_END, a.APPOINTMENT_START) AS total_horas_por_dia
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
    JOIN allocation al ON al.APPOINTMENT_ID = a.ID
    JOIN rapla.category c ON c.ID = eav.ATTRIBUTE_VALUE 
    JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
    WHERE rav.ATTRIBUTE_KEY = "name" 
    AND eav.ATTRIBUTE_KEY = "especialidad" 
    AND ((fecha BETWEEN a.APPOINTMENT_START AND a.REPETITION_END) OR (DATE_FORMAT(a.APPOINTMENT_START, '%Y-%m-%d') = fecha))
    AND DATE_FORMAT(a.APPOINTMENT_START,'%T') BETWEEN '08:00:00' AND '23:00:00'
    AND DAYNAME(a.APPOINTMENT_START) = DAYNAME(fecha)
    AND a.ID NOT IN(
        SELECT ae.APPOINTMENT_ID 
        FROM appointment_exception ae 
        WHERE DATE(ae.EXCEPTION_DATE) = fecha
    )
) AS x;
END //
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
    AND fecha BETWEEN a.APPOINTMENT_START AND a.REPETITION_END
    AND DATE_FORMAT(a.APPOINTMENT_START,'%T') BETWEEN hora_inicio AND hora_fin
    AND DAYNAME(a.APPOINTMENT_START) = DAYNAME(fecha)
    AND a.ID NOT IN(
        SELECT ae.APPOINTMENT_ID 
        FROM appointment_exception ae 
        WHERE DATE(ae.EXCEPTION_DATE) = fecha
    )
) AS x;
END //

DROP PROCEDURE IF EXISTS consultar_cantidad_cursos_rango_horario_fecha;
DELIMITER //
CREATE PROCEDURE consultar_cantidad_cursos_rango_horario_fecha(
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
    AND (DATE_FORMAT(a.APPOINTMENT_START,'%T') > hora_inicio AND DATE_FORMAT(a.APPOINTMENT_START,'%T') < hora_fin
	OR DATE_FORMAT(a.APPOINTMENT_END,'%T') > hora_inicio AND DATE_FORMAT(a.APPOINTMENT_END,'%T') < hora_fin)
    AND DAYNAME(a.APPOINTMENT_START) = DAYNAME(fecha)
    AND a.ID NOT IN(
        SELECT ae.APPOINTMENT_ID 
        FROM appointment_exception ae 
        WHERE DATE(ae.EXCEPTION_DATE) = fecha
    );
END

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
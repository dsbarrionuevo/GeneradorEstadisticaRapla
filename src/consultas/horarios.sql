USE rapla;

SELECT SUM(horas) AS horas_lunes FROM (
SELECT (a.APPOINTMENT_END - a.APPOINTMENT_START) AS horas
    FROM rapla.appointment AS a 
    INNER JOIN rapla.event_attribute_value AS eav ON a.event_id = eav.event_id 
	JOIN allocation al ON al.APPOINTMENT_ID = a.ID
	JOIN rapla.category c on c.ID = eav.ATTRIBUTE_VALUE 
	JOIN rapla.resource_attribute_value rav ON rav.RESOURCE_ID = al.RESOURCE_ID
	WHERE rav.ATTRIBUTE_KEY = "name" 
	AND eav.ATTRIBUTE_KEY = "title" 
	AND "2015-04-06" between a.APPOINTMENT_START and a.REPETITION_END
	AND DATE_FORMAT(a.APPOINTMENT_START,'%T:%f') between "08:00:00" and "13:00:00"
	AND a.ID NOT IN(SELECT ae.APPOINTMENT_ID FROM appointment_exception ae)
	
	AND WEEKDAY(a.APPOINTMENT_START) = "Monday"
) AS x;

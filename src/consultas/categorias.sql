USE rapla;

DROP PROCEDURE IF EXISTS consultar_categorias_por_categoria_padre;
DELIMITER //
CREATE PROCEDURE consultar_categorias_por_categoria_padre(
    IN id_categoria_padre INT(11)
)
BEGIN
SELECT * FROM rapla.category where parent_id = id_categoria_padre;
END //


DROP PROCEDURE IF EXISTS consultar_materias_sistemas;
DELIMITER //
CREATE PROCEDURE consultar_materias_sistemas(
)
BEGIN
/* estas son del anio 2015*/
CALL consultar_categorias_por_categoria_padre(568);
END //

-- Insere imagem do absorvente buscando pelo slug parcial (independente de "6" ou "8" unidades)
INSERT INTO product_image (product_id, image_url, alt_text, display_order, is_primary)
SELECT p.id,
       'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/absorvente_jdxcsa.png',
       'Absorvente Sempre Livre Noturno',
       0,
       true
FROM product p
WHERE p.slug LIKE '%absorvente%sempre%livre%'
  AND NOT EXISTS (
      SELECT 1 FROM product_image pi WHERE pi.product_id = p.id
  );

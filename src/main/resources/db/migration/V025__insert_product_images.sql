-- ============================================================
-- Imagens dos produtos (Cloudinary)
-- Mapeamento: nome do arquivo -> produto (slug)
-- ============================================================
INSERT INTO product_image (product_id, image_url, alt_text, display_order, is_primary)
SELECT p.id, img.url, img.alt, 0, true
FROM (VALUES
  (
    'vitamina-c-1000mg-efervescente-10-comprimidos',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711434/suplemento_jwig1u.png',
    'Vitamina C 1000mg Efervescente 10 comprimidos'
  ),
  (
    'suplemento-omega-3-1g-120-capsulas',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/omega3_r2e1ed.png',
    'Suplemento Ômega 3 1g 120 cápsulas'
  ),
  (
    'desodorante-antitranspirante-rexona-clinical-extra-dry-58g',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711434/desodorante_x6i9na.png',
    'Desodorante Antitranspirante Rexona Clinical Extra Dry 58g'
  ),
  (
    'creme-hidratante-corporal-nivea-milk-400ml',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711434/hidratante_mc3vcz.png',
    'Creme Hidratante Corporal Nivea Milk 400ml'
  ),
  (
    'protetor-solar-nivea-sun-fps-60-200ml',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/protetor_spau2w.png',
    'Protetor Solar Nivea Sun FPS 60 200ml'
  ),
  (
    'neosaldina-dipirona-500mg-20-comprimidos',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711434/dipirona_srvrs3.png',
    'Neosaldina Dipirona 500mg 20 comprimidos'
  ),
  (
    'vitamina-d3-2000ui-60-capsulas',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/vitaminad_srnmbe.png',
    'Vitamina D3 2000UI 60 cápsulas'
  ),
  (
    'shampoo-dove-nutricao-intensa-400ml',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/shampoo_ftwphu.png',
    'Shampoo Dove Nutrição Intensa 400ml'
  ),
  (
    'absorvente-sempre-livre-noturno-8-unidades',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/absorvente_jdxcsa.png',
    'Absorvente Sempre Livre Noturno 8 unidades'
  ),
  (
    'magnesio-dimalato-833mg-60-capsulas',
    'https://res.cloudinary.com/dovibmot9/image/upload/v1773711435/vitamina60cap_rqzppu.png',
    'Magnésio Dimalato 833mg 60 cápsulas'
  )
) AS img(slug, url, alt)
JOIN product p ON p.slug = img.slug;

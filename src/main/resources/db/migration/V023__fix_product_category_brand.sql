-- Atualiza category_id e brand_id dos produtos inseridos na V022
-- caso a CTE scalar subquery não tenha funcionado corretamente

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'vitaminas-e-suplementos')
WHERE slug = 'vitamina-c-1000mg-efervescente-10-comprimidos' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'vitaminas-e-suplementos')
WHERE slug = 'suplemento-omega-3-1g-120-capsulas' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'higiene-pessoal')
WHERE slug = 'desodorante-antitranspirante-rexona-clinical-extra-dry-58g' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'beleza-e-cosmeticos')
WHERE slug = 'creme-hidratante-corporal-nivea-milk-400ml' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'beleza-e-cosmeticos')
WHERE slug = 'protetor-solar-nivea-sun-fps-60-200ml' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'medicamentos')
WHERE slug = 'neosaldina-dipirona-500mg-20-comprimidos' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'vitaminas-e-suplementos')
WHERE slug = 'vitamina-d3-2000ui-60-capsulas' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'higiene-pessoal')
WHERE slug = 'shampoo-dove-nutricao-intensa-400ml' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'higiene-pessoal')
WHERE slug = 'absorvente-sempre-livre-noturno-8-unidades' AND category_id IS NULL;

UPDATE product SET
    category_id = (SELECT id FROM category WHERE slug = 'saude-e-bem-estar')
WHERE slug = 'magnesio-dimalato-833mg-60-capsulas' AND category_id IS NULL;

-- brand_id
UPDATE product SET
    brand_id = (SELECT id FROM brand WHERE slug = 'vitafor')
WHERE slug IN (
    'vitamina-c-1000mg-efervescente-10-comprimidos',
    'suplemento-omega-3-1g-120-capsulas',
    'vitamina-d3-2000ui-60-capsulas',
    'shampoo-dove-nutricao-intensa-400ml',
    'magnesio-dimalato-833mg-60-capsulas'
) AND brand_id IS NULL;

UPDATE product SET
    brand_id = (SELECT id FROM brand WHERE slug = 'rexona')
WHERE slug = 'desodorante-antitranspirante-rexona-clinical-extra-dry-58g' AND brand_id IS NULL;

UPDATE product SET
    brand_id = (SELECT id FROM brand WHERE slug = 'nivea')
WHERE slug IN (
    'creme-hidratante-corporal-nivea-milk-400ml',
    'protetor-solar-nivea-sun-fps-60-200ml'
) AND brand_id IS NULL;

UPDATE product SET
    brand_id = (SELECT id FROM brand WHERE slug = 'neosaldina')
WHERE slug = 'neosaldina-dipirona-500mg-20-comprimidos' AND brand_id IS NULL;

UPDATE product SET
    brand_id = (SELECT id FROM brand WHERE slug = 'sempre-livre')
WHERE slug = 'absorvente-sempre-livre-noturno-8-unidades' AND brand_id IS NULL;

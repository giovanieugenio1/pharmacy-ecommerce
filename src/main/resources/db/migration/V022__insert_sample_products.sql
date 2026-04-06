-- ============================================================
-- Categorias
-- ============================================================
INSERT INTO category (name, slug, description, display_order, active) VALUES
('Vitaminas e Suplementos', 'vitaminas-e-suplementos', 'Vitaminas, minerais e suplementos alimentares para sua saúde', 1, true),
('Higiene Pessoal',         'higiene-pessoal',         'Produtos de higiene e cuidado pessoal diário',               2, true),
('Medicamentos',            'medicamentos',             'Medicamentos isentos de prescrição',                         3, true),
('Beleza e Cosméticos',     'beleza-e-cosmeticos',     'Dermocosméticos, cremes e cuidados com a pele',              4, true),
('Saúde e Bem-estar',       'saude-e-bem-estar',       'Produtos para uma vida mais saudável',                       5, true);

-- ============================================================
-- Marcas
-- ============================================================
INSERT INTO brand (name, slug, description, active) VALUES
('Vitafor',       'vitafor',       'Suplementos e vitaminas de alta qualidade',        true),
('Nivea',         'nivea',         'Cuidados com a pele e corpo há mais de 100 anos',  true),
('Rexona',        'rexona',        'Desodorantes e antitranspirantes de longa duração', true),
('Neosaldina',    'neosaldina',    'Medicamentos para alívio de dores e febre',         true),
('Sempre Livre',  'sempre-livre',  'Linha completa de absorventes femininos',           true);

-- ============================================================
-- Produtos (via CTEs para referenciar category/brand por slug)
-- ============================================================
WITH
  cat_vit  AS (SELECT id FROM category WHERE slug = 'vitaminas-e-suplementos'),
  cat_hig  AS (SELECT id FROM category WHERE slug = 'higiene-pessoal'),
  cat_med  AS (SELECT id FROM category WHERE slug = 'medicamentos'),
  cat_bel  AS (SELECT id FROM category WHERE slug = 'beleza-e-cosmeticos'),
  cat_sau  AS (SELECT id FROM category WHERE slug = 'saude-e-bem-estar'),
  br_vit   AS (SELECT id FROM brand WHERE slug = 'vitafor'),
  br_niv   AS (SELECT id FROM brand WHERE slug = 'nivea'),
  br_rex   AS (SELECT id FROM brand WHERE slug = 'rexona'),
  br_neo   AS (SELECT id FROM brand WHERE slug = 'neosaldina'),
  br_sem   AS (SELECT id FROM brand WHERE slug = 'sempre-livre')
INSERT INTO product (name, slug, sku, category_id, brand_id, short_description, full_description, featured, active)
VALUES
(
  'Vitamina C 1000mg Efervescente 10 comprimidos',
  'vitamina-c-1000mg-efervescente-10-comprimidos',
  'VIT-C-001',
  (SELECT id FROM cat_vit),
  (SELECT id FROM br_vit),
  'Vitamina C 1000mg em comprimidos efervescentes de rápida absorção com sabor laranja.',
  'A Vitamina C 1000mg Efervescente Vitafor é uma excelente fonte de ácido ascórbico, essencial para o fortalecimento do sistema imunológico. Cada comprimido efervescente contém 1000mg de vitamina C, com sabor laranja e baixo teor de açúcar. Dissolva um comprimido em 200ml de água e consuma uma vez ao dia.',
  true, true
),
(
  'Suplemento Ômega 3 1g 120 cápsulas',
  'suplemento-omega-3-1g-120-capsulas',
  'OMG-001',
  (SELECT id FROM cat_vit),
  (SELECT id FROM br_vit),
  'Óleo de peixe com 1g de Ômega 3 por cápsula, rico em EPA e DHA para saúde cardiovascular.',
  'O Suplemento Ômega 3 Vitafor é extraído de peixes de águas profundas e não poluídas, garantindo pureza e qualidade. Cada cápsula contém 1g de óleo de peixe concentrado, com 330mg de EPA e 220mg de DHA. Auxilia na manutenção de níveis saudáveis de triglicerídeos e suporte cardiovascular. Isento de metais pesados e testado por laboratório independente.',
  true, true
),
(
  'Desodorante Antitranspirante Rexona Clinical Extra Dry 58g',
  'desodorante-antitranspirante-rexona-clinical-extra-dry-58g',
  'REX-CLI-001',
  (SELECT id FROM cat_hig),
  (SELECT id FROM br_rex),
  'Proteção antitranspirante clínica por até 96 horas com fórmula dermatologicamente testada.',
  'O Rexona Clinical Extra Dry oferece proteção superior por até 96 horas. Sua fórmula avançada com tecnologia MOTIONSENSE ativa mais proteção quanto mais você se movimenta. Dermatologicamente testado, ideal para quem transpira muito. Não mancha roupas e possui fragrância suave.',
  true, true
),
(
  'Creme Hidratante Corporal Nivea Milk 400ml',
  'creme-hidratante-corporal-nivea-milk-400ml',
  'NIV-MLK-001',
  (SELECT id FROM cat_bel),
  (SELECT id FROM br_niv),
  'Hidratação intensa com Pró-Vitaminas e leite, absorção rápida para pele seca.',
  'O Creme Hidratante Nivea Milk possui fórmula enriquecida com Pró-Vitaminas e extrato de leite que nutre profundamente a pele. Sua textura leve é absorvida rapidamente, deixando a pele macia e hidratada por até 48 horas. Indicado para pele seca e muito seca. Sem parabenos, hipoalergênico.',
  true, true
),
(
  'Protetor Solar Nivea Sun FPS 60 200ml',
  'protetor-solar-nivea-sun-fps-60-200ml',
  'NIV-SUN-001',
  (SELECT id FROM cat_bel),
  (SELECT id FROM br_niv),
  'Proteção solar FPS 60 com UVA e UVB, resistente à água e toque seco.',
  'O Protetor Solar Nivea Sun FPS 60 oferece proteção de amplo espectro contra os raios UVA e UVB. Resistente à água e ao suor, com tecnologia de toque seco que não deixa a pele oleosa. Fórmula com vitamina E que nutre e protege a pele ao mesmo tempo. Recomendado para uso diário.',
  true, true
),
(
  'Neosaldina Dipirona 500mg 20 comprimidos',
  'neosaldina-dipirona-500mg-20-comprimidos',
  'NEO-DIP-001',
  (SELECT id FROM cat_med),
  (SELECT id FROM br_neo),
  'Analgésico e antitérmico com dipirona sódica 500mg para alívio de dores e febre.',
  'A Neosaldina com Dipirona 500mg é indicada para o alívio de dores de cabeça, dores musculares, cólicas, dores de dente e febre. Cada comprimido contém 500mg de dipirona sódica. Não deve ser administrado a crianças menores de 3 meses ou com menos de 5kg. Leia a bula antes de usar. Venda livre.',
  false, true
),
(
  'Vitamina D3 2000UI 60 cápsulas',
  'vitamina-d3-2000ui-60-capsulas',
  'VIT-D3-001',
  (SELECT id FROM cat_vit),
  (SELECT id FROM br_vit),
  'Vitamina D3 em cápsulas softgel de alta biodisponibilidade para imunidade e saúde óssea.',
  'A Vitamina D3 2000UI Vitafor é produzida na forma de colecalciferol, a forma mais ativa da vitamina D, garantindo máxima biodisponibilidade. Essencial para absorção de cálcio, fortalecimento dos ossos e funcionamento do sistema imunológico. Cada cápsula softgel contém 2000UI de vitamina D3. Ideal para pessoas com baixa exposição solar.',
  true, true
),
(
  'Shampoo Dove Nutrição Intensa 400ml',
  'shampoo-dove-nutricao-intensa-400ml',
  'DOV-SHP-001',
  (SELECT id FROM cat_hig),
  (SELECT id FROM br_niv),
  'Shampoo com fórmula nutritiva que repara cabelos danificados e ressecados.',
  'O Shampoo Dove Nutrição Intensa possui fórmula com agentes nutritivos que penetram nas fibras do cabelo, reparando os danos causados por processos químicos, calor e estresse diário. Indicado para cabelos danificados e ressecados. Fórmula com pH balanceado, suave para uso diário. Sem sal e sem sulfato.',
  false, true
),
(
  'Absorvente Sempre Livre Noturno 8 unidades',
  'absorvente-sempre-livre-noturno-8-unidades',
  'SL-NOT-001',
  (SELECT id FROM cat_hig),
  (SELECT id FROM br_sem),
  'Absorvente externo noturno com tecnologia Ultrafina e proteção 360° para noites tranquilas.',
  'O Absorvente Sempre Livre Noturno possui tecnologia Ultrafina com proteção de abas extralongadas, garantindo proteção 360° durante a noite. Sua camada absorvente de alta eficiência mantém você seca e protegida por até 8 horas. Com fragância suave e camada superior macia que previne irritações.',
  false, true
),
(
  'Magnésio Dimalato 833mg 60 cápsulas',
  'magnesio-dimalato-833mg-60-capsulas',
  'MAG-DIM-001',
  (SELECT id FROM cat_sau),
  (SELECT id FROM br_vit),
  'Magnésio Dimalato de alta absorção para energia celular, saúde muscular e qualidade do sono.',
  'O Magnésio Dimalato Vitafor é a forma quelada de magnésio com maior biodisponibilidade, combinado com ácido málico para produção de energia celular. Auxilia no funcionamento muscular, qualidade do sono, redução do estresse e saúde do sistema nervoso. Cada cápsula contém 833mg de magnésio dimalato, equivalente a 100mg de magnésio elementar.',
  true, true
);

-- ============================================================
-- Preços (vinculados à loja principal)
-- ============================================================
WITH main_store AS (SELECT id FROM store WHERE is_main = true LIMIT 1)
INSERT INTO product_price (product_id, store_id, price, promotional_price, active)
SELECT p.id, (SELECT id FROM main_store), pp.price, pp.promo, true
FROM (VALUES
  ('vitamina-c-1000mg-efervescente-10-comprimidos',       29.90,  24.90),
  ('suplemento-omega-3-1g-120-capsulas',                  89.90,  NULL),
  ('desodorante-antitranspirante-rexona-clinical-extra-dry-58g', 32.90, 26.09),
  ('creme-hidratante-corporal-nivea-milk-400ml',           27.90,  22.90),
  ('protetor-solar-nivea-sun-fps-60-200ml',                49.90,  NULL),
  ('neosaldina-dipirona-500mg-20-comprimidos',             15.90,  NULL),
  ('vitamina-d3-2000ui-60-capsulas',                       34.90,  NULL),
  ('shampoo-dove-nutricao-intensa-400ml',                  22.90,  18.50),
  ('absorvente-sempre-livre-noturno-8-unidades',            8.90,  NULL),
  ('magnesio-dimalato-833mg-60-capsulas',                  54.90,  44.90)
) AS pp(slug, price, promo)
JOIN product p ON p.slug = pp.slug;

-- ============================================================
-- Estoque (vinculado à loja principal)
-- ============================================================
WITH main_store AS (SELECT id FROM store WHERE is_main = true LIMIT 1)
INSERT INTO store_inventory (store_id, product_id, available_quantity, reserved_quantity, minimum_quantity, active)
SELECT (SELECT id FROM main_store), p.id, inv.qty, 0, 5, true
FROM (VALUES
  ('vitamina-c-1000mg-efervescente-10-comprimidos',              50),
  ('suplemento-omega-3-1g-120-capsulas',                         30),
  ('desodorante-antitranspirante-rexona-clinical-extra-dry-58g', 80),
  ('creme-hidratante-corporal-nivea-milk-400ml',                 45),
  ('protetor-solar-nivea-sun-fps-60-200ml',                      60),
  ('neosaldina-dipirona-500mg-20-comprimidos',                  120),
  ('vitamina-d3-2000ui-60-capsulas',                             40),
  ('shampoo-dove-nutricao-intensa-400ml',                        55),
  ('absorvente-sempre-livre-noturno-8-unidades',                 90),
  ('magnesio-dimalato-833mg-60-capsulas',                        35)
) AS inv(slug, qty)
JOIN product p ON p.slug = inv.slug;

-- ============================================================
-- Banner promocional
-- ============================================================
INSERT INTO banner (title, subtitle, image_url, target_url, position, display_order, active)
VALUES (
  'Vitaminas e Suplementos com até 20% OFF',
  'Cuide da sua saúde com os melhores suplementos. Frete grátis acima de R$ 99.',
  'https://placehold.co/1200x400/16a34a/ffffff?text=Vitaminas+e+Suplementos+20%25+OFF',
  '/produtos?categoryId=1',
  'HOME_HERO',
  1,
  true
);

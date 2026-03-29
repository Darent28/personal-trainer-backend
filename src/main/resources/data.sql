-- ============================================================
-- Seed data (all inserts are idempotent via ON CONFLICT DO NOTHING)
-- ============================================================

-- Gender types
INSERT INTO gender_type (id, name) VALUES
    (1, 'Male'),
    (2, 'Female')
ON CONFLICT (id) DO NOTHING;

-- Goal types
INSERT INTO goal_type (id, goal_name, goal_description) VALUES
    (1, 'Cut',           'Calorie deficit to reduce body fat while preserving muscle'),
    (2, 'Bulk',          'Calorie surplus to build muscle mass with minimal fat gain'),
    (3, 'Recomposition', 'Maintain calories to simultaneously lose fat and gain muscle')
ON CONFLICT (id) DO NOTHING;

-- Activity levels (Harris-Benedict multipliers)
INSERT INTO level_activity_type (id, activity_name, factor) VALUES
    (1, 'Sedentary',              1.2),
    (2, 'Lightly Active',         1.375),
    (3, 'Moderately Active',      1.55),
    (4, 'Very Active',            1.725),
    (5, 'Extremely Active',       1.9)
ON CONFLICT (id) DO NOTHING;

-- Goal macro config ranges
-- goal_type_id: 1=Cut, 2=Bulk, 3=Recomposition
INSERT INTO goal_macro_config (goal_type_id, calorie_offset_min, calorie_offset_max, protein_per_kg_min, protein_per_kg_max, fat_per_kg_min, fat_per_kg_max)
VALUES
    (1, -500, -300, 2.2, 2.8, 0.6, 1.0),
    (2,  200,  400, 1.6, 2.2, 0.6, 0.9),
    (3,    0,    0, 2.0, 2.6, 0.6, 0.9)
ON CONFLICT (goal_type_id) DO NOTHING;

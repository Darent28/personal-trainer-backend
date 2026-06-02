-- ============================================================
-- Schema for personal-trainer-backend
-- All statements are idempotent (CREATE TABLE IF NOT EXISTS)
-- ============================================================

-- Lookup: gender (referenced by users.gender_id)
CREATE TABLE IF NOT EXISTS gender_type (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Lookup: goal types (cut / bulk / recomposition)
CREATE TABLE IF NOT EXISTS goal_type (
    id               SERIAL PRIMARY KEY,
    goal_name        VARCHAR(100) NOT NULL,
    goal_description TEXT
);

-- Lookup: activity levels with Harris-Benedict multiplier
CREATE TABLE IF NOT EXISTS level_activity_type (
    id            SERIAL PRIMARY KEY,
    activity_name VARCHAR(100) NOT NULL,
    factor        DOUBLE PRECISION NOT NULL
);

-- Macro range configuration per goal (DB-driven, not hardcoded)
CREATE TABLE IF NOT EXISTS goal_macro_config (
    id                  SERIAL PRIMARY KEY,
    goal_type_id        INTEGER NOT NULL UNIQUE REFERENCES goal_type(id),
    calorie_offset_min  INTEGER NOT NULL,
    calorie_offset_max  INTEGER NOT NULL,
    protein_per_kg_min  DOUBLE PRECISION NOT NULL,
    protein_per_kg_max  DOUBLE PRECISION NOT NULL,
    fat_per_kg_min      DOUBLE PRECISION NOT NULL,
    fat_per_kg_max      DOUBLE PRECISION NOT NULL
);

-- Users
CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(255),
    email           VARCHAR(255),
    password        VARCHAR(255),
    status          INTEGER,
    gender_id       INTEGER,
    height          DOUBLE PRECISION,
    email_verified  BOOLEAN DEFAULT FALSE
);

-- Email confirmation tokens
CREATE TABLE IF NOT EXISTS email_confirmation_tokens (
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL UNIQUE,
    user_id    BIGINT NOT NULL REFERENCES users(id),
    expires_at TIMESTAMP NOT NULL,
    used       BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_ect_token ON email_confirmation_tokens(token);
CREATE INDEX IF NOT EXISTS idx_ect_user_id ON email_confirmation_tokens(user_id);

-- User physical info & diet profile
CREATE TABLE IF NOT EXISTS users_info (
    id             BIGSERIAL PRIMARY KEY,
    _weight        DOUBLE PRECISION,
    fat_porcentage DOUBLE PRECISION,
    age            INTEGER,
    activity_level INTEGER REFERENCES level_activity_type(id),
    goal           INTEGER REFERENCES goal_type(id),
    user_id        BIGINT REFERENCES users(id),
    created_at     TIMESTAMP DEFAULT NOW()
);

-- Daily macro plan linked to a users_info record
CREATE TABLE IF NOT EXISTS daily_plans (
    id              BIGSERIAL PRIMARY KEY,
    total_calories  INTEGER,
    total_proteins  INTEGER,
    total_fats      INTEGER,
    total_carbs     INTEGER,
    user_info_id    BIGINT NOT NULL REFERENCES users_info(id)
);

-- Plicometry checks — one row per measurement session
CREATE TABLE IF NOT EXISTS plicometry_check (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT NOT NULL REFERENCES users(id),
    method           SMALLINT NOT NULL,
    fat_percentage   DOUBLE PRECISION NOT NULL,
    site_chest       DOUBLE PRECISION,
    site_midaxillary DOUBLE PRECISION,
    site_triceps     DOUBLE PRECISION,
    site_subscapular DOUBLE PRECISION,
    site_abdomen     DOUBLE PRECISION,
    site_suprailiac  DOUBLE PRECISION,
    site_thigh       DOUBLE PRECISION,
    site_iliac_crest DOUBLE PRECISION,
    created_at       TIMESTAMP DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_plicometry_user_id ON plicometry_check(user_id);

-- Favorite foods saved by users (data denormalized from USDA FoodData Central)
CREATE TABLE IF NOT EXISTS favorite_foods (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id),
    fdc_id        INTEGER NOT NULL,
    name          VARCHAR(500) NOT NULL,
    brand         VARCHAR(255),
    data_type     VARCHAR(100),
    calories      DOUBLE PRECISION,
    protein_g     DOUBLE PRECISION,
    fat_g         DOUBLE PRECISION,
    fiber_g       DOUBLE PRECISION,
    vitamin_a_ug  DOUBLE PRECISION,
    vitamin_c_mg  DOUBLE PRECISION,
    vitamin_d_ug  DOUBLE PRECISION,
    vitamin_e_mg  DOUBLE PRECISION,
    vitamin_k_ug  DOUBLE PRECISION,
    vitamin_b6_mg DOUBLE PRECISION,
    vitamin_b12_ug DOUBLE PRECISION,
    created_at    TIMESTAMP DEFAULT NOW(),
    UNIQUE (user_id, fdc_id)
);

CREATE INDEX IF NOT EXISTS idx_favorite_foods_user_id ON favorite_foods(user_id);

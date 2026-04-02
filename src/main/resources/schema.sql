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
    height         DOUBLE PRECISION,
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

CREATE TABLE IF NOT EXISTS posting (
     id BIGSERIAL PRIMARY KEY,
     user_id VARCHAR(50),
     contents VARCHAR(500),
     created_date TIMESTAMP,
     updated_date TIMESTAMP
);
CREATE INDEX IF NOT EXISTS index_posting_user_id ON posting(user_id);
CREATE INDEX IF NOT EXISTS index_posting_created_date ON posting(created_date);


CREATE TABLE IF NOT EXISTS following (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50),
    follow_user_id VARCHAR(50),
    created_date TIMESTAMP
);
CREATE INDEX IF NOT EXISTS index_follow_user_id ON following(user_id);
CREATE UNIQUE INDEX IF NOT EXISTS unique_index_follow ON following(user_id, follow_user_id);
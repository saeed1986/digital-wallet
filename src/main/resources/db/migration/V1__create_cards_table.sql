
CREATE TABLE cards (
    id UUID PRIMARY KEY,
    display_name VARCHAR(100) NOT NULL,
    last_four VARCHAR(4) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

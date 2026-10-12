-- ====================================================
-- BOOK CONTEXT - Gerenciamento do Catálogo
-- ====================================================

CREATE TABLE books (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(13) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_isbn (isbn),
                       INDEX idx_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE copies (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        book_id BIGINT NOT NULL,
                        serial_number VARCHAR(50) NOT NULL UNIQUE,
                        status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, LOANED, RESERVED, DAMAGED
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                        INDEX idx_book_id (book_id),
                        INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
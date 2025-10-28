-- Minimal seed script for testing
-- Only essential data needed to start the application

-- Clear existing data
DELETE FROM national_tariff_lines;
DELETE FROM product_categories;
DELETE FROM country;
DELETE FROM users;

-- Create users first
INSERT INTO users (username, email, password_hash, is_admin, first_name, last_name, enabled, created_at) VALUES
('admin', 'admin@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', true, 'System', 'Administrator', true, NOW());

-- Create countries
INSERT INTO country (country_code, country_name, region, currency_code) VALUES
('SG', 'Singapore', 'Asia', 'SGD'),
('US', 'United States', 'Americas', 'USD');

-- Create product categories
INSERT INTO product_categories (hs_code, category_name, description, is_active) VALUES
(851713, 'Smartphones', 'Mobile phones with smart capabilities', true),
(854231, 'Microprocessors', 'Electronic integrated circuits', true);

-- Create minimal tariff lines with proper foreign keys
INSERT INTO national_tariff_lines (country_id, tariff_line_code, description, parent_hs_code, level, created_by, updated_by) 
SELECT 
    c.country_id,
    '851713.10',
    'Smartphones - Premium',
    851713,
    10,
    u.user_id,
    u.user_id
FROM country c, users u 
WHERE c.country_code = 'SG' AND u.username = 'admin';

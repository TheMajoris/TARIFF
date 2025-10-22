-- =====================================================
-- Database Seed Script for CS203 Tariff Management System
-- =====================================================
-- This script populates the database with seed data for production/development
-- Schema creation is handled by Hibernate (JPA_DDL_AUTO=create-drop)
-- This script is disabled in test environment via application-test.properties

-- Clear any existing data (Delete in correct order to avoid foreign key constraints)
DELETE FROM tariff_rates;
DELETE FROM national_tariff_lines;
DELETE FROM product_categories;
DELETE FROM country;
DELETE FROM users_refresh; -- Delete refresh tokens first
DELETE FROM users;
DELETE FROM news_tags;
DELETE FROM news;

-- =====================================================
-- USER DATA (Insert users first to avoid foreign key constraints)
-- =====================================================
INSERT INTO users (username, email, password_hash, is_admin, first_name, last_name, enabled, created_at) VALUES
('admin', 'admin@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', true, 'System', 'Administrator', true, NOW()),
('john.doe', 'john.doe@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', true, 'John', 'Doe', true, NOW()),
('jane.smith', 'jane.smith@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', false, 'Jane', 'Smith', true, NOW()),
('bob.wilson', 'bob.wilson@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', false, 'Bob', 'Wilson', true, NOW()),
('alice.brown', 'alice.brown@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', false, 'Alice', 'Brown', true, NOW());

-- Note: Password is 'password123' for all users (hashed with BCrypt 8 rounds)

-- =====================================================
-- COUNTRY DATA (Essential countries only)
-- =====================================================

INSERT INTO country (country_code, country_name, region, currency_code) VALUES
-- Major Electronics Trading Countries
('SG', 'Singapore', 'Asia', 'SGD'),
('MY', 'Malaysia', 'Asia', 'MYR'),
('TH', 'Thailand', 'Asia', 'THB'),
('CN', 'China', 'Asia', 'CNY'),
('US', 'United States', 'Americas', 'USD'),
('DE', 'Germany', 'Europe', 'EUR'),
('JP', 'Japan', 'Asia', 'JPY'),
('IN', 'India', 'Asia', 'INR'),
('KR', 'South Korea', 'Asia', 'KRW'),
('TW', 'Taiwan', 'Asia', 'TWD'),
('VN', 'Vietnam', 'Asia', 'VND'),
('ID', 'Indonesia', 'Asia', 'IDR'),
('PH', 'Philippines', 'Asia', 'PHP'),
('AU', 'Australia', 'Oceania', 'AUD'),
('HK', 'Hong Kong', 'Asia', 'HKD'),
('CA', 'Canada', 'Americas', 'CAD'),
('MX', 'Mexico', 'Americas', 'MXN'),
('BR', 'Brazil', 'Americas', 'BRL'),
('GB', 'United Kingdom', 'Europe', 'GBP'),
('FR', 'France', 'Europe', 'EUR'),
('IT', 'Italy', 'Europe', 'EUR'),
('NL', 'Netherlands', 'Europe', 'EUR'),
('RU', 'Russia', 'Europe', 'RUB')
;

-- =====================================================
-- ELECTRONICS PRODUCT CATEGORIES DATA (Essential HS Codes)
-- =====================================================

INSERT INTO product_categories (hs_code, category_name, description, is_active) VALUES
-- Essential Electronics Categories
(851713, 'Smartphones', 'Mobile phones with smart capabilities', true),
(854231, 'Microprocessors', 'Electronic integrated circuits - processors and controllers', true),
(854232, 'Memory Chips', 'Electronic integrated circuits - memories', true),
(847141, 'Laptops', 'Portable computers weighing not more than 10 kg', true),
(847149, 'Desktop Computers', 'Other digital automatic data processing machines', true),
(852872, 'LED TVs', 'Television receivers with LED displays', true),
(854110, 'Semiconductor Diodes', 'Diodes, other than photosensitive or light-emitting diodes', true),
(854170, 'LED Components', 'Light-emitting diodes (LED)', true),
(847160, 'Computer Peripherals', 'Input or output units for computers', true),
(852852, 'Computer Monitors', 'Monitors using liquid crystal display (LCD) technology', true)
;

-- =====================================================
-- NATIONAL TARIFF LINES DATA (Simplified)
-- =====================================================

INSERT INTO national_tariff_lines (country_id, tariff_line_code, description, parent_hs_code, level, created_by, updated_by) VALUES
-- Singapore National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'SG'), '851713.10', 'Smartphones - Premium', 851713, 10, (SELECT user_id FROM users WHERE username = 'admin'), (SELECT user_id FROM users WHERE username = 'admin')),
((SELECT country_id FROM country WHERE country_code = 'SG'), '854231.10', 'Processors - ARM', 854231, 10, (SELECT user_id FROM users WHERE username = 'admin'), (SELECT user_id FROM users WHERE username = 'admin')),
((SELECT country_id FROM country WHERE country_code = 'SG'), '854232.10', 'Memory - DDR5', 854232, 10, (SELECT user_id FROM users WHERE username = 'admin'), (SELECT user_id FROM users WHERE username = 'admin')),

-- Malaysia National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'MY'), '851713.20', 'Smartphones - Mid-range', 851713, 10, (SELECT user_id FROM users WHERE username = 'john.doe'), (SELECT user_id FROM users WHERE username = 'john.doe')),
((SELECT country_id FROM country WHERE country_code = 'MY'), '847141.20', 'Laptops - Consumer', 847141, 10, (SELECT user_id FROM users WHERE username = 'john.doe'), (SELECT user_id FROM users WHERE username = 'john.doe')),

-- China National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'CN'), '851713.30', 'Smartphones - Budget', 851713, 10, (SELECT user_id FROM users WHERE username = 'admin'), (SELECT user_id FROM users WHERE username = 'admin')),
((SELECT country_id FROM country WHERE country_code = 'CN'), '854231.30', 'Processors - Mobile', 854231, 10, (SELECT user_id FROM users WHERE username = 'admin'), (SELECT user_id FROM users WHERE username = 'admin')),

-- United States National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'US'), '851713.40', 'Smartphones - iPhone', 851713, 10, (SELECT user_id FROM users WHERE username = 'john.doe'), (SELECT user_id FROM users WHERE username = 'john.doe')),
((SELECT country_id FROM country WHERE country_code = 'US'), '847141.40', 'Laptops - MacBook', 847141, 10, (SELECT user_id FROM users WHERE username = 'john.doe'), (SELECT user_id FROM users WHERE username = 'john.doe'))
;

-- =====================================================
-- TARIFF RATE DATA (Essential rates only)
-- =====================================================

INSERT INTO tariff_rates (tariff_rate, tariff_type, unit_quantity, rate_unit, effective_date, expiry_date, preferential_tariff,
                         importing_country_id, exporting_country_id, hs_code, created_at, updated_at) VALUES

-- Singapore importing from China
(0.00, 'ad_valorem',NULL, 'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- Singapore importing from US
(5.00, 'ad_valorem', NULL, 'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

-- Malaysia importing from Singapore (FTA)
(0.00, 'ad_valorem', NULL,'kg', '2024-01-01', '2024-12-31', true,
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 851713, NOW(), NOW()),

-- China importing from US
(25.00, 'ad_valorem',NULL, 'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

-- US importing from China
(26.40, 'ad_valorem', NULL,'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- Germany importing from US
(0.00, 'ad_valorem', NULL,'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

-- Japan importing from South Korea
(0.00, 'ad_valorem',NULL, 'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854232, NOW(), NOW()),

-- India importing from China
(30.00, 'ad_valorem',NULL, 'kg', '2024-01-01', '2024-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'),
 (SELECT country_id FROM country WHERE country_code = 'CN'),
 851713, NOW(), NOW()),

-- Singapore importing from China
(3.00, 'specific', 1.00, 'kg', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'SG'),
 (SELECT country_id FROM country WHERE country_code = 'CN'),
 851713, NOW(), NOW()),

-- Singapore importing from US
(5.00, 'specific', 1.00, 'kg', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'SG'),
 (SELECT country_id FROM country WHERE country_code = 'US'),
 854231, NOW(), NOW()),

-- Malaysia importing from Singapore (FTA - 0 tariff)
(0.00, 'specific', 1.00, 'kg', '2023-01-01', '2023-12-31', true,
 (SELECT country_id FROM country WHERE country_code = 'MY'),
 (SELECT country_id FROM country WHERE country_code = 'SG'),
 851713, NOW(), NOW()),

-- China importing from US
(2.50, 'specific', 400.00, 'g', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'),
 (SELECT country_id FROM country WHERE country_code = 'US'),
 854231, NOW(), NOW()),

-- US importing from China
(4.00, 'specific',1.00, 'kg', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'),
 (SELECT country_id FROM country WHERE country_code = 'CN'),
 851713, NOW(), NOW()),

-- Germany importing from US
(0.00, 'specific', 1.00, 'kg', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'),
 (SELECT country_id FROM country WHERE country_code = 'US'),
 854231, NOW(), NOW()),

-- Japan importing from South Korea
(1.50, 'specific', 1.00, 'kg', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'JP'),
 (SELECT country_id FROM country WHERE country_code = 'KR'),
 854232, NOW(), NOW()),

-- India importing from China
(2.00, 'specific', 400.00, 'g', '2023-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'),
 (SELECT country_id FROM country WHERE country_code = 'CN'),
 851713, NOW(), NOW());

-- =====================================================
-- SEED SCRIPT COMPLETION VERIFICATION
-- =====================================================
-- Display summary counts to verify successful data insertion
SELECT 'Countries:' as table_name, COUNT(*) as count FROM country
UNION ALL
SELECT 'Product Categories:', COUNT(*) FROM product_categories
UNION ALL
SELECT 'Users:', COUNT(*) FROM users
UNION ALL
SELECT 'National Tariff Lines:', COUNT(*) FROM national_tariff_lines
UNION ALL
SELECT 'Tariff Rates:', COUNT(*) FROM tariff_rates;

-- =====================================================
-- NEWS AI
-- =====================================================
ALTER SEQUENCE news_news_id_seq RESTART WITH 1;
INSERT INTO news (headline, summary, url) VALUES
  ('US to impose tariffs on pharma imports, kitchen cabinets, furniture, heavy trucks', 'In a trio of posts on Truth Social on Thursday evening, President Trump said the US would impose a slew of tariffs starting Oct. 1. Trump said imported kitchen cabinets, bathroom vanities, pharmaceutical imports, and heavy trucks will be taxed in the latest move to try to force more manufacturing onto US soil.', 'https://finance.yahoo.com/news/live/trump-tariffs-live-updates-us-to-impose-tariffs-on-pharma-imports-kitchen-cabinets-furniture-heavy-trucks-175804095.html'),
  ('Tariff inflation has been later and less than expected', 'Fed Chair Powell said Tuesday that businesses passing higher costs from tariffs on to consumers has been "later and less than we expected."', 'https://spectrumlocalnews.com/us/snplus/business/2025/09/23/federal-reserve-chair-jerome-powell-speech'),
  ('South Korean president says US tariffs talks worrying FX market', 'South Korean President Lee Jae Myung said negotiations with the U.S. on tariffs have stirred concerns in the foreign exchange market but he was confident the two sides will reach a solution, a statement from Lees office said on Tuesday.', 'https://finance.yahoo.com/news/south-korean-president-says-us-032814927.html');

INSERT INTO news_tags (news_id, tag) VALUES
 (1, 'trade'),
 (1, 'US'),
 (1, 'China'),
 (2, 'trade'),
 (2, 'US'),
 (2, 'inflation'),
 (3, 'energy'),
 (3, 'korea'),
 (3, 'south korea');

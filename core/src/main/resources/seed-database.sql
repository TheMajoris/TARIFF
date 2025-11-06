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
-- TARIFF RATE DATA (30+ Current, 20+ Expired)
-- =====================================================

INSERT INTO tariff_rates (tariff_rate, tariff_type, unit_quantity, rate_unit, effective_date, expiry_date, preferential_tariff,
                         importing_country_id, exporting_country_id, hs_code, created_at, updated_at) VALUES

-- =====================================================
-- CURRENT TARIFFS (2024-2025)
-- =====================================================

-- 1. US importing smartphones from China (Liberation Day + Section 301)
(54.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 2. US importing semiconductors from China (Section 301 Four-Year Review)
(50.00, 'ad_valorem', NULL, 'percent', '2025-01-01', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854231, NOW(), NOW()),

-- 3. US importing laptops from China
(51.10, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

-- 4. US importing electric vehicles from China
(100.00, 'ad_valorem', NULL, 'percent', '2024-09-27', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 870380, NOW(), NOW()),

-- 5. US importing lithium-ion EV batteries from China
(25.00, 'ad_valorem', NULL, 'percent', '2024-09-27', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 850760, NOW(), NOW()),

-- 6. US importing solar cells from China
(50.00, 'ad_valorem', NULL, 'percent', '2024-09-27', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854140, NOW(), NOW()),

-- 7. US importing steel products from China
(25.00, 'ad_valorem', NULL, 'percent', '2024-09-27', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 720839, NOW(), NOW()),

-- 8. US importing aluminum products from China
(25.00, 'ad_valorem', NULL, 'percent', '2024-09-27', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 760611, NOW(), NOW()),

-- 9. China importing from US (retaliatory tariffs)
(32.60, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

-- 10. China importing smartphones from US
(32.60, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 851713, NOW(), NOW()),

-- 11. EU importing from US (reciprocal tariffs)
(15.00, 'ad_valorem', NULL, 'percent', '2025-07-27', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

-- 12. EU importing electronics from US
(15.00, 'ad_valorem', NULL, 'percent', '2025-07-27', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 851713, NOW(), NOW()),

-- 13. US importing from Japan
(24.00, 'ad_valorem', NULL, 'percent', '2025-08-01', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 870323, NOW(), NOW()),

-- 14. US importing electronics from Japan
(24.00, 'ad_valorem', NULL, 'percent', '2025-08-01', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW()),

-- 15. US importing from South Korea
(25.00, 'ad_valorem', NULL, 'percent', '2025-08-01', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 851713, NOW(), NOW()),

-- 16. US importing semiconductors from South Korea
(25.00, 'ad_valorem', NULL, 'percent', '2025-08-01', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854231, NOW(), NOW()),

-- 17. India importing smartphones from China
(30.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 18. India importing electronics components from China
(8.50, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854232, NOW(), NOW()),

-- 19. India importing laptops from China
(20.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

-- 20. Singapore importing from Malaysia (ASEAN FTA)
(0.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', true,
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 851713, NOW(), NOW()),

-- 21. Malaysia importing from Singapore (ASEAN FTA)
(0.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', true,
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 854231, NOW(), NOW()),

-- 22. Vietnam importing from China
(3.70, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 23. US importing from Vietnam (electronics exemption)
(0.00, 'ad_valorem', NULL, 'percent', '2025-04-12', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 851713, NOW(), NOW()),

-- 24. US importing semiconductors from Vietnam
(0.00, 'ad_valorem', NULL, 'percent', '2025-04-12', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 854231, NOW(), NOW()),

-- 25. Japan importing from South Korea
(0.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854232, NOW(), NOW()),

-- 26. South Korea importing from Japan
(0.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854231, NOW(), NOW()),

-- 27. EU importing steel (exceeding quota)
(50.00, 'ad_valorem', NULL, 'percent', '2024-07-01', '2026-06-30', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 720839, NOW(), NOW()),

-- 28. Germany importing from China (MFN rate)
(4.80, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 29. Singapore importing from US (standard rate)
(0.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 851713, NOW(), NOW()),

-- 30. Malaysia importing from China
(5.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

-- 31. Thailand importing from China (ASEAN-China FTA)
(0.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', true,
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 32. India importing from Vietnam
(10.00, 'ad_valorem', NULL, 'percent', '2024-01-01', '2025-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 854232, NOW(), NOW()),

-- 33. US importing syringes from China
(50.00, 'ad_valorem', NULL, 'percent', '2024-09-27', '2026-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 901831, NOW(), NOW()),


-- =====================================================
-- EXPIRED TARIFFS (2023 and earlier)
-- =====================================================

-- 34. US importing smartphones from China (pre-escalation)
(7.50, 'ad_valorem', NULL, 'percent', '2020-02-14', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 35. US importing semiconductors from China (pre-review)
(25.00, 'ad_valorem', NULL, 'percent', '2019-05-10', '2024-01-01', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854231, NOW(), NOW()),

-- 36. US importing laptops from China (List 4A rate)
(7.50, 'ad_valorem', NULL, 'percent', '2020-02-14', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

-- 37. US importing electric vehicles from China (pre-increase)
(27.50, 'ad_valorem', NULL, 'percent', '2022-01-01', '2024-09-27', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 870380, NOW(), NOW()),

-- 38. US importing solar cells from China (pre-increase)
(25.00, 'ad_valorem', NULL, 'percent', '2018-07-06', '2024-09-27', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854140, NOW(), NOW()),

-- 39. China importing from US (pre-retaliation)
(15.00, 'ad_valorem', NULL, 'percent', '2019-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

-- 40. China importing smartphones from US (initial rate)
(10.00, 'ad_valorem', NULL, 'percent', '2018-07-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 851713, NOW(), NOW()),

-- 41. EU importing steel (within quota - old rate)
(25.00, 'ad_valorem', NULL, 'percent', '2022-01-01', '2024-06-30', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 720839, NOW(), NOW()),

-- 42. Germany importing from China (pre-adjustment)
(3.50, 'ad_valorem', NULL, 'percent', '2020-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 43. US importing from Japan (pre-tariff era)
(0.00, 'ad_valorem', NULL, 'percent', '2020-01-01', '2025-07-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 870323, NOW(), NOW()),

-- 44. US importing electronics from Japan (MFN rate)
(0.00, 'ad_valorem', NULL, 'percent', '2020-01-01', '2025-07-31', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW()),

-- 45. US importing from South Korea (KORUS FTA)
(0.00, 'ad_valorem', NULL, 'percent', '2018-01-01', '2025-07-31', true,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 851713, NOW(), NOW()),

-- 46. US importing semiconductors from South Korea (pre-tariff)
(0.00, 'ad_valorem', NULL, 'percent', '2018-01-01', '2025-07-31', true,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854231, NOW(), NOW()),

-- 47. India importing smartphones from China (lower rate)
(20.00, 'ad_valorem', NULL, 'percent', '2020-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 48. India importing electronics components from China (old rate)
(7.00, 'ad_valorem', NULL, 'percent', '2021-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854232, NOW(), NOW()),

-- 49. Vietnam importing from China (higher rate)
(5.00, 'ad_valorem', NULL, 'percent', '2020-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- 50. Malaysia importing from China (pre-adjustment)
(10.00, 'ad_valorem', NULL, 'percent', '2021-01-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

-- 51. US importing steel from China (Section 232 initial)
(25.00, 'ad_valorem', NULL, 'percent', '2018-03-23', '2024-09-27', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 720839, NOW(), NOW()),

-- 52. US importing aluminum from China (Section 232 initial)
(10.00, 'ad_valorem', NULL, 'percent', '2018-03-23', '2024-09-27', false,
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 760611, NOW(), NOW()),

-- 53. China importing agricultural products from US
(5.00, 'ad_valorem', NULL, 'percent', '2018-07-01', '2023-12-31', false,
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 120190, NOW(), NOW()),

-- 54. EU importing from US (pre-reciprocal tariffs)
(4.80, 'ad_valorem', NULL, 'percent', '2020-01-01', '2025-07-26', false,
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
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

-- =====================================================
-- Database Seed Script for CS203 Tariff Management System
-- =====================================================

-- Clear existing data (in reverse order of dependencies)
DELETE FROM tariff_rate;
DELETE FROM national_tariff_lines;
DELETE FROM product_categories;
DELETE FROM country;

-- Reset sequences (PostgreSQL)
ALTER SEQUENCE IF EXISTS country_country_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS product_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS national_tariff_lines_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS tariff_rate_id_seq RESTART WITH 1;

-- =====================================================
-- COUNTRY DATA
-- =====================================================

INSERT INTO country (country_code, country_name, region, currency_code) VALUES
-- Asia-Pacific
('AU', 'Australia', 'Asia-Pacific', 'AUD'),
('CN', 'China', 'Asia-Pacific', 'CNY'),
('HK', 'Hong Kong', 'Asia-Pacific', 'HKD'),
('IN', 'India', 'Asia-Pacific', 'INR'),
('ID', 'Indonesia', 'Asia-Pacific', 'IDR'),
('JP', 'Japan', 'Asia-Pacific', 'JPY'),
('MY', 'Malaysia', 'Asia-Pacific', 'MYR'),
('NZ', 'New Zealand', 'Asia-Pacific', 'NZD'),
('PH', 'Philippines', 'Asia-Pacific', 'PHP'),
('SG', 'Singapore', 'Asia-Pacific', 'SGD'),
('KR', 'South Korea', 'Asia-Pacific', 'KRW'),
('TW', 'Taiwan', 'Asia-Pacific', 'TWD'),
('TH', 'Thailand', 'Asia-Pacific', 'THB'),
('VN', 'Vietnam', 'Asia-Pacific', 'VND'),

-- Europe
('AT', 'Austria', 'Europe', 'EUR'),
('BE', 'Belgium', 'Europe', 'EUR'),
('BG', 'Bulgaria', 'Europe', 'BGN'),
('HR', 'Croatia', 'Europe', 'EUR'),
('CY', 'Cyprus', 'Europe', 'EUR'),
('CZ', 'Czech Republic', 'Europe', 'CZK'),
('DK', 'Denmark', 'Europe', 'DKK'),
('EE', 'Estonia', 'Europe', 'EUR'),
('FI', 'Finland', 'Europe', 'EUR'),
('FR', 'France', 'Europe', 'EUR'),
('DE', 'Germany', 'Europe', 'EUR'),
('GR', 'Greece', 'Europe', 'EUR'),
('HU', 'Hungary', 'Europe', 'HUF'),
('IE', 'Ireland', 'Europe', 'EUR'),
('IT', 'Italy', 'Europe', 'EUR'),
('LV', 'Latvia', 'Europe', 'EUR'),
('LT', 'Lithuania', 'Europe', 'EUR'),
('LU', 'Luxembourg', 'Europe', 'EUR'),
('MT', 'Malta', 'Europe', 'EUR'),
('NL', 'Netherlands', 'Europe', 'EUR'),
('PL', 'Poland', 'Europe', 'PLN'),
('PT', 'Portugal', 'Europe', 'EUR'),
('RO', 'Romania', 'Europe', 'RON'),
('SK', 'Slovakia', 'Europe', 'EUR'),
('SI', 'Slovenia', 'Europe', 'EUR'),
('ES', 'Spain', 'Europe', 'EUR'),
('SE', 'Sweden', 'Europe', 'SEK'),
('CH', 'Switzerland', 'Europe', 'CHF'),
('GB', 'United Kingdom', 'Europe', 'GBP'),
('NO', 'Norway', 'Europe', 'NOK'),
('IS', 'Iceland', 'Europe', 'ISK'),

-- North America
('CA', 'Canada', 'North America', 'CAD'),
('MX', 'Mexico', 'North America', 'MXN'),
('US', 'United States', 'North America', 'USD'),

-- South America
('AR', 'Argentina', 'South America', 'ARS'),
('BO', 'Bolivia', 'South America', 'BOB'),
('BR', 'Brazil', 'South America', 'BRL'),
('CL', 'Chile', 'South America', 'CLP'),
('CO', 'Colombia', 'South America', 'COP'),
('EC', 'Ecuador', 'South America', 'USD'),
('GY', 'Guyana', 'South America', 'GYD'),
('PY', 'Paraguay', 'South America', 'PYG'),
('PE', 'Peru', 'South America', 'PEN'),
('SR', 'Suriname', 'South America', 'SRD'),
('UY', 'Uruguay', 'South America', 'UYU'),
('VE', 'Venezuela', 'South America', 'VES'),

-- Middle East & Africa
('DZ', 'Algeria', 'Africa', 'DZD'),
('EG', 'Egypt', 'Africa', 'EGP'),
('ET', 'Ethiopia', 'Africa', 'ETB'),
('GH', 'Ghana', 'Africa', 'GHS'),
('KE', 'Kenya', 'Africa', 'KES'),
('MA', 'Morocco', 'Africa', 'MAD'),
('NG', 'Nigeria', 'Africa', 'NGN'),
('ZA', 'South Africa', 'Africa', 'ZAR'),
('TN', 'Tunisia', 'Africa', 'TND'),
('AE', 'United Arab Emirates', 'Middle East', 'AED'),
('BH', 'Bahrain', 'Middle East', 'BHD'),
('IR', 'Iran', 'Middle East', 'IRR'),
('IQ', 'Iraq', 'Middle East', 'IQD'),
('IL', 'Israel', 'Middle East', 'ILS'),
('JO', 'Jordan', 'Middle East', 'JOD'),
('KW', 'Kuwait', 'Middle East', 'KWD'),
('LB', 'Lebanon', 'Middle East', 'LBP'),
('OM', 'Oman', 'Middle East', 'OMR'),
('QA', 'Qatar', 'Middle East', 'QAR'),
('SA', 'Saudi Arabia', 'Middle East', 'SAR'),
('TR', 'Turkey', 'Middle East', 'TRY'),

-- Additional Asian Countries
('BD', 'Bangladesh', 'Asia-Pacific', 'BDT'),
('BT', 'Bhutan', 'Asia-Pacific', 'BTN'),
('BN', 'Brunei', 'Asia-Pacific', 'BND'),
('KH', 'Cambodia', 'Asia-Pacific', 'KHR'),
('FJ', 'Fiji', 'Asia-Pacific', 'FJD'),
('LA', 'Laos', 'Asia-Pacific', 'LAK'),
('MV', 'Maldives', 'Asia-Pacific', 'MVR'),
('MN', 'Mongolia', 'Asia-Pacific', 'MNT'),
('MM', 'Myanmar', 'Asia-Pacific', 'MMK'),
('NP', 'Nepal', 'Asia-Pacific', 'NPR'),
('PK', 'Pakistan', 'Asia-Pacific', 'PKR'),
('PG', 'Papua New Guinea', 'Asia-Pacific', 'PGK'),
('LK', 'Sri Lanka', 'Asia-Pacific', 'LKR'),

-- Caribbean & Central America
('BS', 'Bahamas', 'Caribbean', 'BSD'),
('BB', 'Barbados', 'Caribbean', 'BBD'),
('BZ', 'Belize', 'Central America', 'BZD'),
('CR', 'Costa Rica', 'Central America', 'CRC'),
('CU', 'Cuba', 'Caribbean', 'CUP'),
('DO', 'Dominican Republic', 'Caribbean', 'DOP'),
('SV', 'El Salvador', 'Central America', 'USD'),
('GT', 'Guatemala', 'Central America', 'GTQ'),
('HT', 'Haiti', 'Caribbean', 'HTG'),
('HN', 'Honduras', 'Central America', 'HNL'),
('JM', 'Jamaica', 'Caribbean', 'JMD'),
('NI', 'Nicaragua', 'Central America', 'NIO'),
('PA', 'Panama', 'Central America', 'PAB'),
('TT', 'Trinidad and Tobago', 'Caribbean', 'TTD');

-- Display inserted countries count
-- SELECT COUNT(*) as total_countries FROM country;

-- =====================================================
-- PRODUCT CATEGORIES DATA (HS CODES)
-- =====================================================

INSERT INTO product_categories (hs_code, category_name, description, tariff_base_rate, is_active) VALUES
-- Section I: Live Animals; Animal Products (01-05)
(010111, 'Live Horses - Pure-bred breeding animals', 'Live horses for breeding purposes, pure-bred animals', 0.0, true),
(010119, 'Live Horses - Other', 'Live horses other than pure-bred breeding animals', 5.2, true),
(010121, 'Live Bovine Animals - Pure-bred breeding', 'Live cattle for breeding purposes, pure-bred animals', 0.0, true),
(010129, 'Live Bovine Animals - Other', 'Live cattle other than pure-bred breeding animals', 6.8, true),
(010221, 'Live Swine - Pure-bred breeding', 'Live pigs for breeding purposes, pure-bred animals', 0.0, true),
(010229, 'Live Swine - Other', 'Live pigs other than pure-bred breeding animals', 4.1, true),
(010311, 'Live Swine - Weighing less than 50 kg', 'Live pigs weighing less than 50 kg', 4.1, true),
(020110, 'Beef and Veal - Carcasses and half-carcasses', 'Fresh or chilled beef and veal carcasses', 12.8, true),
(020120, 'Beef and Veal - Other cuts with bone in', 'Fresh or chilled beef cuts with bone', 12.8, true),
(020130, 'Beef and Veal - Boneless', 'Fresh or chilled boneless beef and veal', 12.8, true),

-- Section II: Vegetable Products (06-14)
(060110, 'Bulbs, tubers - Dormant', 'Bulbs, tubers, tuberous roots in dormant state', 8.5, true),
(060120, 'Bulbs, tubers - In growth or flower', 'Bulbs, tubers in growth or in flower; chicory plants', 8.5, true),
(070110, 'Potatoes - Seed', 'Seed potatoes for planting', 2.1, true),
(070190, 'Potatoes - Other', 'Fresh or chilled potatoes, other than seed', 4.8, true),
(080111, 'Coconuts - Desiccated', 'Desiccated coconuts', 5.4, true),
(080119, 'Coconuts - Other', 'Fresh coconuts, other than desiccated', 5.4, true),
(080211, 'Almonds - In shell', 'Fresh or dried almonds in shell', 3.2, true),
(080212, 'Almonds - Shelled', 'Fresh or dried shelled almonds', 3.2, true),
(090111, 'Coffee - Not roasted, not decaffeinated', 'Coffee, not roasted, not decaffeinated', 0.0, true),
(090112, 'Coffee - Not roasted, decaffeinated', 'Coffee, not roasted, decaffeinated', 0.0, true),

-- Section III: Animal or Vegetable Fats and Oils (15)
(150710, 'Soya-bean oil - Crude', 'Crude soya-bean oil, whether or not degummed', 9.6, true),
(150790, 'Soya-bean oil - Other', 'Refined soya-bean oil and fractions', 9.6, true),
(151110, 'Palm oil - Crude', 'Crude palm oil', 0.0, true),
(151190, 'Palm oil - Other', 'Refined palm oil and fractions', 5.1, true),
(151211, 'Sunflower-seed oil - Crude', 'Crude sunflower-seed oil', 9.6, true),
(151219, 'Sunflower-seed oil - Other', 'Refined sunflower-seed oil and fractions', 9.6, true),

-- Section IV: Prepared Foodstuffs (16-24)
(160100, 'Sausages and similar products', 'Sausages and similar products of meat, meat offal or blood', 15.4, true),
(170111, 'Cane sugar - Raw', 'Raw cane sugar, not containing added flavouring', 0.0, true),
(170112, 'Beet sugar - Raw', 'Raw beet sugar, not containing added flavouring', 0.0, true),
(170191, 'Refined cane sugar', 'Refined cane sugar, containing added flavouring', 0.0, true),
(180100, 'Cocoa beans', 'Cocoa beans, whole or broken, raw or roasted', 0.0, true),
(180200, 'Cocoa shells, husks, skins', 'Cocoa shells, husks, skins and other cocoa waste', 0.0, true),

-- Section V: Mineral Products (25-27)
(250100, 'Salt - Suitable for human consumption', 'Salt suitable for human consumption', 0.0, true),
(250200, 'Unroasted iron pyrites', 'Unroasted iron pyrites', 0.0, true),
(260111, 'Iron ores - Non-agglomerated', 'Iron ores and concentrates, non-agglomerated', 0.0, true),
(260112, 'Iron ores - Agglomerated', 'Iron ores and concentrates, agglomerated', 0.0, true),
(270111, 'Coal - Anthracite', 'Anthracite coal, whether or not pulverised', 0.0, true),
(270112, 'Coal - Bituminous', 'Bituminous coal, whether or not pulverised', 0.0, true),

-- Section VI: Products of Chemical Industries (28-38)
(280461, 'Silicon - Containing by weight ≥99.99% silicon', 'Silicon containing by weight not less than 99.99% of silicon', 3.7, true),
(280469, 'Silicon - Other', 'Silicon containing by weight less than 99.99% of silicon', 5.5, true),
(290110, 'Saturated acyclic hydrocarbons', 'Saturated acyclic hydrocarbons', 5.5, true),
(290121, 'Ethylene', 'Ethylene', 5.5, true),
(290122, 'Propene (propylene)', 'Propene (propylene)', 5.5, true),

-- Section VII: Plastics and Rubber (39-40)
(390110, 'Polyethylene - Specific gravity < 0.94', 'Polyethylene having a specific gravity of less than 0.94', 6.5, true),
(390120, 'Polyethylene - Specific gravity ≥ 0.94', 'Polyethylene having a specific gravity of 0.94 or more', 6.5, true),
(390210, 'Polypropylene', 'Polypropylene in primary forms', 6.5, true),
(400110, 'Natural rubber latex', 'Natural rubber latex, whether or not pre-vulcanised', 0.0, true),
(400121, 'Natural rubber - Smoked sheets', 'Natural rubber in the form of smoked sheets', 0.0, true),

-- Section VIII: Raw Hides, Skins, Leather (41-43)
(410110, 'Raw bovine hides - Whole', 'Raw hides and skins of bovine animals, whole', 0.0, true),
(410120, 'Raw bovine hides - Other', 'Raw hides and skins of bovine animals, other than whole', 0.0, true),
(420211, 'Trunks, suitcases - With outer surface of leather', 'Trunks and suitcases with outer surface of leather', 3.7, true),
(420212, 'Trunks, suitcases - With outer surface of plastics', 'Trunks and suitcases with outer surface of plastics', 3.7, true),

-- Section IX: Wood and Wood Products (44-46)
(440110, 'Fuel wood - In logs, billets, twigs', 'Fuel wood, in logs, in billets, in twigs, in faggots', 0.0, true),
(440121, 'Wood chips - Coniferous', 'Wood chips or particles of coniferous wood', 0.0, true),
(440122, 'Wood chips - Non-coniferous', 'Wood chips or particles of non-coniferous wood', 0.0, true),
(441011, 'Particle board - Of wood', 'Particle board and similar board of wood', 0.0, true),

-- Section X: Pulp of Wood, Paper (47-49)
(470100, 'Mechanical wood pulp', 'Mechanical wood pulp', 0.0, true),
(470200, 'Chemical wood pulp - Dissolving grades', 'Chemical wood pulp, dissolving grades', 0.0, true),
(480100, 'Newsprint', 'Newsprint, in rolls or sheets', 0.0, true),
(480211, 'Uncoated paper - Weighing < 40 g/m²', 'Uncoated paper for writing, printing, weighing less than 40 g/m²', 0.0, true),

-- Section XI: Textiles (50-63)
(500100, 'Silkworm cocoons', 'Silkworm cocoons suitable for reeling', 0.0, true),
(510111, 'Wool - Greasy, not carded or combed', 'Greasy wool, not carded or combed, including fleece-washed wool', 0.0, true),
(520100, 'Cotton - Not carded or combed', 'Cotton, not carded or combed', 0.0, true),
(610110, 'Men\'s overcoats - Knitted or crocheted', 'Men\'s or boys\' overcoats, knitted or crocheted', 12.0, true),

-- Section XII: Footwear, Headgear (64-67)
(640110, 'Waterproof footwear - Covering the knee', 'Waterproof footwear with outer soles and uppers of rubber or plastics, covering the knee', 8.5, true),
(640191, 'Waterproof footwear - Covering the ankle', 'Waterproof footwear covering the ankle but not covering the knee', 8.5, true),
(650100, 'Hat-forms, hat bodies', 'Hat-forms, hat bodies and hoods of felt', 2.7, true),

-- Section XIII: Stone, Plaster, Ceramic, Glass (68-70)
(680100, 'Natural stone - Roughly trimmed', 'Natural stone (except slate) roughly trimmed or merely cut', 0.0, true),
(690100, 'Bricks, blocks - Of siliceous fossil meals', 'Bricks, blocks, tiles and other ceramic goods of siliceous fossil meals', 0.0, true),
(700100, 'Cullet and other waste glass', 'Cullet and other waste and scrap of glass', 0.0, true),

-- Section XIV: Pearls, Precious Stones, Metals (71)
(710110, 'Natural pearls', 'Natural pearls, whether or not worked or graded', 0.0, true),
(710210, 'Diamonds - Unsorted', 'Diamonds, unsorted', 0.0, true),
(710391, 'Rubies - Unworked or simply sawn', 'Rubies, sapphires and emeralds, unworked or simply sawn or roughly shaped', 0.0, true),

-- Section XV: Base Metals (72-83)
(720110, 'Pig iron - Non-alloy', 'Non-alloy pig iron containing by weight 0.5% or less of phosphorus', 0.0, true),
(720150, 'Pig iron - Alloy', 'Alloy pig iron; spiegeleisen', 0.0, true),
(730110, 'Sheet piling - Of iron or steel', 'Sheet piling of iron or steel', 0.0, true),
(740100, 'Copper mattes', 'Copper mattes; cement copper (precipitated copper)', 0.0, true),

-- Section XVI: Machinery, Mechanical Appliances (84-85)
(840111, 'Nuclear reactors', 'Nuclear reactors', 0.0, true),
(840211, 'Watertube boilers - Steam producing', 'Watertube boilers with a steam production exceeding 45 t per hour', 0.0, true),
(841011, 'Hydraulic turbines - Of a power ≤1,000 kW', 'Hydraulic turbines and water wheels, of a power not exceeding 1,000 kW', 0.0, true),
(850110, 'Motors - Of an output ≤37.5 W', 'Motors of an output not exceeding 37.5 W', 2.7, true),

-- Section XVII: Vehicles, Aircraft, Vessels (86-89)
(860110, 'Railway locomotives - Powered from external source', 'Railway locomotives powered from an external source of electricity', 0.0, true),
(870110, 'Tractors - Road tractors for semi-trailers', 'Road tractors for semi-trailers', 10.0, true),
(870120, 'Tractors - Other', 'Motor vehicles for the transport of ten or more persons', 10.0, true),
(880100, 'Balloons and dirigibles', 'Balloons and dirigibles; gliders, hang gliders', 0.0, true),

-- Section XVIII: Optical, Photographic, Medical Instruments (90-92)
(900110, 'Optical fibres - Individual', 'Optical fibres, optical fibre bundles and cables', 0.0, true),
(901011, 'Photographic equipment - For colour photography', 'Apparatus and equipment for photographic laboratories, for colour photography', 0.0, true),
(920110, 'Pianos - Upright pianos', 'Upright pianos', 3.7, true),

-- Section XIX: Arms and Ammunition (93)
(930100, 'Military weapons - Other than revolvers', 'Military weapons, other than revolvers, pistols', 0.0, true),
(930200, 'Revolvers and pistols', 'Revolvers and pistols, other than those of heading 93.03 or 93.04', 0.0, true),

-- Section XX: Miscellaneous Manufactured Articles (94-96)
(940110, 'Seats - Aircraft seats', 'Seats of a kind used in aircraft', 0.0, true),
(940130, 'Seats - Swivel seats with variable height adjustment', 'Swivel seats with variable height adjustment', 0.0, true),
(950110, 'Wheeled toys - Designed to be ridden by children', 'Wheeled toys designed to be ridden by children', 0.0, true),

-- Section XXI: Works of Art (97)
(970100, 'Paintings, drawings - Original', 'Paintings, drawings and pastels, executed entirely by hand', 0.0, true),
(970200, 'Original engravings, prints', 'Original engravings, prints and lithographs', 0.0, true);

-- Display inserted product categories count
-- SELECT COUNT(*) as total_product_categories FROM product_categories;

-- =====================================================
-- USER DATA
-- =====================================================

INSERT INTO users (username, email, password, role, full_name, created_at) VALUES
('admin', 'admin@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', 'ADMIN', 'System Administrator', NOW()),
('john.doe', 'john.doe@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', 'MANAGER', 'John Doe', NOW()),
('jane.smith', 'jane.smith@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', 'MANAGER', 'Jane Smith', NOW()),
('bob.wilson', 'bob.wilson@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', 'USER', 'Bob Wilson', NOW()),
('alice.brown', 'alice.brown@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', 'USER', 'Alice Brown', NOW());

-- Note: Password is 'password123' for all users (hashed with BCrypt)

-- =====================================================
-- NATIONAL TARIFF LINES DATA
-- =====================================================

INSERT INTO national_tariff_lines (country_id, tariff_line_code, description, parent_hs_code, level, created_by, updated_by) VALUES
-- Singapore National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.01011110', 'Pure-bred breeding horses - Male', 010111, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.01011120', 'Pure-bred breeding horses - Female', 010111, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.02011000', 'Fresh or chilled beef carcasses', 020110, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.07011000', 'Seed potatoes for planting', 070110, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.08011100', 'Desiccated coconuts', 080111, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.15071000', 'Crude soya-bean oil', 150710, 8, 1, 1),

-- Malaysia National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.010111.10', 'Pure-bred breeding stallions', 010111, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.010111.20', 'Pure-bred breeding mares', 010111, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.151110.00', 'Crude palm oil', 151110, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.151190.10', 'Refined palm oil', 151190, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.180100.00', 'Cocoa beans, whole or broken', 180100, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.400110.00', 'Natural rubber latex', 400110, 8, 2, 2),

-- Thailand National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.070190.10', 'Fresh potatoes for consumption', 070190, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.080119.00', 'Fresh coconuts', 080119, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.170111.00', 'Raw cane sugar', 170111, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.400121.00', 'Smoked rubber sheets', 400121, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.520100.00', 'Cotton, not carded', 520100, 8, 3, 3),

-- China National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.720110.00', 'Non-alloy pig iron', 720110, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.840111.00', 'Nuclear reactors', 840111, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.870110.10', 'Road tractors, new', 870110, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.870110.90', 'Road tractors, used', 870110, 8, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.390110.00', 'Low density polyethylene', 390110, 8, 1, 1),

-- United States National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.0101.11.10', 'Thoroughbred horses for racing', 010111, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.0201.10.05', 'High quality beef carcasses', 020110, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.0801.11.00', 'Desiccated coconuts', 080111, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8701.10.10', 'Road tractors, diesel', 870110, 8, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8701.10.50', 'Road tractors, other', 870110, 8, 2, 2),

-- European Union (Germany) National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.01011110', 'Pure-bred breeding horses, male', 010111, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.02011000', 'Beef carcasses, fresh', 020110, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.87011010', 'Truck tractors, new', 870110, 8, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.84021100', 'Steam boilers, large', 840211, 8, 3, 3),

-- Japan National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.010111.000', 'Pure-bred breeding horses', 010111, 9, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.020110.000', 'Beef carcasses and half-carcasses', 020110, 9, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.870110.000', 'Road tractors for semi-trailers', 870110, 9, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.390110.000', 'Polyethylene, low density', 390110, 9, 4, 4),

-- India National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.01011100', 'Live horses, pure-bred breeding', 010111, 8, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.52010000', 'Cotton, not carded or combed', 520100, 8, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.72011000', 'Pig iron, non-alloy', 720110, 8, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.87011000', 'Tractors, road', 870110, 8, 5, 5);

-- =====================================================
-- TARIFF RATE DATA
-- =====================================================

INSERT INTO tariff_rates (tariff_rate, tariff_type, rate_unit, effective_date, expiry_date, preferential_tariff, 
                         importing_country_id, exporting_country_id, hs_code, created_at, updated_at) VALUES

-- Singapore importing from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 010111, NOW(), NOW()),

(5.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 020110, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 151110, NOW(), NOW()),

(2.5000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 400121, NOW(), NOW()),

-- Malaysia importing from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 010111, NOW(), NOW()),

(15.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 020110, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 180100, NOW(), NOW()),

(10.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 390110, NOW(), NOW()),

-- Thailand importing from various countries
(20.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 020110, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 151110, NOW(), NOW()),

(5.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 520100, NOW(), NOW()),

-- China importing from various countries
(8.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 020110, NOW(), NOW()),

(25.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 870110, NOW(), NOW()),

(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 720110, NOW(), NOW()),

-- United States importing from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 010111, NOW(), NOW()),

(26.4000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 020110, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 080111, NOW(), NOW()),

(4.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 870110, NOW(), NOW()),

-- European Union (Germany) importing from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 010111, NOW(), NOW()),

(12.8000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'AR'), 
 020110, NOW(), NOW()),

(10.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 870110, NOW(), NOW()),

-- Japan importing from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 010111, NOW(), NOW()),

(38.5000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 020110, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 020110, NOW(), NOW()),

-- India importing from various countries
(30.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 010111, NOW(), NOW()),

(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 520100, NOW(), NOW()),

(7.5000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 870110, NOW(), NOW()),

-- Cross-regional trade examples
(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 870110, NOW(), NOW()),

(5.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'BR'), 
 (SELECT country_id FROM country WHERE country_code = 'AR'), 
 020110, NOW(), NOW()),

(0.0000, 'EU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'FR'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 870110, NOW(), NOW()),

-- Some expired rates for historical data
(12.0000, 'MFN', 'ad valorem', '2023-01-01', '2023-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 870110, NOW(), NOW()),

(15.0000, 'MFN', 'ad valorem', '2023-06-01', '2023-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 390110, NOW(), NOW());

-- Display summary counts
-- SELECT COUNT(*) as total_users FROM users;
-- SELECT COUNT(*) as total_national_tariff_lines FROM national_tariff_lines;
-- SELECT COUNT(*) as total_tariff_rates FROM tariff_rates;

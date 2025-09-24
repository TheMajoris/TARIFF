-- =====================================================
-- Database Seed Script for CS203 Tariff Management System
-- =====================================================

-- Clear existing data (in reverse order of dependencies)
DELETE FROM tariff_rates;
DELETE FROM national_tariff_lines;
DELETE FROM product_categories;
DELETE FROM country;
DELETE FROM users;

-- Reset sequences (PostgreSQL)
ALTER SEQUENCE IF EXISTS users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS country_country_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS product_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS national_tariff_lines_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS tariff_rates_id_seq RESTART WITH 1;

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
-- ELECTRONICS PRODUCT CATEGORIES DATA (HS CODES)
-- =====================================================

INSERT INTO product_categories (hs_code, category_name, description, tariff_base_rate, is_active) VALUES
-- Semiconductors and Electronic Components (HS 85)
(850110, 'Electric Motors ≤ 37.5W', 'Electric motors of an output not exceeding 37.5 W', 2.7, true),
(850120, 'Universal AC/DC Motors > 37.5W', 'Universal AC/DC motors of an output exceeding 37.5 W', 2.7, true),
(850131, 'DC Motors > 37.5W ≤ 750W', 'DC motors of an output exceeding 37.5 W but not exceeding 750 W', 2.7, true),
(850132, 'DC Motors > 750W ≤ 75kW', 'DC motors of an output exceeding 750 W but not exceeding 75 kW', 2.7, true),
(850140, 'AC Motors > 37.5W', 'AC motors, single-phase, of an output exceeding 37.5 W', 2.7, true),
(850211, 'Diesel Generating Sets ≤ 75kVA', 'Generating sets with compression-ignition internal combustion piston engines, ≤ 75kVA', 0.0, true),
(850212, 'Diesel Generating Sets 75-375kVA', 'Generating sets with compression-ignition internal combustion piston engines, 75-375kVA', 0.0, true),
(850213, 'Diesel Generating Sets > 375kVA', 'Generating sets with compression-ignition internal combustion piston engines, > 375kVA', 0.0, true),
(850231, 'Wind-powered Generating Sets', 'Wind-powered generating sets', 0.0, true),
(850239, 'Other Generating Sets', 'Other generating sets', 0.0, true),
(850300, 'Electric Motor Parts', 'Parts suitable for use solely or principally with electric motors and generators', 0.0, true),
(850410, 'Ballasts for Discharge Lamps', 'Ballasts for discharge lamps or tubes', 0.0, true),
(850421, 'Liquid Dielectric Transformers ≤ 650kVA', 'Liquid dielectric transformers having a power handling capacity not exceeding 650 kVA', 0.0, true),
(850422, 'Liquid Dielectric Transformers 650kVA-10MVA', 'Liquid dielectric transformers having a power handling capacity exceeding 650 kVA but not exceeding 10,000 kVA', 0.0, true),
(850423, 'Liquid Dielectric Transformers > 10MVA', 'Liquid dielectric transformers having a power handling capacity exceeding 10,000 kVA', 0.0, true),
(850431, 'Dry-type Transformers ≤ 1kVA', 'Transformers having a power handling capacity not exceeding 1 kVA', 0.0, true),
(850432, 'Dry-type Transformers 1-16kVA', 'Transformers having a power handling capacity exceeding 1 kVA but not exceeding 16 kVA', 0.0, true),
(850433, 'Dry-type Transformers 16-500kVA', 'Transformers having a power handling capacity exceeding 16 kVA but not exceeding 500 kVA', 0.0, true),
(850434, 'Dry-type Transformers > 500kVA', 'Transformers having a power handling capacity exceeding 500 kVA', 0.0, true),
(850440, 'Static Converters', 'Static converters', 0.0, true),
(850450, 'Inductors', 'Other inductors', 0.0, true),
(850490, 'Transformer Parts', 'Parts of transformers, static converters and inductors', 0.0, true),

-- Semiconductors (HS 8541-8542)
(854110, 'Diodes', 'Diodes, other than photosensitive or light-emitting diodes', 0.0, true),
(854121, 'Transistors ≤ 1W', 'Transistors, other than photosensitive transistors, with a dissipation rate of less than 1 W', 0.0, true),
(854129, 'Transistors > 1W', 'Transistors, other than photosensitive transistors, with a dissipation rate of 1 W or more', 0.0, true),
(854130, 'Thyristors', 'Thyristors, diacs and triacs, other than photosensitive devices', 0.0, true),
(854140, 'Photosensitive Semiconductors', 'Photosensitive semiconductor devices, including photovoltaic cells', 0.0, true),
(854150, 'Semiconductor Devices NES', 'Other semiconductor devices', 0.0, true),
(854160, 'Mounted Piezoelectric Crystals', 'Mounted piezoelectric crystals', 0.0, true),
(854170, 'LED Diodes', 'Light-emitting diodes (LED)', 0.0, true),
(854190, 'Semiconductor Device Parts', 'Parts of semiconductor devices and electronic integrated circuits', 0.0, true),

-- Integrated Circuits (HS 8542)
(854211, 'Memory Circuits - Cards/Modules', 'Cards incorporating an electronic integrated circuit (smart cards)', 0.0, true),
(854213, 'Metal Oxide Semiconductors', 'Metal oxide semiconductors (MOS technology)', 0.0, true),
(854214, 'Digital Monolithic ICs', 'Digital monolithic integrated circuits', 0.0, true),
(854219, 'Other Monolithic ICs', 'Other monolithic integrated circuits', 0.0, true),
(854221, 'Hybrid ICs - Thick/Thin Film', 'Hybrid integrated circuits for thick or thin film technology', 0.0, true),
(854229, 'Other Hybrid ICs', 'Other hybrid integrated circuits', 0.0, true),
(854231, 'Processors and Controllers', 'Processors and controllers, whether or not combined with memories, converters, logic circuits, amplifiers, clock and timing circuits', 0.0, true),
(854232, 'Memories', 'Memories', 0.0, true),
(854233, 'Amplifiers', 'Amplifiers', 0.0, true),
(854239, 'Other Electronic ICs', 'Other electronic integrated circuits', 0.0, true),
(854290, 'IC Parts', 'Parts of electronic integrated circuits and microassemblies', 0.0, true),

-- Electronic Equipment (HS 8517-8528)
(851711, 'Line Telephone Sets', 'Line telephone sets with cordless handsets', 0.0, true),
(851712, 'Cordless Telephones', 'Telephones for cellular networks or for other wireless networks', 0.0, true),
(851713, 'Smartphones', 'Smartphones', 0.0, true),
(851718, 'Other Telephone Sets', 'Other telephone sets and apparatus for transmission or reception of voice, images or other data', 0.0, true),
(851761, 'Base Stations', 'Base stations for wireless telecommunications', 0.0, true),
(851762, 'Machines for Reception/Conversion', 'Machines for the reception, conversion and transmission or regeneration of voice, images or other data', 0.0, true),
(851769, 'Other Telecom Equipment', 'Other apparatus for transmission or reception of voice, images or other data', 0.0, true),
(851770, 'Telecom Parts', 'Parts of telephone sets and other apparatus for transmission or reception of voice, images or other data', 0.0, true),

-- Computing Equipment (HS 8471)
(847110, 'Analog/Hybrid Computers', 'Analogue or hybrid automatic data processing machines', 0.0, true),
(847141, 'Digital Computers ≤ 10kg', 'Comprising in the same housing at least a central processing unit and an input and output unit, whether or not combined, weighing not more than 10 kg', 0.0, true),
(847149, 'Other Digital Processing Units', 'Other digital automatic data processing machines presented in the form of systems', 0.0, true),
(847150, 'Processing Units', 'Processing units, whether or not containing in the same housing one or two of the following types of unit: storage units, input units, output units', 0.0, true),
(847160, 'Input/Output Units', 'Input or output units, whether or not containing storage units in the same housing', 0.0, true),
(847170, 'Storage Units', 'Storage units', 0.0, true),
(847180, 'Other ADP Units', 'Other units of automatic data processing machines', 0.0, true),
(847190, 'ADP Machine Parts', 'Parts and accessories of automatic data processing machines and units thereof', 0.0, true),

-- Electronic Displays (HS 8528)
(852851, 'Monitors - CRT', 'Monitors capable of directly connecting to and designed for use with an automatic data processing machine, using cathode-ray tube technology', 6.4, true),
(852852, 'Monitors - LCD/LED', 'Monitors capable of directly connecting to and designed for use with an automatic data processing machine, using liquid crystal display (LCD) technology', 6.4, true),
(852859, 'Other Monitors', 'Other monitors capable of directly connecting to and designed for use with an automatic data processing machine', 6.4, true),
(852861, 'Projectors - CRT', 'Projectors capable of directly connecting to and designed for use with an automatic data processing machine, using cathode-ray tube technology', 6.4, true),
(852862, 'Projectors - LCD', 'Projectors capable of directly connecting to and designed for use with an automatic data processing machine, using liquid crystal display (LCD) technology', 6.4, true),
(852869, 'Other Projectors', 'Other projectors capable of directly connecting to and designed for use with an automatic data processing machine', 6.4, true),
(852871, 'TV Receivers - CRT', 'Reception apparatus for television, whether or not incorporating radio-broadcast receivers or sound or video recording or reproducing apparatus, using cathode-ray tube technology', 6.4, true),
(852872, 'TV Receivers - LCD/LED/OLED', 'Reception apparatus for television, color, with a flat panel screen, using liquid crystal display (LCD), light-emitting diode (LED) or organic light-emitting diode (OLED) technology', 6.4, true),
(852873, 'TV Receivers - Other Color', 'Other color reception apparatus for television', 6.4, true),

-- Audio/Video Equipment (HS 8519-8522)
(851910, 'Coin/Token-operated Sound Equipment', 'Coin- or disc-operated record-players', 0.0, true),
(851920, 'Sound Recording/Reproducing Apparatus', 'Apparatus operated by coins, banknotes, bank cards, tokens or by other means of payment', 0.0, true),
(851930, 'Turntables', 'Turntables (record-decks)', 0.0, true),
(851940, 'Transcribing Machines', 'Transcribing machines and other sound reproducing apparatus', 0.0, true),
(851950, 'Telephone Answering Machines', 'Telephone answering machines', 0.0, true),

-- Electronic Components and Parts
(853110, 'Burglar/Fire Alarms', 'Burglar or fire alarms and similar apparatus', 0.0, true),
(853120, 'Indicator Panels', 'Indicator panels incorporating liquid crystal devices (LCD) or light-emitting diodes (LED)', 0.0, true),
(853180, 'Other Electric Sound/Visual Signaling', 'Other electric sound or visual signalling apparatus', 0.0, true),
(853190, 'Signaling Apparatus Parts', 'Parts of electric sound or visual signalling apparatus', 0.0, true),

-- Electronic Capacitors (HS 8532)
(853210, 'Fixed Capacitors - Tantalum', 'Fixed capacitors designed for use in 50/60 Hz circuits and having a reactive power handling capacity of not less than 0.5 kvar (power capacitors)', 0.0, true),
(853221, 'Fixed Capacitors - Tantalum', 'Tantalum capacitors', 0.0, true),
(853222, 'Fixed Capacitors - Aluminum Electrolytic', 'Aluminium electrolytic capacitors', 0.0, true),
(853223, 'Fixed Capacitors - Ceramic Dielectric', 'Capacitors with ceramic dielectric, single layer', 0.0, true),
(853224, 'Fixed Capacitors - Ceramic Multilayer', 'Capacitors with ceramic dielectric, multilayer', 0.0, true),
(853225, 'Fixed Capacitors - Paper/Plastic', 'Capacitors with dielectric of paper or plastics', 0.0, true),
(853229, 'Other Fixed Capacitors', 'Other fixed capacitors', 0.0, true),
(853230, 'Variable/Adjustable Capacitors', 'Variable or adjustable (pre-set) capacitors', 0.0, true),
(853290, 'Capacitor Parts', 'Parts of capacitors', 0.0, true);

-- Display inserted product categories count
-- SELECT COUNT(*) as total_product_categories FROM product_categories;

-- =====================================================
-- USER DATA
-- =====================================================

INSERT INTO users (username, email, password_hash, is_admin, first_name, last_name, enabled, created_at) VALUES
('admin', 'admin@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', true, 'System', 'Administrator', true, NOW()),
('john.doe', 'john.doe@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', true, 'John', 'Doe', true, NOW()),
('jane.smith', 'jane.smith@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', false, 'Jane', 'Smith', true, NOW()),
('bob.wilson', 'bob.wilson@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', false, 'Bob', 'Wilson', true, NOW()),
('alice.brown', 'alice.brown@cs203.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tqNIr6UIOjKm4i', false, 'Alice', 'Brown', true, NOW());

-- Note: Password is 'password123' for all users (hashed with BCrypt)

-- =====================================================
-- ELECTRONICS NATIONAL TARIFF LINES DATA
-- =====================================================

INSERT INTO national_tariff_lines (country_id, tariff_line_code, description, parent_hs_code, level, created_by, updated_by) VALUES
-- Singapore National Tariff Lines (Electronics Hub)
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85171210', 'Smartphones - 5G capable', 851712, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85171220', 'Smartphones - 4G/LTE', 851712, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85171300', 'Smartphones - Premium tier', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85411010', 'Semiconductor diodes - Silicon', 854110, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85411020', 'Semiconductor diodes - Gallium arsenide', 854110, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85423100', 'Microprocessors - ARM based', 854231, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85423200', 'Memory chips - DDR4/DDR5 RAM', 854232, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), 'SG.85285200', 'LCD monitors - 4K resolution', 852852, 10, 1, 1),

-- Malaysia National Tariff Lines (Manufacturing Hub)
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.854231.10', 'Microprocessors - Intel x86', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.854231.20', 'Microprocessors - AMD processors', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.854232.10', 'Flash memory - NAND chips', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.854232.20', 'DRAM memory - Server grade', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.847141.00', 'Laptops - Consumer grade', 847141, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.847149.10', 'Desktop computers - Gaming', 847149, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), 'MY.847149.20', 'Desktop computers - Business', 847149, 10, 2, 2),

-- Thailand National Tariff Lines (Assembly Operations)
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.852872.10', 'LED TVs - 32-43 inch', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.852872.20', 'LED TVs - 55-65 inch', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.852872.30', 'LED TVs - 75+ inch', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.851712.00', 'Mobile phones - Feature phones', 851712, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.851713.00', 'Smartphones - Budget tier', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), 'TH.847160.00', 'Keyboards and mouse - Wireless', 847160, 10, 3, 3),

-- China National Tariff Lines (Major Electronics Producer)
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.85423100', 'Processors - Mobile chipsets', 854231, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.85423200', 'Memory - Consumer DRAM', 854232, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.85171300', 'Smartphones - Mid-range', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.85285200', 'Computer monitors - Gaming', 852852, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.85411000', 'Power diodes - High voltage', 854110, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.85417000', 'LED chips - White light', 854170, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), 'CN.84714100', 'Tablets - Android based', 847141, 10, 1, 1),

-- United States National Tariff Lines (High-tech Focus)
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8542.31.00', 'Microprocessors - Server CPUs', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8542.32.00', 'Memory - ECC server RAM', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8517.13.00', 'Smartphones - iPhone series', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8471.41.01', 'Laptops - MacBook series', 847141, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8471.49.01', 'Workstations - High-end', 847149, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), 'US.8528.52.10', 'Professional monitors - 4K+', 852852, 10, 2, 2),

-- European Union (Germany) National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.85423100', 'Automotive processors', 854231, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.85411000', 'Industrial diodes', 854110, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.85171300', 'Smartphones - Enterprise', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.84714900', 'Industrial computers', 847149, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), 'DE.85285200', 'Industrial displays', 852852, 10, 3, 3),

-- Japan National Tariff Lines (Precision Electronics)
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.854231.000', 'Microcontrollers - Automotive', 854231, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.854110.000', 'Precision diodes', 854110, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.852872.000', 'OLED displays - Premium', 852872, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.851713.000', 'Smartphones - Sony Xperia', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), 'JP.847141.000', 'Portable gaming devices', 847141, 10, 4, 4),

-- India National Tariff Lines (Growing Market)
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.85171300', 'Smartphones - Domestic brands', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.85287200', 'LED TVs - Budget segment', 852872, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.84714100', 'Tablets - Education sector', 847141, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.85423100', 'Mobile processors - Entry level', 854231, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), 'IN.85411000', 'Basic diodes - Consumer electronics', 854110, 10, 5, 5);

-- =====================================================
-- TARIFF RATE DATA
-- =====================================================

INSERT INTO tariff_rates (tariff_rate, tariff_type, rate_unit, effective_date, expiry_date, preferential_tariff, 
                         importing_country_id, exporting_country_id, hs_code, created_at, updated_at) VALUES

-- Singapore importing electronics from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(5.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 847141, NOW(), NOW()),

(2.5000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 852872, NOW(), NOW()),

-- Malaysia importing electronics from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW()),

(15.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 851713, NOW(), NOW()),

(10.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 852872, NOW(), NOW()),

-- Thailand importing electronics from various countries
(20.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847160, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 852872, NOW(), NOW()),

(5.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 851713, NOW(), NOW()),

-- China importing electronics from various countries
(8.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854170, NOW(), NOW()),

(25.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854232, NOW(), NOW()),

-- United States importing electronics from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 847141, NOW(), NOW()),

(26.4000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 854110, NOW(), NOW()),

(4.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 852872, NOW(), NOW()),

-- European Union (Germany) importing electronics from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(12.8000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(10.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847149, NOW(), NOW()),

-- Japan importing electronics from various countries
(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854232, NOW(), NOW()),

(38.5000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.0000, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'TW'), 
 852872, NOW(), NOW()),

-- India importing electronics from various countries
(30.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854110, NOW(), NOW()),

(7.5000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 852872, NOW(), NOW()),

-- Cross-regional electronics trade examples
(0.0000, 'NAFTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

(5.0000, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'BR'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 851713, NOW(), NOW()),

(0.0000, 'EU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'FR'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 854231, NOW(), NOW()),

-- Some expired rates for historical data
(12.0000, 'MFN', 'ad valorem', '2023-01-01', '2023-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 852872, NOW(), NOW()),

(15.0000, 'MFN', 'ad valorem', '2023-06-01', '2023-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW());

-- Display summary counts
-- SELECT COUNT(*) as total_users FROM users;
-- SELECT COUNT(*) as total_national_tariff_lines FROM national_tariff_lines;
-- SELECT COUNT(*) as total_tariff_rates FROM tariff_rates;

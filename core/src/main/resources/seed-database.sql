-- =====================================================
-- Database Seed Script for CS203 Tariff Management System
-- =====================================================
-- This script populates the database with seed data for production/development
-- Schema creation is handled by Hibernate (JPA_DDL_AUTO=create-drop)
-- This script is disabled in test environment via application-test.properties

-- Clear any existing data (just in case)
DELETE FROM tariff_rates;
DELETE FROM national_tariff_lines;
DELETE FROM product_categories;
DELETE FROM country;
DELETE FROM users;
DELETE FROM news_tags;
DELETE FROM news;

-- =====================================================
-- COUNTRY DATA (UN M49 Standard Regional Classification)
-- =====================================================

INSERT INTO country (country_code, country_name, region, currency_code) VALUES

-- =====================================================
-- AFRICA (UN M49 Region Code: 002)
-- =====================================================
('DZ', 'Algeria', 'Africa', 'DZD'),
('AO', 'Angola', 'Africa', 'AOA'),
('BJ', 'Benin', 'Africa', 'XOF'),
('BW', 'Botswana', 'Africa', 'BWP'),
('BF', 'Burkina Faso', 'Africa', 'XOF'),
('BI', 'Burundi', 'Africa', 'BIF'),
('CM', 'Cameroon', 'Africa', 'XAF'),
('CV', 'Cape Verde', 'Africa', 'CVE'),
('CF', 'Central African Republic', 'Africa', 'XAF'),
('TD', 'Chad', 'Africa', 'XAF'),
('KM', 'Comoros', 'Africa', 'KMF'),
('CG', 'Congo', 'Africa', 'XAF'),
('CD', 'Democratic Republic of the Congo', 'Africa', 'CDF'),
('DJ', 'Djibouti', 'Africa', 'DJF'),
('EG', 'Egypt', 'Africa', 'EGP'),
('GQ', 'Equatorial Guinea', 'Africa', 'XAF'),
('ER', 'Eritrea', 'Africa', 'ERN'),
('SZ', 'Eswatini', 'Africa', 'SZL'),
('ET', 'Ethiopia', 'Africa', 'ETB'),
('GA', 'Gabon', 'Africa', 'XAF'),
('GM', 'Gambia', 'Africa', 'GMD'),
('GH', 'Ghana', 'Africa', 'GHS'),
('GN', 'Guinea', 'Africa', 'GNF'),
('GW', 'Guinea-Bissau', 'Africa', 'XOF'),
('CI', 'Ivory Coast', 'Africa', 'XOF'),
('KE', 'Kenya', 'Africa', 'KES'),
('LS', 'Lesotho', 'Africa', 'LSL'),
('LR', 'Liberia', 'Africa', 'LRD'),
('LY', 'Libya', 'Africa', 'LYD'),
('MG', 'Madagascar', 'Africa', 'MGA'),
('MW', 'Malawi', 'Africa', 'MWK'),
('ML', 'Mali', 'Africa', 'XOF'),
('MR', 'Mauritania', 'Africa', 'MRU'),
('MU', 'Mauritius', 'Africa', 'MUR'),
('MA', 'Morocco', 'Africa', 'MAD'),
('MZ', 'Mozambique', 'Africa', 'MZN'),
('NA', 'Namibia', 'Africa', 'NAD'),
('NE', 'Niger', 'Africa', 'XOF'),
('NG', 'Nigeria', 'Africa', 'NGN'),
('RW', 'Rwanda', 'Africa', 'RWF'),
('ST', 'Sao Tome and Principe', 'Africa', 'STN'),
('SN', 'Senegal', 'Africa', 'XOF'),
('SC', 'Seychelles', 'Africa', 'SCR'),
('SL', 'Sierra Leone', 'Africa', 'SLE'),
('SO', 'Somalia', 'Africa', 'SOS'),
('ZA', 'South Africa', 'Africa', 'ZAR'),
('SS', 'South Sudan', 'Africa', 'SSP'),
('SD', 'Sudan', 'Africa', 'SDG'),
('TZ', 'Tanzania', 'Africa', 'TZS'),
('TG', 'Togo', 'Africa', 'XOF'),
('TN', 'Tunisia', 'Africa', 'TND'),
('UG', 'Uganda', 'Africa', 'UGX'),
('ZM', 'Zambia', 'Africa', 'ZMW'),
('ZW', 'Zimbabwe', 'Africa', 'ZWL'),

-- =====================================================
-- AMERICAS (UN M49 Region Code: 019)
-- =====================================================
('AR', 'Argentina', 'Americas', 'ARS'),
('BS', 'Bahamas', 'Americas', 'BSD'),
('BB', 'Barbados', 'Americas', 'BBD'),
('BZ', 'Belize', 'Americas', 'BZD'),
('BM', 'Bermuda', 'Americas', 'BMD'),
('BO', 'Bolivia', 'Americas', 'BOB'),
('BR', 'Brazil', 'Americas', 'BRL'),
('CA', 'Canada', 'Americas', 'CAD'),
('CL', 'Chile', 'Americas', 'CLP'),
('CO', 'Colombia', 'Americas', 'COP'),
('CR', 'Costa Rica', 'Americas', 'CRC'),
('CU', 'Cuba', 'Americas', 'CUP'),
('DO', 'Dominican Republic', 'Americas', 'DOP'),
('EC', 'Ecuador', 'Americas', 'USD'),
('SV', 'El Salvador', 'Americas', 'USD'),
('FK', 'Falkland Islands', 'Americas', 'FKP'),
('GF', 'French Guiana', 'Americas', 'EUR'),
('GL', 'Greenland', 'Americas', 'DKK'),
('GT', 'Guatemala', 'Americas', 'GTQ'),
('GY', 'Guyana', 'Americas', 'GYD'),
('HT', 'Haiti', 'Americas', 'HTG'),
('HN', 'Honduras', 'Americas', 'HNL'),
('JM', 'Jamaica', 'Americas', 'JMD'),
('MX', 'Mexico', 'Americas', 'MXN'),
('NI', 'Nicaragua', 'Americas', 'NIO'),
('PA', 'Panama', 'Americas', 'PAB'),
('PY', 'Paraguay', 'Americas', 'PYG'),
('PE', 'Peru', 'Americas', 'PEN'),
('PM', 'Saint Pierre and Miquelon', 'Americas', 'EUR'),
('SR', 'Suriname', 'Americas', 'SRD'),
('TT', 'Trinidad and Tobago', 'Americas', 'TTD'),
('US', 'United States', 'Americas', 'USD'),
('UY', 'Uruguay', 'Americas', 'UYU'),
('VE', 'Venezuela', 'Americas', 'VES'),

-- =====================================================
-- ASIA (UN M49 Region Code: 142)
-- =====================================================
('AF', 'Afghanistan', 'Asia', 'AFN'),
('AM', 'Armenia', 'Asia', 'AMD'),
('AZ', 'Azerbaijan', 'Asia', 'AZN'),
('BH', 'Bahrain', 'Asia', 'BHD'),
('BD', 'Bangladesh', 'Asia', 'BDT'),
('BT', 'Bhutan', 'Asia', 'BTN'),
('BN', 'Brunei', 'Asia', 'BND'),
('KH', 'Cambodia', 'Asia', 'KHR'),
('CN', 'China', 'Asia', 'CNY'),
('CY', 'Cyprus', 'Asia', 'EUR'),
('GE', 'Georgia', 'Asia', 'GEL'),
('HK', 'Hong Kong', 'Asia', 'HKD'),
('IN', 'India', 'Asia', 'INR'),
('ID', 'Indonesia', 'Asia', 'IDR'),
('IR', 'Iran', 'Asia', 'IRR'),
('IQ', 'Iraq', 'Asia', 'IQD'),
('IL', 'Israel', 'Asia', 'ILS'),
('JP', 'Japan', 'Asia', 'JPY'),
('JO', 'Jordan', 'Asia', 'JOD'),
('KZ', 'Kazakhstan', 'Asia', 'KZT'),
('KW', 'Kuwait', 'Asia', 'KWD'),
('KG', 'Kyrgyzstan', 'Asia', 'KGS'),
('LA', 'Laos', 'Asia', 'LAK'),
('LB', 'Lebanon', 'Asia', 'LBP'),
('MO', 'Macao', 'Asia', 'MOP'),
('MY', 'Malaysia', 'Asia', 'MYR'),
('MV', 'Maldives', 'Asia', 'MVR'),
('MN', 'Mongolia', 'Asia', 'MNT'),
('MM', 'Myanmar', 'Asia', 'MMK'),
('NP', 'Nepal', 'Asia', 'NPR'),
('KP', 'North Korea', 'Asia', 'KPW'),
('OM', 'Oman', 'Asia', 'OMR'),
('PK', 'Pakistan', 'Asia', 'PKR'),
('PS', 'Palestine', 'Asia', 'ILS'),
('PH', 'Philippines', 'Asia', 'PHP'),
('QA', 'Qatar', 'Asia', 'QAR'),
('SA', 'Saudi Arabia', 'Asia', 'SAR'),
('SG', 'Singapore', 'Asia', 'SGD'),
('KR', 'South Korea', 'Asia', 'KRW'),
('LK', 'Sri Lanka', 'Asia', 'LKR'),
('SY', 'Syria', 'Asia', 'SYP'),
('TW', 'Taiwan', 'Asia', 'TWD'),
('TJ', 'Tajikistan', 'Asia', 'TJS'),
('TH', 'Thailand', 'Asia', 'THB'),
('TL', 'Timor-Leste', 'Asia', 'USD'),
('TR', 'Turkey', 'Asia', 'TRY'),
('TM', 'Turkmenistan', 'Asia', 'TMT'),
('AE', 'United Arab Emirates', 'Asia', 'AED'),
('UZ', 'Uzbekistan', 'Asia', 'UZS'),
('VN', 'Vietnam', 'Asia', 'VND'),
('YE', 'Yemen', 'Asia', 'YER'),

-- =====================================================
-- EUROPE (UN M49 Region Code: 150)
-- =====================================================
('AL', 'Albania', 'Europe', 'ALL'),
('AD', 'Andorra', 'Europe', 'EUR'),
('AT', 'Austria', 'Europe', 'EUR'),
('BY', 'Belarus', 'Europe', 'BYN'),
('BE', 'Belgium', 'Europe', 'EUR'),
('BA', 'Bosnia and Herzegovina', 'Europe', 'BAM'),
('BG', 'Bulgaria', 'Europe', 'BGN'),
('HR', 'Croatia', 'Europe', 'EUR'),
('CZ', 'Czech Republic', 'Europe', 'CZK'),
('DK', 'Denmark', 'Europe', 'DKK'),
('EE', 'Estonia', 'Europe', 'EUR'),
('FI', 'Finland', 'Europe', 'EUR'),
('FR', 'France', 'Europe', 'EUR'),
('DE', 'Germany', 'Europe', 'EUR'),
('GR', 'Greece', 'Europe', 'EUR'),
('HU', 'Hungary', 'Europe', 'HUF'),
('IS', 'Iceland', 'Europe', 'ISK'),
('IE', 'Ireland', 'Europe', 'EUR'),
('IT', 'Italy', 'Europe', 'EUR'),
('XK', 'Kosovo', 'Europe', 'EUR'),
('LV', 'Latvia', 'Europe', 'EUR'),
('LI', 'Liechtenstein', 'Europe', 'CHF'),
('LT', 'Lithuania', 'Europe', 'EUR'),
('LU', 'Luxembourg', 'Europe', 'EUR'),
('MT', 'Malta', 'Europe', 'EUR'),
('MD', 'Moldova', 'Europe', 'MDL'),
('MC', 'Monaco', 'Europe', 'EUR'),
('ME', 'Montenegro', 'Europe', 'EUR'),
('NL', 'Netherlands', 'Europe', 'EUR'),
('MK', 'North Macedonia', 'Europe', 'MKD'),
('NO', 'Norway', 'Europe', 'NOK'),
('PL', 'Poland', 'Europe', 'PLN'),
('PT', 'Portugal', 'Europe', 'EUR'),
('RO', 'Romania', 'Europe', 'RON'),
('RU', 'Russia', 'Europe', 'RUB'),
('SM', 'San Marino', 'Europe', 'EUR'),
('RS', 'Serbia', 'Europe', 'RSD'),
('SK', 'Slovakia', 'Europe', 'EUR'),
('SI', 'Slovenia', 'Europe', 'EUR'),
('ES', 'Spain', 'Europe', 'EUR'),
('SE', 'Sweden', 'Europe', 'SEK'),
('CH', 'Switzerland', 'Europe', 'CHF'),
('UA', 'Ukraine', 'Europe', 'UAH'),
('GB', 'United Kingdom', 'Europe', 'GBP'),
('VA', 'Vatican City', 'Europe', 'EUR'),

-- =====================================================
-- OCEANIA (UN M49 Region Code: 009)
-- =====================================================
('AU', 'Australia', 'Oceania', 'AUD'),
('CK', 'Cook Islands', 'Oceania', 'NZD'),
('FJ', 'Fiji', 'Oceania', 'FJD'),
('KI', 'Kiribati', 'Oceania', 'AUD'),
('MH', 'Marshall Islands', 'Oceania', 'USD'),
('FM', 'Micronesia', 'Oceania', 'USD'),
('NR', 'Nauru', 'Oceania', 'AUD'),
('NC', 'New Caledonia', 'Oceania', 'XPF'),
('NZ', 'New Zealand', 'Oceania', 'NZD'),
('NU', 'Niue', 'Oceania', 'NZD'),
('PW', 'Palau', 'Oceania', 'USD'),
('PG', 'Papua New Guinea', 'Oceania', 'PGK'),
('WS', 'Samoa', 'Oceania', 'WST'),
('SB', 'Solomon Islands', 'Oceania', 'SBD'),
('TO', 'Tonga', 'Oceania', 'TOP'),
('TV', 'Tuvalu', 'Oceania', 'AUD'),
('VU', 'Vanuatu', 'Oceania', 'VUV')
ON CONFLICT (country_code) DO NOTHING;



-- Display inserted countries count
-- SELECT COUNT(*) as total_countries FROM country;

-- =====================================================
-- ELECTRONICS PRODUCT CATEGORIES DATA (HS CODES)
-- =====================================================

INSERT INTO product_categories (hs_code, category_name, description, is_active) VALUES
-- Semiconductors and Electronic Components (HS 85)
(850110, 'Electric Motors ≤ 37.5W', 'Electric motors of an output not exceeding 37.5 W',  true),
(850120, 'Universal AC/DC Motors > 37.5W', 'Universal AC/DC motors of an output exceeding 37.5 W',  true),
(850131, 'DC Motors > 37.5W ≤ 750W', 'DC motors of an output exceeding 37.5 W but not exceeding 750 W',  true),
(850132, 'DC Motors > 750W ≤ 75kW', 'DC motors of an output exceeding 750 W but not exceeding 75 kW',  true),
(850140, 'AC Motors > 37.5W', 'AC motors, single-phase, of an output exceeding 37.5 W',  true),
(850211, 'Diesel Generating Sets ≤ 75kVA', 'Generating sets with compression-ignition internal combustion piston engines, ≤ 75kVA',  true),
(850212, 'Diesel Generating Sets 75-375kVA', 'Generating sets with compression-ignition internal combustion piston engines, 75-375kVA',  true),
(850213, 'Diesel Generating Sets > 375kVA', 'Generating sets with compression-ignition internal combustion piston engines, > 375kVA',  true),
(850231, 'Wind-powered Generating Sets', 'Wind-powered generating sets',  true),
(850239, 'Other Generating Sets', 'Other generating sets',  true),
(850300, 'Electric Motor Parts', 'Parts suitable for use solely or principally with electric motors and generators',  true),
(850410, 'Ballasts for Discharge Lamps', 'Ballasts for discharge lamps or tubes',  true),
(850421, 'Liquid Dielectric Transformers ≤ 650kVA', 'Liquid dielectric transformers having a power handling capacity not exceeding 650 kVA',  true),
(850422, 'Liquid Dielectric Transformers 650kVA-10MVA', 'Liquid dielectric transformers having a power handling capacity exceeding 650 kVA but not exceeding 10,000 kVA',  true),
(850423, 'Liquid Dielectric Transformers > 10MVA', 'Liquid dielectric transformers having a power handling capacity exceeding 10,000 kVA',  true),
(850431, 'Dry-type Transformers ≤ 1kVA', 'Transformers having a power handling capacity not exceeding 1 kVA',  true),
(850432, 'Dry-type Transformers 1-16kVA', 'Transformers having a power handling capacity exceeding 1 kVA but not exceeding 16 kVA',  true),
(850433, 'Dry-type Transformers 16-500kVA', 'Transformers having a power handling capacity exceeding 16 kVA but not exceeding 500 kVA',  true),
(850434, 'Dry-type Transformers > 500kVA', 'Transformers having a power handling capacity exceeding 500 kVA',  true),
(850440, 'Static Converters', 'Static converters',  true),
(850450, 'Inductors', 'Other inductors',  true),
(850490, 'Transformer Parts', 'Parts of transformers, static converters and inductors',  true),

-- Semiconductors (HS 8541-8542)
(854110, 'Diodes', 'Diodes, other than photosensitive or light-emitting diodes',  true),
(854121, 'Transistors ≤ 1W', 'Transistors, other than photosensitive transistors, with a dissipation rate of less than 1 W',  true),
(854129, 'Transistors > 1W', 'Transistors, other than photosensitive transistors, with a dissipation rate of 1 W or more',  true),
(854130, 'Thyristors', 'Thyristors, diacs and triacs, other than photosensitive devices',  true),
(854140, 'Photosensitive Semiconductors', 'Photosensitive semiconductor devices, including photovoltaic cells',  true),
(854150, 'Semiconductor Devices NES', 'Other semiconductor devices',  true),
(854160, 'Mounted Piezoelectric Crystals', 'Mounted piezoelectric crystals',  true),
(854170, 'LED Diodes', 'Light-emitting diodes (LED)',  true),
(854190, 'Semiconductor Device Parts', 'Parts of semiconductor devices and electronic integrated circuits',  true),

-- Integrated Circuits (HS 8542)
(854211, 'Memory Circuits - Cards/Modules', 'Cards incorporating an electronic integrated circuit (smart cards)',  true),
(854213, 'Metal Oxide Semiconductors', 'Metal oxide semiconductors (MOS technology)',  true),
(854214, 'Digital Monolithic ICs', 'Digital monolithic integrated circuits',  true),
(854219, 'Other Monolithic ICs', 'Other monolithic integrated circuits',  true),
(854221, 'Hybrid ICs - Thick/Thin Film', 'Hybrid integrated circuits for thick or thin film technology',  true),
(854229, 'Other Hybrid ICs', 'Other hybrid integrated circuits',  true),
(854231, 'Processors and Controllers', 'Processors and controllers, whether or not combined with memories, converters, logic circuits, amplifiers, clock and timing circuits',  true),
(854232, 'Memories', 'Memories',  true),
(854233, 'Amplifiers', 'Amplifiers',  true),
(854239, 'Other Electronic ICs', 'Other electronic integrated circuits',  true),
(854290, 'IC Parts', 'Parts of electronic integrated circuits and microassemblies',  true),

-- Electronic Equipment (HS 8517-8528)
(851711, 'Line Telephone Sets', 'Line telephone sets with cordless handsets',  true),
(851712, 'Cordless Telephones', 'Telephones for cellular networks or for other wireless networks',  true),
(851713, 'Smartphones', 'Smartphones',  true),
(851718, 'Other Telephone Sets', 'Other telephone sets and apparatus for transmission or reception of voice, images or other data',  true),
(851761, 'Base Stations', 'Base stations for wireless telecommunications',  true),
(851762, 'Machines for Reception/Conversion', 'Machines for the reception, conversion and transmission or regeneration of voice, images or other data',  true),
(851769, 'Other Telecom Equipment', 'Other apparatus for transmission or reception of voice, images or other data',  true),
(851770, 'Telecom Parts', 'Parts of telephone sets and other apparatus for transmission or reception of voice, images or other data',  true),

-- Computing Equipment (HS 8471)
(847110, 'Analog/Hybrid Computers', 'Analogue or hybrid automatic data processing machines',  true),
(847141, 'Digital Computers ≤ 10kg', 'Comprising in the same housing at least a central processing unit and an input and output unit, whether or not combined, weighing not more than 10 kg',  true),
(847149, 'Other Digital Processing Units', 'Other digital automatic data processing machines presented in the form of systems',  true),
(847150, 'Processing Units', 'Processing units, whether or not containing in the same housing one or two of the following types of unit: storage units, input units, output units',  true),
(847160, 'Input/Output Units', 'Input or output units, whether or not containing storage units in the same housing',  true),
(847170, 'Storage Units', 'Storage units',  true),
(847180, 'Other ADP Units', 'Other units of automatic data processing machines',  true),
(847190, 'ADP Machine Parts', 'Parts and accessories of automatic data processing machines and units thereof',  true),

-- Electronic Displays (HS 8528)
(852851, 'Monitors - CRT', 'Monitors capable of directly connecting to and designed for use with an automatic data processing machine, using cathode-ray tube technology',  true),
(852852, 'Monitors - LCD/LED', 'Monitors capable of directly connecting to and designed for use with an automatic data processing machine, using liquid crystal display (LCD) technology',  true),
(852859, 'Other Monitors', 'Other monitors capable of directly connecting to and designed for use with an automatic data processing machine',  true),
(852861, 'Projectors - CRT', 'Projectors capable of directly connecting to and designed for use with an automatic data processing machine, using cathode-ray tube technology',  true),
(852862, 'Projectors - LCD', 'Projectors capable of directly connecting to and designed for use with an automatic data processing machine, using liquid crystal display (LCD) technology',  true),
(852869, 'Other Projectors', 'Other projectors capable of directly connecting to and designed for use with an automatic data processing machine',  true),
(852871, 'TV Receivers - CRT', 'Reception apparatus for television, whether or not incorporating radio-broadcast receivers or sound or video recording or reproducing apparatus, using cathode-ray tube technology',  true),
(852872, 'TV Receivers - LCD/LED/OLED', 'Reception apparatus for television, color, with a flat panel screen, using liquid crystal display (LCD), light-emitting diode (LED) or organic light-emitting diode (OLED) technology',  true),
(852873, 'TV Receivers - Other Color', 'Other color reception apparatus for television',  true),

-- Audio/Video Equipment (HS 8519-8522)
(851910, 'Coin/Token-operated Sound Equipment', 'Coin- or disc-operated record-players',  true),
(851920, 'Sound Recording/Reproducing Apparatus', 'Apparatus operated by coins, banknotes, bank cards, tokens or by other means of payment',  true),
(851930, 'Turntables', 'Turntables (record-decks)',  true),
(851940, 'Transcribing Machines', 'Transcribing machines and other sound reproducing apparatus',  true),
(851950, 'Telephone Answering Machines', 'Telephone answering machines',  true),

-- Electronic Components and Parts
(853110, 'Burglar/Fire Alarms', 'Burglar or fire alarms and similar apparatus',  true),
(853120, 'Indicator Panels', 'Indicator panels incorporating liquid crystal devices (LCD) or light-emitting diodes (LED)',  true),
(853180, 'Other Electric Sound/Visual Signaling', 'Other electric sound or visual signalling apparatus',  true),
(853190, 'Signaling Apparatus Parts', 'Parts of electric sound or visual signalling apparatus',  true),

-- Electronic Capacitors (HS 8532)
(853210, 'Fixed Capacitors - Tantalum', 'Fixed capacitors designed for use in 50/60 Hz circuits and having a reactive power handling capacity of not less than 0.5 kvar (power capacitors)',  true),
(853221, 'Fixed Capacitors - Tantalum', 'Tantalum capacitors',  true),
(853222, 'Fixed Capacitors - Aluminum Electrolytic', 'Aluminium electrolytic capacitors',  true),
(853223, 'Fixed Capacitors - Ceramic Dielectric', 'Capacitors with ceramic dielectric, single layer',  true),
(853224, 'Fixed Capacitors - Ceramic Multilayer', 'Capacitors with ceramic dielectric, multilayer',  true),
(853225, 'Fixed Capacitors - Paper/Plastic', 'Capacitors with dielectric of paper or plastics',  true),
(853229, 'Other Fixed Capacitors', 'Other fixed capacitors',  true),
(853230, 'Variable/Adjustable Capacitors', 'Variable or adjustable (pre-set) capacitors',  true),
(853290, 'Capacitor Parts', 'Parts of capacitors',  true)
ON CONFLICT (hs_code) DO NOTHING;

-- Display inserted product categories count
-- SELECT COUNT(*) as total_product_categories FROM product_categories;

-- =====================================================
-- USER DATA
-- =====================================================

INSERT INTO users (username, email, password_hash, is_admin, first_name, last_name, enabled, created_at) VALUES
('admin', 'admin@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', true, 'System', 'Administrator', true, NOW()),
('john.doe', 'john.doe@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', true, 'John', 'Doe', true, NOW()),
('jane.smith', 'jane.smith@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', false, 'Jane', 'Smith', true, NOW()),
('bob.wilson', 'bob.wilson@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', false, 'Bob', 'Wilson', true, NOW()),
('alice.brown', 'alice.brown@cs203.com', '$2a$08$IZyVxB7U36Xo8fzalZDqwel.gjVeraa5K37UIsEhx.dYH4HP9GmO.', false, 'Alice', 'Brown', true, NOW())
ON CONFLICT (username) DO NOTHING;

-- Note: Password is 'password123' for all users (hashed with BCrypt 8 rounds)

-- =====================================================
-- ELECTRONICS NATIONAL TARIFF LINES DATA
-- =====================================================

INSERT INTO national_tariff_lines (country_id, tariff_line_code, description, parent_hs_code, level, created_by, updated_by) VALUES
-- Singapore National Tariff Lines (Electronics Hub)
((SELECT country_id FROM country WHERE country_code = 'SG'), '851712.10', 'Smartphones - 5G capable', 851712, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '851712.20', 'Smartphones - 4G/LTE', 851712, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '851713.00', 'Smartphones - Premium tier', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '854110.10', 'Semiconductor diodes - Silicon', 854110, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '854110.20', 'Semiconductor diodes - Gallium arsenide', 854110, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '854231.00', 'Microprocessors - ARM based', 854231, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '854232.00', 'Memory chips - DDR4/DDR5 RAM', 854232, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'SG'), '852852.00', 'LCD monitors - 4K resolution', 852852, 10, 1, 1),

-- Malaysia National Tariff Lines (Manufacturing Hub)
((SELECT country_id FROM country WHERE country_code = 'MY'), '854231.10', 'Microprocessors - Intel x86', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), '854231.20', 'Microprocessors - AMD processors', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), '854232.10', 'Flash memory - NAND chips', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), '854232.20', 'DRAM memory - Server grade', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), '847141.00', 'Laptops - Consumer grade', 847141, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), '847149.10', 'Desktop computers - Gaming', 847149, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'MY'), '847149.20', 'Desktop computers - Business', 847149, 10, 2, 2),

-- Thailand National Tariff Lines (Assembly Operations)
((SELECT country_id FROM country WHERE country_code = 'TH'), '852872.10', 'LED TVs - 32-43 inch', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), '852872.20', 'LED TVs - 55-65 inch', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), '852872.30', 'LED TVs - 75+ inch', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), '851712.00', 'Mobile phones - Feature phones', 851712, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), '851713.00', 'Smartphones - Budget tier', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'TH'), '847160.00', 'Keyboards and mouse - Wireless', 847160, 10, 3, 3),

-- China National Tariff Lines (Major Electronics Producer)
((SELECT country_id FROM country WHERE country_code = 'CN'), '854231.00', 'Processors - Mobile chipsets', 854231, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), '854232.00', 'Memory - Consumer DRAM', 854232, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), '851713.00', 'Smartphones - Mid-range', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), '852852.00', 'Computer monitors - Gaming', 852852, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), '854110.00', 'Power diodes - High voltage', 854110, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), '854170.00', 'LED chips - White light', 854170, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CN'), '847141.00', 'Tablets - Android based', 847141, 10, 1, 1),

-- United States National Tariff Lines (High-tech Focus)
((SELECT country_id FROM country WHERE country_code = 'US'), '854231.00', 'Microprocessors - Server CPUs', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), '854232.00', 'Memory - ECC server RAM', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), '851713.00', 'Smartphones - iPhone series', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), '847141.01', 'Laptops - MacBook series', 847141, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), '847149.01', 'Workstations - High-end', 847149, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'US'), '852852.10', 'Professional monitors - 4K+', 852852, 10, 2, 2),

-- European Union (Germany) National Tariff Lines
((SELECT country_id FROM country WHERE country_code = 'DE'), '854231.00', 'Automotive processors', 854231, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), '854110.00', 'Industrial diodes', 854110, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), '851713.00', 'Smartphones - Enterprise', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), '847149.00', 'Industrial computers', 847149, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'DE'), '852852.00', 'Industrial displays', 852852, 10, 3, 3),

-- Japan National Tariff Lines (Precision Electronics)
((SELECT country_id FROM country WHERE country_code = 'JP'), '854231.000', 'Microcontrollers - Automotive', 854231, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), '854110.000', 'Precision diodes', 854110, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), '852872.000', 'OLED displays - Premium', 852872, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), '851713.000', 'Smartphones - Sony Xperia', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'JP'), '847141.000', 'Portable gaming devices', 847141, 10, 4, 4),

-- India National Tariff Lines (Growing Market)
((SELECT country_id FROM country WHERE country_code = 'IN'), '851713.00', 'Smartphones - Domestic brands', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), '852872.00', 'LED TVs - Budget segment', 852872, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), '847141.00', 'Tablets - Education sector', 847141, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), '854231.00', 'Mobile processors - Entry level', 854231, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'IN'), '854110.00', 'Basic diodes - Consumer electronics', 854110, 10, 5, 5),

-- South Korea National Tariff Lines (Technology Leader)
((SELECT country_id FROM country WHERE country_code = 'KR'), '854231.00', 'Mobile chipsets - Samsung Exynos', 854231, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'KR'), '854232.00', 'Memory chips - Samsung/SK Hynix', 854232, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'KR'), '851713.00', 'Smartphones - Samsung Galaxy', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'KR'), '852872.00', 'OLED displays - Samsung/LG', 852872, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'KR'), '854170.00', 'LED components - Samsung LED', 854170, 10, 1, 1),

-- Taiwan National Tariff Lines (Semiconductor Hub)
((SELECT country_id FROM country WHERE country_code = 'TW'), '854231.00', 'Processors - TSMC manufactured', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'TW'), '854232.00', 'Memory - DDR modules', 854232, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'TW'), '847141.00', 'Laptops - Acer/Asus', 847141, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'TW'), '847149.00', 'Motherboards - Asus/MSI', 847149, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'TW'), '852852.00', 'Computer monitors - ViewSonic', 852852, 10, 2, 2),

-- Vietnam National Tariff Lines (Manufacturing Hub)
((SELECT country_id FROM country WHERE country_code = 'VN'), '851713.00', 'Smartphones - Samsung assembly', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'VN'), '847141.00', 'Laptops - Dell/HP assembly', 847141, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'VN'), '847160.00', 'Keyboards - Logitech assembly', 847160, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'VN'), '852872.00', 'LED TVs - Samsung assembly', 852872, 10, 3, 3),

-- Indonesia National Tariff Lines (Growing Market)
((SELECT country_id FROM country WHERE country_code = 'ID'), '851713.00', 'Smartphones - Local assembly', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'ID'), '852872.00', 'LED TVs - Local assembly', 852872, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'ID'), '847141.00', 'Tablets - Budget segment', 847141, 10, 4, 4),

-- Philippines National Tariff Lines (Electronics Assembly)
((SELECT country_id FROM country WHERE country_code = 'PH'), '854231.00', 'Microprocessors - Assembly operations', 854231, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'PH'), '851713.00', 'Smartphones - Local brands', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'PH'), '847160.00', 'Computer peripherals', 847160, 10, 5, 5),

-- Australia National Tariff Lines (Developed Market)
((SELECT country_id FROM country WHERE country_code = 'AU'), '851713.00', 'Smartphones - Premium segment', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'AU'), '847141.00', 'Laptops - Business/Consumer', 847141, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'AU'), '852872.00', 'LED TVs - High-end', 852872, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'AU'), '854231.00', 'Processors - Gaming/Workstation', 854231, 10, 1, 1),

-- Hong Kong National Tariff Lines (Trade Hub)
((SELECT country_id FROM country WHERE country_code = 'HK'), '851713.00', 'Smartphones - Re-export hub', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'HK'), '854231.00', 'Processors - Trading hub', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'HK'), '854232.00', 'Memory chips - Distribution', 854232, 10, 2, 2),

-- Canada National Tariff Lines (NAFTA Partner)
((SELECT country_id FROM country WHERE country_code = 'CA'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'CA'), '847141.01', 'Laptops - BlackBerry legacy', 847141, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'CA'), '854231.00', 'Processors - Data centers', 854231, 10, 3, 3),

-- Mexico National Tariff Lines (NAFTA Manufacturing)
((SELECT country_id FROM country WHERE country_code = 'MX'), '851713.00', 'Smartphones - Maquiladora assembly', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'MX'), '847141.00', 'Laptops - Assembly operations', 847141, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'MX'), '852872.00', 'LED TVs - Manufacturing', 852872, 10, 4, 4),

-- Brazil National Tariff Lines (Major South American Market)
((SELECT country_id FROM country WHERE country_code = 'BR'), '851713.00', 'Smartphones - Domestic market', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'BR'), '847141.00', 'Tablets - Education/Consumer', 847141, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'BR'), '852872.00', 'LED TVs - Local assembly', 852872, 10, 5, 5),

-- United Kingdom National Tariff Lines (Post-Brexit)
((SELECT country_id FROM country WHERE country_code = 'GB'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'GB'), '847149.00', 'Computers - Financial services', 847149, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'GB'), '854231.00', 'Processors - ARM Holdings', 854231, 10, 1, 1),

-- France National Tariff Lines (EU Major Market)
((SELECT country_id FROM country WHERE country_code = 'FR'), '851713.00', 'Smartphones - Consumer electronics', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'FR'), '847141.00', 'Laptops - Enterprise market', 847141, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'FR'), '854231.00', 'Processors - Automotive/IoT', 854231, 10, 2, 2),

-- Italy National Tariff Lines (EU Market)
((SELECT country_id FROM country WHERE country_code = 'IT'), '851713.00', 'Smartphones - Fashion-tech', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'IT'), '847149.00', 'Industrial computers', 847149, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'IT'), '854110.00', 'Power semiconductors', 854110, 10, 3, 3),

-- Netherlands National Tariff Lines (EU Distribution Hub)
((SELECT country_id FROM country WHERE country_code = 'NL'), '851713.00', 'Smartphones - European distribution', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'NL'), '854231.00', 'Processors - Data center market', 854231, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'NL'), '847141.00', 'Laptops - Philips ecosystem', 847141, 10, 4, 4),

-- Russia National Tariff Lines (Large Market)
((SELECT country_id FROM country WHERE country_code = 'RU'), '851713.00', 'Smartphones - Domestic demand', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'RU'), '847141.00', 'Laptops - Enterprise/Consumer', 847141, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'RU'), '852872.00', 'LED TVs - Local assembly', 852872, 10, 5, 5),

-- Middle Eastern Countries National Tariff Lines
-- Turkey National Tariff Lines (Regional Electronics Hub)
((SELECT country_id FROM country WHERE country_code = 'TR'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'TR'), '847141.00', 'Laptops - Business sector', 847141, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'TR'), '852872.00', 'LED TVs - Domestic assembly', 852872, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'TR'), '854231.00', 'Processors - Industrial applications', 854231, 10, 1, 1),

-- UAE National Tariff Lines (Trade Hub)
((SELECT country_id FROM country WHERE country_code = 'AE'), '851713.00', 'Smartphones - Premium segment', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'AE'), '854231.00', 'Processors - Data centers', 854231, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'AE'), '847141.00', 'Laptops - Enterprise market', 847141, 10, 2, 2),

-- Saudi Arabia National Tariff Lines (Vision 2030)
((SELECT country_id FROM country WHERE country_code = 'SA'), '851713.00', 'Smartphones - Consumer electronics', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'SA'), '852872.00', 'LED TVs - Entertainment sector', 852872, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'SA'), '847149.00', 'Computers - Government digitization', 847149, 10, 3, 3),

-- Israel National Tariff Lines (Tech Innovation Hub)
((SELECT country_id FROM country WHERE country_code = 'IL'), '854231.00', 'Processors - R&D sector', 854231, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'IL'), '851713.00', 'Smartphones - Tech development', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'IL'), '847149.00', 'Workstations - Defense tech', 847149, 10, 4, 4),

-- Additional Asian Countries National Tariff Lines
-- Bangladesh National Tariff Lines (Textile Electronics)
((SELECT country_id FROM country WHERE country_code = 'BD'), '851713.00', 'Smartphones - Budget segment', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'BD'), '847160.00', 'Computer peripherals - Manufacturing', 847160, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'BD'), '852872.00', 'LED TVs - Consumer market', 852872, 10, 5, 5),

-- Pakistan National Tariff Lines (Growing Market)
((SELECT country_id FROM country WHERE country_code = 'PK'), '851713.00', 'Smartphones - Local assembly', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'PK'), '847141.00', 'Laptops - Education sector', 847141, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'PK'), '854110.00', 'Diodes - Basic electronics', 854110, 10, 1, 1),

-- Sri Lanka National Tariff Lines (Electronics Assembly)
((SELECT country_id FROM country WHERE country_code = 'LK'), '851713.00', 'Smartphones - Assembly operations', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'LK'), '847160.00', 'Keyboards - Export manufacturing', 847160, 10, 2, 2),

-- Additional European Countries National Tariff Lines
-- Ukraine National Tariff Lines (IT Services)
((SELECT country_id FROM country WHERE country_code = 'UA'), '847149.00', 'Computers - IT sector', 847149, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'UA'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'UA'), '854231.00', 'Processors - Software development', 854231, 10, 3, 3),

-- Belarus National Tariff Lines (EAEU Member)
((SELECT country_id FROM country WHERE country_code = 'BY'), '847141.00', 'Laptops - Regional assembly', 847141, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'BY'), '852872.00', 'LED TVs - Consumer electronics', 852872, 10, 4, 4),

-- Poland National Tariff Lines (EU Manufacturing Hub)
((SELECT country_id FROM country WHERE country_code = 'PL'), '851713.00', 'Smartphones - EU market', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'PL'), '847141.00', 'Laptops - Regional manufacturing', 847141, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'PL'), '854231.00', 'Processors - Automotive sector', 854231, 10, 5, 5),

-- African Countries National Tariff Lines
-- South Africa National Tariff Lines (Regional Leader)
((SELECT country_id FROM country WHERE country_code = 'ZA'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'ZA'), '847141.00', 'Laptops - Business sector', 847141, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'ZA'), '852872.00', 'LED TVs - Entertainment', 852872, 10, 1, 1),

-- Nigeria National Tariff Lines (West African Hub)
((SELECT country_id FROM country WHERE country_code = 'NG'), '851713.00', 'Smartphones - Mobile communications', 851713, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'NG'), '847160.00', 'Computer accessories', 847160, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'NG'), '852872.00', 'LED TVs - Consumer demand', 852872, 10, 2, 2),

-- Egypt National Tariff Lines (North African Hub)
((SELECT country_id FROM country WHERE country_code = 'EG'), '851713.00', 'Smartphones - Regional assembly', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'EG'), '847141.00', 'Laptops - Education/Business', 847141, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'EG'), '854110.00', 'Diodes - Electronics assembly', 854110, 10, 3, 3),

-- Kenya National Tariff Lines (East African Hub)
((SELECT country_id FROM country WHERE country_code = 'KE'), '851713.00', 'Smartphones - Mobile banking ecosystem', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'KE'), '847141.00', 'Tablets - Education sector', 847141, 10, 4, 4),

-- Ghana National Tariff Lines (West African Market)
((SELECT country_id FROM country WHERE country_code = 'GH'), '851713.00', 'Smartphones - Consumer electronics', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'GH'), '852872.00', 'LED TVs - Entertainment sector', 852872, 10, 5, 5),

-- Caribbean & Central American Countries National Tariff Lines
-- Costa Rica National Tariff Lines (Intel Operations)
((SELECT country_id FROM country WHERE country_code = 'CR'), '854231.00', 'Processors - Intel manufacturing', 854231, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CR'), '847141.00', 'Laptops - Assembly operations', 847141, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'CR'), '851713.00', 'Smartphones - Central American market', 851713, 10, 1, 1),

-- Dominican Republic National Tariff Lines (Free Trade Zones)
((SELECT country_id FROM country WHERE country_code = 'DO'), '847160.00', 'Computer peripherals - Export manufacturing', 847160, 10, 2, 2),
((SELECT country_id FROM country WHERE country_code = 'DO'), '851713.00', 'Smartphones - Caribbean market', 851713, 10, 2, 2),

-- Jamaica National Tariff Lines (Caribbean Hub)
((SELECT country_id FROM country WHERE country_code = 'JM'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 3, 3),
((SELECT country_id FROM country WHERE country_code = 'JM'), '847141.00', 'Laptops - Tourism/Business', 847141, 10, 3, 3),

-- Panama National Tariff Lines (Regional Trade Hub)
((SELECT country_id FROM country WHERE country_code = 'PA'), '851713.00', 'Smartphones - Regional distribution', 851713, 10, 4, 4),
((SELECT country_id FROM country WHERE country_code = 'PA'), '854231.00', 'Processors - Logistics sector', 854231, 10, 4, 4),

-- Oceania Countries National Tariff Lines
-- New Zealand National Tariff Lines (CPTPP Member)
((SELECT country_id FROM country WHERE country_code = 'NZ'), '851713.00', 'Smartphones - Consumer market', 851713, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'NZ'), '847141.00', 'Laptops - Business/Education', 847141, 10, 5, 5),
((SELECT country_id FROM country WHERE country_code = 'NZ'), '852852.00', 'Computer monitors - Professional', 852852, 10, 5, 5),

-- Fiji National Tariff Lines (Pacific Hub)
((SELECT country_id FROM country WHERE country_code = 'FJ'), '851713.00', 'Smartphones - Pacific island market', 851713, 10, 1, 1),
((SELECT country_id FROM country WHERE country_code = 'FJ'), '847141.00', 'Laptops - Tourism sector', 847141, 10, 1, 1);

-- =====================================================
-- TARIFF RATE DATA
-- =====================================================

INSERT INTO tariff_rates (tariff_rate, tariff_type, rate_unit, effective_date, expiry_date, preferential_tariff, 
                         importing_country_id, exporting_country_id, hs_code, created_at, updated_at) VALUES

-- Singapore importing electronics from various countries
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(5.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 847141, NOW(), NOW()),

(2.50, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 852872, NOW(), NOW()),

-- Malaysia importing electronics from various countries
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW()),

(15.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 851713, NOW(), NOW()),

(10.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 852872, NOW(), NOW()),

-- Thailand importing electronics from various countries
(20.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847160, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 852872, NOW(), NOW()),

(5.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 851713, NOW(), NOW()),

-- China importing electronics from various countries
(8.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854170, NOW(), NOW()),

(25.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854232, NOW(), NOW()),

-- United States importing electronics from various countries
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 847141, NOW(), NOW()),

(26.40, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 854110, NOW(), NOW()),

(4.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 852872, NOW(), NOW()),

-- European Union (Germany) importing electronics from various countries
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(12.80, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(10.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847149, NOW(), NOW()),

-- Japan importing electronics from various countries
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854232, NOW(), NOW()),

(38.50, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 (SELECT country_id FROM country WHERE country_code = 'TW'), 
 852872, NOW(), NOW()),

-- India importing electronics from various countries
(30.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854110, NOW(), NOW()),

(7.50, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IN'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 852872, NOW(), NOW()),

-- Cross-regional electronics trade examples
(0.00, 'NAFTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

(5.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'BR'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 851713, NOW(), NOW()),

(0.00, 'EU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'FR'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 854231, NOW(), NOW()),

-- Some expired rates for historical data
(12.00, 'MFN', 'ad valorem', '2023-01-01', '2023-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 852872, NOW(), NOW()),

(15.00, 'MFN', 'ad valorem', '2023-06-01', '2023-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW()),

-- South Korea tariff rates (Technology powerhouse)
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 (SELECT country_id FROM country WHERE country_code = 'TW'), 
 854231, NOW(), NOW()),

(8.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854232, NOW(), NOW()),

-- Taiwan tariff rates (Semiconductor hub)
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TW'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854231, NOW(), NOW()),

(10.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TW'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'TW'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 852872, NOW(), NOW()),

-- Vietnam tariff rates (Manufacturing hub)
(0.00, 'ASEAN', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 851713, NOW(), NOW()),

(20.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

(0.00, 'ASEAN', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 852872, NOW(), NOW()),

-- Indonesia tariff rates (Protected market)
(17.50, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'ID'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'ASEAN', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'ID'), 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 852872, NOW(), NOW()),

(25.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'ID'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

-- Philippines tariff rates (Electronics assembly)
(0.00, 'ASEAN', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'PH'), 
 (SELECT country_id FROM country WHERE country_code = 'SG'), 
 854231, NOW(), NOW()),

(30.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'PH'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

-- Australia tariff rates (Developed market)
(5.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

(0.00, 'CPTPP', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'AU'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854231, NOW(), NOW()),

-- Hong Kong tariff rates (Free port)
(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'HK'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'HK'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'HK'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 854232, NOW(), NOW()),

-- Canada tariff rates (NAFTA partner)
(0.00, 'NAFTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 851713, NOW(), NOW()),

(0.00, 'NAFTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 847141, NOW(), NOW()),

(6.10, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 854231, NOW(), NOW()),

-- Mexico tariff rates (NAFTA manufacturing)
(0.00, 'NAFTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 851713, NOW(), NOW()),

(15.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

(0.00, 'NAFTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'MX'), 
 (SELECT country_id FROM country WHERE country_code = 'CA'), 
 852872, NOW(), NOW()),

-- Brazil tariff rates (Protected market)
(60.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'BR'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(35.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'BR'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

(16.00, 'MERCOSUR', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'BR'), 
 (SELECT country_id FROM country WHERE country_code = 'AR'), 
 852872, NOW(), NOW()),

-- United Kingdom tariff rates (Post-Brexit)
(12.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'GB'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'GB'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847149, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'GB'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854231, NOW(), NOW()),

-- France tariff rates (EU common external tariff)
(0.00, 'EU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'FR'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 851713, NOW(), NOW()),

(14.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'FR'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'FR'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

-- Italy tariff rates (EU member)
(0.00, 'EU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'IT'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 851713, NOW(), NOW()),

(14.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IT'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847149, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'IT'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854110, NOW(), NOW()),

-- Netherlands tariff rates (EU distribution hub)
(0.00, 'EU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'NL'), 
 (SELECT country_id FROM country WHERE country_code = 'DE'), 
 851713, NOW(), NOW()),

(0.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'NL'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 854231, NOW(), NOW()),

(14.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'NL'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 847141, NOW(), NOW()),

-- Russia tariff rates (Import substitution policy)
(15.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'RU'), 
 (SELECT country_id FROM country WHERE country_code = 'CN'), 
 851713, NOW(), NOW()),

(20.00, 'MFN', 'ad valorem', '2024-01-01', '2024-12-31', false, 
 (SELECT country_id FROM country WHERE country_code = 'RU'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847141, NOW(), NOW()),

(0.00, 'EAEU', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'RU'), 
 (SELECT country_id FROM country WHERE country_code = 'BY'), 
 852872, NOW(), NOW()),

-- Additional cross-regional electronics trade
(0.00, 'CPTPP', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'NZ'), 
 (SELECT country_id FROM country WHERE country_code = 'JP'), 
 851713, NOW(), NOW()),

(25.00, 'GSP', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'BD'), 
 (SELECT country_id FROM country WHERE country_code = 'US'), 
 847160, NOW(), NOW()),

(0.00, 'FTA', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'CL'), 
 (SELECT country_id FROM country WHERE country_code = 'KR'), 
 854231, NOW(), NOW()),

-- ASEAN intra-regional trade
(0.00, 'ASEAN', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'MY'), 
 (SELECT country_id FROM country WHERE country_code = 'VN'), 
 851713, NOW(), NOW()),

(0.00, 'ASEAN', 'ad valorem', '2024-01-01', '2024-12-31', true, 
 (SELECT country_id FROM country WHERE country_code = 'TH'), 
 (SELECT country_id FROM country WHERE country_code = 'ID'), 
 847141, NOW(), NOW());

-- =====================================================
-- SEED SCRIPT COMPLETION VERIFICATION
-- =====================================================
-- Display summary counts to verify successful data insertion
-- Uncomment the following lines for debugging if needed:
-- SELECT 'Countries:' as table_name, COUNT(*) as count FROM country
-- UNION ALL
-- SELECT 'Product Categories:', COUNT(*) FROM product_categories
-- UNION ALL  
-- SELECT 'Users:', COUNT(*) FROM users
-- UNION ALL
-- SELECT 'National Tariff Lines:', COUNT(*) FROM national_tariff_lines
-- UNION ALL
-- SELECT 'Tariff Rates:', COUNT(*) FROM tariff_rates;

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

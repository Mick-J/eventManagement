-- Inserting country
INSERT INTO country (country_name, country_code, country_dial_code)
VALUES
    ('China', 'CN', '86'),
    ('France', 'FR', '33'),
    ('USA', 'US', '1'),
    ('Hong Kong', 'HK', '852'),
    ('Malaysia', 'MY', '60'),
    ('India', 'IN', '91'),
    ('Germany', 'DE', '49'),
    ('Italy', 'IT', '39'),
    ('Taiwan', 'TW', '886'),
    ('South Korea', 'KR', '82'),
    ('Singapore', 'SG', '65'),
    ('Switzerland', 'CH', '41'),
    ('Denmark', 'DK', '45'),
    ('Poland', 'PL', '48'),
    ('Tanzania', 'TZ', '255');

-- Inserting company
INSERT INTO company (company_name, website, country_id, contact_email, company_phone, company_fax)
VALUES
    ('Tech China', 'https://techchina.com', 1, 'info@techchina.com', '+86 287658881', '+86 287658881'),
    ('Tech France', 'https://techfrance.com', 2, 'info@techfrance.com', '+33 487554321', '+33 487554321'),
    ('Tech USA', 'https://techusa.com', 3, 'info@techusa.com', '+1 987654321', '+1 987654321'),
    ('Tech Hong Kong', 'https://techhongkong.com', 4, 'info@techhongkong.com', '+852 387654321', '+52 387654321'),
    ('Tech Malaysia', 'https://techmalaysia.com', 5, 'info@techmalaysia.com', '+60 887654351', '+60 887654351'),
    ('Tech India', 'https://techindia.com', 6, 'info@techindia.com', '+91 987659321', '+91 987659321'),
    ('Tech Germany', 'https://techgermany.com', 7, 'info@techgermany.com', '+49 687654321', '+49 687654321'),
    ('Tech Italy', 'https://techitaly.com', 8, 'info@techitaly.com', '+39 787654321', '+39 787654321'),
    ('Tech Taiwan', 'https://techtaiwan.com', 9, 'info@techtaiwan.com', '+886 587654321', '+886 587654321'),
    ('Tech South Korea', 'https://techkorea.com', 10, 'info@techkorea.com', '+82 989001321', '+82 989001321'),
    ('Tech Singapore', 'https://techsingapore.com', 11, 'info@techsingapore.com', '+65 387654321', '+65 387654321'),
    ('Tech Switzerland', 'https://techswitzerland.com', 12, 'info@techswitzerland.com', '+41 987854321', '+41 987854321'),
    ('Tech Denmark', 'https://techdenmark.com', 13, 'info@techdenmark.com', '+45 887554321', '+45 887554321'),
    ('Tech Poland', 'https://techpoland.com', 14, 'info@techpoland.com', '+48 287654321', '+48 287654321'),
    ('Tech Tanzania', 'https://techtanzania.com', 15, 'info@techtanzania.com', '+255 123456789', '+244 287884321');
-- Inserting Users
INSERT INTO user (user_firstname, user_lastname, user_password, user_email, user_phone, user_active, user_role)
VALUES
    ('John', 'Doe', '$2b$12$d9OKliB.PaXxykgniw875.TjiZhblXcZRezODTaiw.aj94Cx/lJc2', 'john@techchina.com', '+49 123456789', 'Y', 'ADMIN'),
    ('Louis', 'Philip', '$2b$12$kKgaE25N9pTf1jaO1EeIB.n570qQmAmtpo9K/FNvkgF3Dmr/N40cq', 'louis@techchina.com', '+1 123456789', 'Y', 'ADMIN'),
    ('Alice', 'Smith', '$2b$12$c73tWVemmjPKIfnsgW5LNudgnZcp3K5TPxqC5DZYPcxUbfNSuB14e', 'alice@techfrance.com', '+33 123456789', 'Y', 'USER'),
    ('Robert', 'Brown', '$2b$12$pa/VrLE8m6iU1fZC6jzq9Ot.j8jCtGWDJc7oyH7xu9z8XS.h52xaK', 'robert@techusa.com', '+1 123456789', 'Y', 'USER'),
    ('Emma', 'Williams', '$2b$12$grJlCpoCRrHMj6haxBgYmedjXB6od9Uf0K1N74rCofJSR3uMW8jY.', 'emma@techhongkong.com', '+852 123456789', 'Y', 'USER'),
    ('Liam', 'Jones', '$2b$12$cwQYtPPo5futccqK/iYxAu/badF0ERKrkOhJHzb/rK.mSzZZYdrx.', 'liam@techmalaysia.com', '+60 123456789', 'Y', 'USER'),
    ('Alice', 'Martin', '$2b$12$NF3ejmk.icacLJw5uzKQBe2gnuZXQQJ60iiNgowVO5zfSdg8KaDbu', 'alice@techusa.com', '+1 2025550101', 'Y', 'USER'),
    ('Hugo', 'Lefevre', '$2b$12$qK38cR/dyaSAvnAkwhwO..jll5RgaUutLjis8brNmg/7SA9zZfXK.', 'hugo@techfrance.com', '+33 612345678', 'Y', 'USER'),
    ('Carlos', 'Rodriguez', '$2b$12$Nq5KXE1ZCaBOB8Dv62eL6euVo985p6U9vKqr3HOitdz9XNULvuIkK', 'carlos@techgermany.com', '+34 699887766', 'Y', 'USER'),
    ('Elena', 'Kovalenko', '$2b$12$wLDgoTtY8OcIM.OmfOwUgOIosNphTl.ToYXaLYWtNcMzBDCsnFQhS', 'elena@techsingapore.com', '+7 9123456789', 'Y', 'USER'),
    ('Mei', 'Wang', '$2b$12$GGaU5ilNo5QGG2D5Ie4LoOyKRJXp3RK/XVOl0FOEJBDM9iXBbxhQS', 'mei@techchina.com', '+86 13987654321', 'Y', 'USER'),
    ('Raj', 'Patel', '$2b$12$u1XUogBqoA9VPuBbyWzg7uQYBYpPCH89trmuN2bsZE9piUr811LCK', 'raj@techindia.com', '+91 9876543210', 'Y', 'USER'),
    ('Sophie', 'Dubois', '$2b$12$d0vrOHW2kTZswyd1JfJeZedk1Dy0oaJxoA2M3VwEMlqy80SUXGeVW', 'sophie@techkorea.com', '+1 4165550202', 'Y', 'USER'),
    ('Luca', 'Bianchi', '$2b$12$hAt6NAyZgzlwpnrYRq.F8eBdu6AKKEwIYr.C0yxkqwTwUzQiwjmtC', 'luca@techitaly.com', '+39 3209876543', 'Y', 'USER'),
    ('Ahmed', 'Mohamed', '$2b$12$6hzaKkrpJyVVw9X02K0q1eRrJpmSYhEOVIxm3l7sCUwpphB1uLTHO', 'ahmed@techtaiwan.com', '+20 1002345678', 'Y', 'USER'),
    ('Emily', 'Chidenka', '$2b$12$4UIWF63DJlJkXFanZ9tbLuLMugMh7ownv4FESXDxfQ9FZaJ52Iz/W', 'emily@techswitzerland.com', '+255 7712345678', 'Y', 'USER'),
    ('Bernard', 'Sose', '$2b$12$w7fRpKsqP799eBojl7wznuirmkiY/wtS2IL.Wd9czXxKRIDo7/zMC', 'bernard@techdenmark.com', '+255 7712345678', 'Y', 'USER'),
    ('Huge', 'Si', '$2b$12$lacKiddHHWBmbyJMeZ/ngu3IAnzroL.Mq1nCxAiHJEiSNtxBqaMBu', 'si@techpoland.com', '+255 7712345678', 'Y', 'USER'),
    ('Michael', 'Kelman', '$2b$12$xfzjyol8fVkzdmyqYSCID.BcIeCEkB7clc8PF4T7SJt5fw/ydtwRG', 'michael@techtanzania.com', '+255 7712345678', 'Y', 'USER'),
    ('Chupa', 'Gary', '$2b$12$xfzjyol8fVkzdmyqYSCID.BcIeCEkB7clc8PF4T7SJt5fw/ydtwRG', 'Chupa@techitaly.com', '+255 7712345678', 'Y', 'USER');

-- Inserting User_company
INSERT INTO user_company (user_id, company_id, date_recruited, job_title, departement) VALUES
(1, 1, '2021-06-15', 'Software Developer', 'IT'),
(2, 1, '2022-03-10', 'Software Developer', 'IT'),
(3, 2, '2020-11-05', 'Marketing Manager', 'Marketing'),
(4, 3, '2019-09-20', 'HR Specialist', 'Human Resources'),
(5, 4, '2023-01-25', 'Financial Analyst', 'Finance'),
(6, 5, '2022-07-18', 'Project Manager', 'Operations'),
(7, 3, '2021-04-12', 'Sales Executive', 'Sales'),
(8, 2, '2020-08-30', 'Graphic Designer', 'Design'),
(9, 7, '2023-06-05', 'IT Support Engineer', 'IT'),
(10, 11, '2022-02-14', 'Business Analyst', 'Business Development'),
(11, 1, '2019-12-22', 'Database Administrator', 'IT'),
(12, 6, '2021-09-28', 'Customer Support', 'Customer Service'),
(13, 10, '2020-05-17', 'Network Engineer', 'IT'),
(14, 8, '2023-03-11', 'Digital Marketer', 'Marketing'),
(15, 9, '2019-07-07', 'Legal Advisor', 'Legal'),
(16, 12, '2021-11-19', 'Account Manager', 'Sales'),
(17, 13, '2022-08-24', 'Security Analyst', 'Cybersecurity'),
(18, 14, '2020-10-01', 'UX/UI Designer', 'Design'),
(19, 15, '2023-05-08', 'Data Scientist', 'Data Analytics');

-- Inserting  event_type
INSERT INTO event_type (event_type) VALUES ('CONFERENCE'), ('EXHIBITION'), ('MEETING');

-- Inserting event_tech
INSERT INTO event_tech (event_title, event_type_id, event_description, event_date_begin, event_date_end, event_time_begin, event_time_end, event_location, company_id, event_responsible_id)
VALUES

    ('Tech Expo 2025', 2, 'A global tech expo.', '2025-01-15', '2025-01-17', '09:00 AM', '06:00 PM', 'Shanghai, China', 1, 1),
    ('Business Summit 2023', 1, 'Annual business summit.', '2023-02-20', '2023-02-22', '10:00 AM', '05:00 PM', 'Paris, France', 2, 2),
    ('AI Innovations 2024', 1, 'Latest AI trends and developments.', '2024-04-10', '2024-04-12', '09:30 AM', '04:30 PM', 'Berlin, Germany', 3, 3),
    ('AI Summit Paris', 1, 'A summit on the latest AI innovations.', '2025-04-10', '2025-04-12', '09:00 AM', '05:00 PM', 'Paris, France', 2, 2),
    ('CyberSecurity World USA', 1, 'Exploring the future of cybersecurity.', '2025-05-15', '2025-05-16', '10:00 PM', '06:00 PM', 'New York, USA', 3, 3),
    ('Tech Innovators Meetup', 3, 'Networking for tech startups.', '2025-06-20', '2025-06-20', '14:00 PM', '08:00 PM', 'Hong Kong', 4, 4),
    ('GreenTech Malaysia', 2, 'Sustainable technology innovations.', '2025-07-05', '2025-07-07', '09:00 AM', '06:00 PM', 'Kuala Lumpur, Malaysia', 5, 5),
    ('Blockchain Summit India', 1, 'Blockchain trends and applications.', '2025-08-12', '2025-08-14', '10:00 PM', '05:00 PM', 'Mumbai, India', 6, 6),
    ('Smart Cities Germany', 2, 'The future of smart urban living.', '2025-09-18', '2025-09-20', '09:00 AM', '06:00 PM', 'Berlin, Germany', 7, 7),
    ('Tech Italy Hackathon', 2, '48-hour coding challenge.', '2025-10-10', '2025-10-12', '08:00 AM', '11:59 PM', 'Milan, Italy', 8, 8);



INSERT INTO event_tech (event_title, event_type_id, event_description, event_date_begin, event_date_end, event_time_begin, event_time_end, event_location, company_id, event_responsible_id)
VALUES
    ('IoT Conference Taiwan', 1, 'Internet of Things advancements.', '2025-11-05', '2025-11-06', '09:00 AM', '05:00 PM', 'Taipei, Taiwan', 9, 9),
    ('AI Robotics Expo Korea', 2, 'The latest in AI robotics.', '2025-12-01', '2025-12-03', '10:00 AM', '06:00 PM', 'Seoul, South Korea', 10, 10),
    ('FinTech Forum Singapore', 1, 'The future of financial technology.', '2025-12-15', '2025-12-16', '09:00 PM', '04:00 PM', 'Singapore', 11, 11),
    ('AI Global Summit', 1, 'Discussing AI trends and innovations.', '2027-01-22', '2027-01-24', '09:00 AM', '05:00 PM', 'Paris, France', 2, 2),
    ('CyberSecurity Asia', 2, 'Exploring cybersecurity advancements.', '2027-02-15', '2027-02-17', '10:00 AM', '06:00 PM', 'Hong Kong', 4, 4),
    ('SmartTech USA', 3, 'A high-level tech industry meeting.', '2027-03-10', '2027-03-10', '02:00 PM', '05:00 PM', 'New York, USA', 3, 3),
    ('IoT World Congress', 1, 'The latest in IoT technology.', '2027-04-05', '2027-04-07', '08:30 AM', '04:30 PM', 'Berlin, Germany', 7, 7),
    ('GreenTech Expo', 2, 'Innovations in sustainable technology.', '2027-05-18', '2027-05-20', '09:00 AM', '07:00 PM', 'Kuala Lumpur, Malaysia', 5, 5),
    ('Blockchain Revolution', 1, 'Blockchain applications in various industries.', '2027-06-12', '2027-06-14', '10:00 AM', '06:00 PM', 'Mumbai, India', 6, 6),
    ('Tech Connect', 3, 'A networking event for tech professionals.', '2027-07-22', '2027-07-22', '03:00 PM', '06:00 PM', 'Milan, Italy', 8, 8);

INSERT INTO event_tech (event_title, event_type_id, event_description, event_date_begin, event_date_end, event_time_begin, event_time_end, event_location, company_id, event_responsible_id)
VALUES
    ('AI Robotics Expo', 2, 'Latest advancements in AI and robotics.', '2027-09-08', '2027-09-10', '09:00 AM', '06:00 PM', 'Seoul, South Korea', 10, 10),
    ('Startup Pitch Night', 3, 'Startups showcase their projects.', '2027-10-14', '2027-10-14', '06:00 PM', '09:00 PM', 'Singapore', 11, 11),
    ('Future Mobility Expo', 2, 'New technologies in transportation.', '2027-11-30', '2027-12-02', '09:30 AM', '06:30 PM', 'Taipei, Taiwan', 9, 9),
    ('Quantum Computing Summit', 1, 'Advancements in quantum computing technology.', '2028-01-10', '2028-01-12', '09:00 AM', '05:00 PM', 'Beijing, China', 1, 1),
    ('Global Cybersecurity Forum', 1, 'Discussing the future of cybersecurity.', '2028-02-20', '2028-02-22', '10:00 AM', '04:00 PM', 'Paris, France', 2, 2),
    ('AI & Big Data Expo', 2, 'Showcasing AI and big data innovations.', '2028-03-15', '2028-03-17', '09:30 AM', '06:30 PM', 'New York, USA', 3, 3),
    ('Tech Hong Kong Startup Meetup', 3, 'Networking event for startups.', '2028-04-07', '2028-04-07', '02:00 PM', '06:00 PM', 'Hong Kong', 4, 4),
    ('Renewable Energy Conference', 1, 'Exploring green energy solutions.', '2028-05-22', '2028-05-24', '08:30 AM', '05:30 PM', 'Kuala Lumpur, Malaysia', 5, 5),
    ('Blockchain & Fintech Innovation', 2, 'Latest trends in blockchain and fintech.', '2028-07-02', '2028-07-04', '10:00 AM', '07:00 PM', 'Mumbai, India', 6, 6),
    ('Smart Manufacturing Expo', 2, 'Revolutionizing manufacturing with technology.', '2028-09-10', '2028-09-12', '09:00 AM', '05:00 PM', 'Berlin, Germany', 7, 7);

INSERT INTO event_tech (event_title, event_type_id, event_description, event_date_begin, event_date_end, event_time_begin, event_time_end, event_location, company_id, event_responsible_id)
VALUES
    ('AI Ethics & Governance Roundtable', 3, 'Discussing the ethics of AI development.', '2028-10-18', '2028-10-18', '03:00 PM', '07:00 PM', 'Rome, Italy', 8, 8),
    ('5G & IoT World Summit', 1, 'The future of 5G and IoT technologies.', '2028-11-27', '2028-11-29', '09:00 AM', '05:00 PM', 'Taipei, Taiwan', 9, 9),
    ('Autonomous Vehicles Forum', 3, 'Debating the impact of self-driving tech.', '2028-12-14', '2028-12-14', '02:30 PM', '06:30 PM', 'Seoul, South Korea', 10, 10),
    ('Future Tech Summit', 1, 'Exploring future innovations in technology.', '2029-01-12', '2029-01-14', '09:00 AM', '05:00 PM', 'Shanghai, China', 1, 1),
    ('AI & Robotics Exhibition', 2, 'Showcasing the latest in AI and robotics.', '2029-02-22', '2029-02-24', '10:00 AM', '06:00 PM', 'Paris, France', 2, 2),
    ('Cybersecurity Roundtable', 3, 'Discussing cybersecurity challenges and solutions.', '2029-03-18', '2029-03-18', '02:00 PM', '06:00 PM', 'New York, USA', 3, 3),
    ('Smart Cities Conference', 1, 'Innovations in smart city development.', '2029-04-10', '2029-04-12', '09:30 AM', '04:30 PM', 'Hong Kong', 4, 4),
    ('Blockchain & Crypto Expo', 2, 'Exhibition on blockchain and cryptocurrency.', '2029-05-05', '2029-05-07', '10:00 AM', '07:00 PM', 'Kuala Lumpur, Malaysia', 5, 5),
    ('Renewable Energy Innovation', 1, 'Green energy and sustainability discussions.', '2029-06-15', '2029-06-17', '08:30 AM', '05:30 PM', 'Mumbai, India', 6, 6),
    ('Automotive Tech Forum', 3, 'The future of electric and autonomous vehicles.', '2029-08-20', '2029-08-20', '03:00 PM', '07:00 PM', 'Berlin, Germany', 7, 7);

INSERT INTO event_tech (event_title, event_type_id, event_description, event_date_begin, event_date_end, event_time_begin, event_time_end, event_location, company_id, event_responsible_id)
VALUES
    ('Digital Transformation Summit', 1, 'Adapting businesses to digital evolution.', '2029-09-25', '2029-09-27', '09:00 AM', '05:00 PM', 'Rome, Italy', 8, 8),
    ('IoT & 5G World Expo', 2, 'Exploring IoT and 5G advancements.', '2029-11-10', '2029-11-12', '10:00 AM', '06:30 PM', 'Taipei, Taiwan', 9, 9),
    ('Tech Ethics & AI Safety Meetup', 3, 'Debating ethics and risks in AI.', '2029-12-14', '2029-12-14', '02:30 PM', '06:30 PM', 'Seoul, South Korea', 10, 10),
    ('Global AI Summit', 1, 'Advancements in artificial intelligence and machine learning.', '2030-01-20', '2030-01-22', '09:00 AM', '05:30 PM', 'Beijing, China', 1, 1),
    ('Future of Mobility Expo', 2, 'Showcasing innovations in electric and autonomous vehicles.', '2030-02-15', '2030-02-17', '10:00 AM', '06:00 PM', 'Paris, France', 2, 2),
    ('Cybersecurity Leadership Meeting', 3, 'Discussing strategies for enhancing cybersecurity.', '2030-03-08', '2030-03-08', '02:00 PM', '06:00 PM', 'New York, USA', 3, 3),
    ('Smart Manufacturing Conference', 1, 'Exploring Industry 4.0 and automation.', '2030-04-12', '2030-04-14', '09:30 AM', '04:30 PM', 'Hong Kong', 4, 4),
    ('Green Energy World Expo', 2, 'Innovations in sustainable and renewable energy.', '2030-05-18', '2030-05-20', '10:00 AM', '07:00 PM', 'Kuala Lumpur, Malaysia', 5, 5),
    ('Digital Economy Forum', 1, 'The impact of digital transformation on global markets.', '2030-06-22', '2030-06-24', '08:30 AM', '05:30 PM', 'Bangalore, India', 6, 6),
    ('FinTech & Blockchain Roundtable', 3, 'Trends and challenges in financial technology and blockchain.', '2030-08-05', '2030-08-05', '03:00 PM', '07:00 PM', 'Berlin, Germany', 7, 7),
    ('5G & IoT Innovation Summit', 1, 'Exploring 5G networks and IoT applications.', '2030-09-10', '2030-09-12', '09:00 AM', '05:00 PM', 'Milan, Italy', 8, 8),
    ('AR & VR World Exhibition', 2, 'Showcasing the latest advancements in augmented and virtual reality.', '2030-11-07', '2030-11-09', '10:00 AM', '06:30 PM', 'Taipei, Taiwan', 9, 9),
    ('AI Ethics & Governance Meetup', 3, 'Discussing responsible AI development and regulations.', '2030-12-16', '2030-12-16', '02:30 PM', '06:30 PM', 'Seoul, South Korea', 10, 10);

-- Inserting Company Attendees
INSERT INTO company_attendee ( user_id, event_id)
VALUES
    ( 2, 1),
    ( 1, 1),
    ( 3, 1),
    ( 5, 1),

    ( 2, 2),
    ( 1, 2),
    ( 3, 2),
    ( 5, 2);

-- Inserting lead
INSERT INTO lead_event (lead_firstname, lead_lastname, lead_email, lead_phone, lead_company_name, lead_interest_level, comment, lead_state, lead_b_card_img)
VALUES
    ('Michael', 'Johnson', 'michael@randomcorp.com', '+86 123456789', 'RandomCorp', 'HIGH', 'Interested in partnership.', 'PENDING', 'michael_RandomCorp_card.png'),
    ('Emily', 'Davis', 'emily@businessglobal.com', '+33 123456789', 'BusinessGlobal', 'MEDIUM', 'Needs more details.', 'PROCESSED', 'emily_BusinessGlobal_card.png'),
    ('Sophia', 'White', 'sophia@nextgenventures.com', '+1 123456789', 'NextGen Ventures', 'LOW', 'Exploring options.', 'IGNORE', 'sophia_NextGenVentures_card.png'),
    ('James', 'Brown', 'james@innovatech.com', '+44 7986123456', 'Innovatech', 'LOW', 'Not interested at the moment.', 'PENDING', 'James_Innovatech_card.png'),
    ('Emily', 'White', 'emily@greenenergy.com', '+33 654987123', 'GreenEnergy', 'HIGH', 'Excited about future partnerships.', 'PENDING', 'Emily_GreenEnergy_card.png'),
    ('David', 'Black', 'david@smartworks.com', '+49 123456789', 'SmartWorks', 'MEDIUM', 'Potential interest in services.', 'PENDING', 'David_SmartWorks_card.png'),
    ('Olivia', 'Miller', 'olivia@clearpath.com', '+61 400123456', 'ClearPath', 'HIGH', 'Looking forward to future ventures.', 'PENDING', 'Olivia_ClearPath_card.png'),
    ('Liam', 'Wilson', 'liam@infinitysystems.com', '+1 555789456', 'InfinitySystems', 'LOW', 'Not the right fit at the moment.', 'PENDING', 'Liam_InfinitySystems_card.png'),
    ('Sophia', 'Martinez', 'sophia@datalytics.com', '+34 912345678', 'Datalytics', 'HIGH', 'Eager to discuss collaboration.', 'PENDING', 'Sophia_Datalytics_card.png'),
    ('Ethan', 'Harris', 'ethan@synergycorp.com', '+44 777123456', 'SynergyCorp', 'MEDIUM', 'Would like more information.', 'PENDING', 'Ethan_SynergyCorp_card.png'),
    ('Charlotte', 'Clark', 'charlotte@bluewave.com', '+1 2123456789', 'BlueWave', 'HIGH', 'Looking for innovative solutions.', 'PENDING', 'Charlotte_BlueWave_card.png'),
    ('Mason', 'Lewis', 'mason@techworks.com', '+33 765432109', 'TechWorks', 'LOW', 'Not ready for a partnership yet.', 'PENDING', 'Mason_TechWorks_card.png'),
    ('Amelia', 'Young', 'amelia@futurelabs.com', '+49 234567890', 'FutureLabs', 'MEDIUM', 'Considering options for future projects.', 'PENDING', 'Amelia_FutureLabs_card.png'),
    ('Benjamin', 'Scott', 'benjamin@titanindustries.com', '+61 412345678', 'TitanIndustries', 'HIGH', 'Excited about tech innovations.', 'PENDING', 'Benjamin_TitanIndustries_card.png'),
    ('Isabella', 'King', 'isabella@cleantechsolutions.com', '+1 3456789012', 'CleanTechSolutions', 'LOW', 'Currently not a priority.', 'PENDING', 'Isabella_CleanTechSolutions_card.png'),
    ('Henry', 'Lee', 'henry@matrixcorp.com', '+44 701234567', 'MatrixCorp', 'MEDIUM', 'Considering future expansion opportunities.', 'PENDING', 'Henry_MatrixCorp_card.png'),
    ('Mia', 'Walker', 'mia@nextgeninnovations.com', '+33 650987654', 'NextGenInnovations', 'HIGH', 'Very interested in technology integration.', 'PENDING', 'Mia_NextGenInnovations_card.png');


INSERT INTO lead_event (lead_firstname, lead_lastname, lead_email, lead_phone, lead_company_name, lead_interest_level, comment, lead_state, lead_b_card_img)
VALUES
    ('Lucas', 'Anderson', 'lucas@brightfuture.com', '+1 4156789012', 'BrightFuture', 'HIGH', 'Interested in AI collaborations.', 'PENDING', 'Lucas_BrightFuture_card.png'),
    ('Ava', 'Roberts', 'ava@reliablesolutions.com', '+44 755432109', 'ReliableSolutions', 'MEDIUM', 'Looking into new opportunities.', 'PENDING', 'Ava_ReliableSolutions_card.png'),
    ('Oliver', 'Jackson', 'oliver@globaltech.com', '+61 4123456789', 'GlobalTech', 'LOW', 'Currently not pursuing new ventures.', 'PENDING', 'Oliver_GlobalTech_card.png'),
    ('Sophia', 'Green', 'sophia@nextleveltechnologies.com', '+33 654321987', 'NextLevelTechnologies', 'HIGH', 'Very excited about future potential.', 'PENDING', 'Sophia_NextLevelTechnologies_card.png'),
    ('William', 'Adams', 'william@creativesolutions.com', '+1 5101234567', 'CreativeSolutions', 'MEDIUM', 'Interested in discussing possibilities.', 'PENDING', 'William_CreativeSolutions_card.png'),
    ('Mason', 'Taylor', 'mason@supersolutions.com', '+44 7054321098', 'SuperSolutions', 'LOW', 'Not currently focused on partnerships.', 'PENDING', 'Mason_SuperSolutions_card.png'),
    ('Charlotte', 'Harris', 'charlotte@prodigyinnovations.com', '+1 9876543210', 'ProdigyInnovations', 'HIGH', 'Looking for strategic partnerships.', 'PENDING', 'Charlotte_ProdigyInnovations_card.png'),
    ('Eliot', 'Walker', 'eliot@brightmind.com', '+33 617849503', 'BrightMind', 'MEDIUM', 'Open to discussing potential collaborations.', 'PENDING', 'Eliot_BrightMind_card.png'),
    ('Isla', 'Perez', 'isla@techpioneers.com', '+49 123457890', 'TechPioneers', 'LOW', 'Not currently pursuing new ventures.', 'PENDING', 'Isla_TechPioneers_card.png'),
    ('Jack', 'Collins', 'jack@inspiregroup.com', '+61 400234567', 'InspireGroup', 'HIGH', 'Excited about technology and partnerships.', 'PENDING', 'Jack_InspireGroup_card.png');

INSERT INTO lead_event (lead_firstname, lead_lastname, lead_email, lead_phone, lead_company_name, lead_interest_level, comment, lead_state, lead_b_card_img)
VALUES
    ('Ethan', 'Walker', 'ethan@advancedsolutions.com', '+1 3039876543', 'AdvancedSolutions', 'HIGH', 'Looking for advanced tech solutions.', 'PENDING', 'Ethan_AdvancedSolutions_card.png'),
    ('Harper', 'Clark', 'harper@fintechworld.com', '+44 7556123456', 'FinTechWorld', 'MEDIUM', 'Exploring fintech innovations for future collaborations.', 'PENDING', 'Harper_FinTechWorld_card.png'),
    ('Alexander', 'Scott', 'alexander@bigdataanalytics.com', '+33 690123456', 'BigDataAnalytics', 'LOW', 'Currently not seeking new collaborations.', 'PENDING', 'Alexander_BigDataAnalytics_card.png'),
    ('Grace', 'Johnson', 'grace@envirotechsolutions.com', '+49 1712345678', 'EnviroTechSolutions', 'HIGH', 'Interested in eco-friendly technology solutions.', 'PENDING', 'Grace_EnviroTechSolutions_card.png'),
    ('Jackson', 'Moore', 'jackson@smartcitysolutions.com', '+1 2134567890', 'SmartCitySolutions', 'MEDIUM', 'Potential interest in urban tech solutions.', 'PENDING', 'Jackson_SmartCitySolutions_card.png'),
    ('Lily', 'Evans', 'lily@nextgenventures.com', '+44 7076543210', 'NextGenVentures', 'LOW', 'Currently focused on other projects.', 'PENDING', 'Lily_NextGenVentures_card.png'),
    ('Sebastian', 'Allen', 'sebastian@cloudtechsolutions.com', '+33 621234567', 'CloudTechSolutions', 'HIGH', 'Looking for cloud technology collaborations.', 'PENDING', 'Sebastian_CloudTechSolutions_card.png'),
    ('Zoe', 'White', 'zoe@biotechinnovations.com', '+49 1765432109', 'BiotechInnovations', 'MEDIUM', 'Looking into biotech-related opportunities.', 'PENDING', 'Zoe_BiotechInnovations_card.png'),
    ('Daniel', 'King', 'daniel@securewebsolutions.com', '+1 3059876543', 'SecureWebSolutions', 'LOW', 'Not looking for partnerships at this time.', 'PENDING', 'Daniel_SecureWebSolutions_card.png'),
    ('Aiden', 'Martin', 'aiden@nextgenhealth.com', '+44 7051234567', 'NextGenHealth', 'HIGH', 'Interested in healthcare technology innovations.', 'PENDING', 'Aiden_NextGenHealth_card.png');

INSERT INTO lead_event_tech (event_id, lead_id)
VALUES  (1, 1),
        (1, 2),
        (1, 3),
        (2, 1),
        (2, 2),
        (3, 3),
        (53, 1),
        (53, 2),
        (53, 3);
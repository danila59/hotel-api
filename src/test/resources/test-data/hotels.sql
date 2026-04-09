
DELETE FROM hotel_amenities;
DELETE FROM hotels;

INSERT INTO hotels (id, name, description, brand, house_number, street, city, country, post_code, phone, email, check_in, check_out)
VALUES
    (100, 'Hilton Minsk', 'Luxury hotel in center', 'Hilton', '9', 'Pobediteley Ave', 'Minsk', 'Belarus', '220004', '+375171234567', 'hilton@test.com', '14:00', '12:00'),

    (101, 'Marriott Moscow', 'Business hotel', 'Marriott', '1', 'Tverskaya', 'Moscow', 'Russia', '125009', '+74951234567', 'marriott@test.com', '15:00', '12:00'),

    (102, 'Hilton Moscow', 'Premium hotel', 'Hilton', '10', 'Arbat', 'Moscow', 'Russia', '119002', '+74959876543', 'hiltonmsk@test.com', '14:00', '12:00');
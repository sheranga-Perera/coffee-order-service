-- Insert test customer
INSERT INTO customer (id, name, address, mobile_number, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440002', 'Test Customer', '123 Test St', '1234567890', CURRENT_TIMESTAMP);

-- Insert test shop
INSERT INTO shop (id, name, longitude, latitude, queues, max_queue_size, open_time, close_time, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Coffee Haven', '40.7128', '-74.0060', 2, 10, '08:00:00', '20:00:00', CURRENT_TIMESTAMP);

-- Insert test queue for the shop
INSERT INTO queue (id, shop_id, customer_id, max_size, current_size, status)
VALUES ('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440002', 10, 0, 'ACTIVE'); 
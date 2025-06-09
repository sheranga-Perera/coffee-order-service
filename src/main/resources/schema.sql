-- Create Customer table
CREATE TABLE IF NOT EXISTS customer (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(225),
    mobile_number VARCHAR(10),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create Menu table
CREATE TABLE IF NOT EXISTS menu (
    id UUID PRIMARY KEY,
    shop_id UUID,
    item_id UUID
);

-- Create Shop table
CREATE TABLE IF NOT EXISTS shop (
    id UUID PRIMARY KEY,
    name VARCHAR(225) NOT NULL,
    longitude VARCHAR(225),
    latitude VARCHAR(225),
    menu_id UUID,
    queues INT,
    max_queue_size INT,
    open_time TIME,
    close_time TIME,
    created_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (menu_id) REFERENCES menu(id)
);

-- Create Queue table
CREATE TABLE IF NOT EXISTS queue (
    id UUID PRIMARY KEY,
    customer_id UUID,
    shop_id UUID,
    max_size INT,
    current_size INT,
    status VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (shop_id) REFERENCES shop(id)
);

-- Create Orders table
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    customer_id UUID,
    shop_id UUID,
    menu_item VARCHAR(100),
    queue_id UUID,
    queue_position INT,
    status VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (shop_id) REFERENCES shop(id),
    FOREIGN KEY (queue_id) REFERENCES queue(id)
); 
-- Test database initialization script
-- This script runs when the MariaDB container starts

-- Create test database if it doesn't exist
CREATE DATABASE IF NOT EXISTS test_eventdb;

-- Use the test database
USE test_eventdb;

-- The application will create tables via JPA/Hibernate

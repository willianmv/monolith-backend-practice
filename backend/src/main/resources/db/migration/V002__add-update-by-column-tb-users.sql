ALTER TABLE tb_users
ADD COLUMN updated_by bigint;

ALTER TABLE tb_users
ADD CONSTRAINT fk_tb_users_updated_by FOREIGN KEY (updated_by) REFERENCES tb_users (id) ON DELETE SET NULL;
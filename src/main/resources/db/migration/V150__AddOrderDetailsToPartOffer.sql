alter table t_part_offer
add column if not exists order_details jsonb;
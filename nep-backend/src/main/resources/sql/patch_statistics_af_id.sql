-- 已有库升级补丁：为 statistics 增加 af_id，并尽量回填历史关联
-- 用法：在 MySQL 中对库 nep 执行本脚本（可重复执行：先判断列是否存在）

USE nep;

-- 1) 加列（若已存在会报错，可忽略或先检查 information_schema）
SET @col_exists := (
  SELECT COUNT(*) FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'statistics'
    AND COLUMN_NAME = 'af_id'
);

SET @sql := IF(
  @col_exists = 0,
  'ALTER TABLE `statistics` ADD COLUMN `af_id` int DEFAULT NULL COMMENT ''关联反馈单号'' AFTER `gm_id`',
  'SELECT ''af_id already exists'' AS info'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2) 精确匹配回填：监督员电话 + 地址 + 描述
UPDATE statistics s
JOIN aqi_feedback f
  ON s.fd_id = f.tel_id
 AND s.address = f.address
 AND s.information = f.information
SET s.af_id = f.af_id
WHERE s.af_id IS NULL;

-- 3) 兜底：仅地址+描述唯一匹配时回填（兼容历史 seed 中 fd_id 不一致的数据）
UPDATE statistics s
JOIN aqi_feedback f
  ON s.address = f.address
 AND s.information = f.information
SET s.af_id = f.af_id
WHERE s.af_id IS NULL
  AND (
    SELECT COUNT(*) FROM aqi_feedback x
    WHERE x.address = s.address AND x.information = s.information
  ) = 1;
